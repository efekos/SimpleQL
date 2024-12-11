/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package dev.efekos.simple_ql.data;

import dev.efekos.simple_ql.annotation.AutoIncrement;
import dev.efekos.simple_ql.annotation.Primary;
import dev.efekos.simple_ql.annotation.Type;
import dev.efekos.simple_ql.annotation.Unique;
import dev.efekos.simple_ql.exception.NoGetterException;
import dev.efekos.simple_ql.exception.NoSetterException;
import dev.efekos.simple_ql.implementor.Implementor;
import dev.efekos.simple_ql.query.Query;
import dev.efekos.simple_ql.query.QueryResult;
import dev.efekos.simple_ql.thread.UpdateActionThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * One of the main classes of SimpleQL, used to manage a table created using a {@link Database}. Each table will have a
 * list of {@link T} when using, and it'll be converted to table rows in the background when updating the database. All
 * update actions are done by {@link UpdateActionThread}, making updates a lot faster.
 *
 * @param <T> Type which rows of this table will become.
 */
public class Table<T extends TableRow<T>> {

    private static final Logger log = LoggerFactory.getLogger(Table.class);
    private final Database database;
    private final String name;
    private final Class<T> clazz;
    private final Map<Class<?>, Implementor<?, ?>> implementors = new HashMap<>();
    private Field primaryKey = null;

    /**
     * Creates a new table instance. This constructor isn't public as Tables should be created using
     * {@link Database#registerTable(String, Class, Implementor[])}.
     *
     * @param database     Parent of this table.
     * @param name         Name of this table to use on queries and updates.
     * @param clazz        Class of {@link T} to avoid the requirement of insane reflection.
     * @param implementors A list of {@link Implementor} to use while dealing with {@link T}.
     */
    Table(Database database, String name, Class<T> clazz, Implementor<?, ?>... implementors) {
        this.database = database;
        this.name = name;
        this.clazz = clazz;

        for (Implementor<?, ?> implementor : implementors) {
            this.implementors.put(grabClass(implementor), implementor);
        }

        for (Field field : clazz.getDeclaredFields())
            if (field.isAnnotationPresent(Primary.class)) {
                primaryKey = field;
                break;
            }
        if (primaryKey == null)
            throw new IllegalArgumentException("At least one primary key is required for " + clazz.getName());
    }

    @SuppressWarnings("unchecked")
    private <C> Class<C> grabClass(Implementor<C, ?> implementor) {
        try {
            for (java.lang.reflect.Type type : implementor.getClass().getGenericInterfaces())
                if (type instanceof ParameterizedType pt && pt.getRawType() == Implementor.class) {
                    java.lang.reflect.Type t = pt.getActualTypeArguments()[0];
                    return (Class<C>) Class.forName(t instanceof ParameterizedType ptt ? ptt.getRawType().getTypeName() : t.getTypeName());
                }
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    private String createGenerationCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(name);
        builder.append(" (");
        for (Field field : clazz.getDeclaredFields()) {
            String columnName = field.getName();
            boolean primary = field.isAnnotationPresent(Primary.class);
            boolean unique = field.isAnnotationPresent(Unique.class);
            boolean autoIncrement = field.isAnnotationPresent(AutoIncrement.class);

            if (!builder.toString().endsWith("(")) builder.append(",");
            builder.append(columnName);
            builder.append(" ");
            builder.append(findType(field));
            if (primary) builder.append(" PRIMARY KEY");
            if (primary || unique) builder.append(" UNIQUE");
            if (autoIncrement) builder.append(" AUTO_INCREMENT");
        }
        return builder.append(")").toString();
    }

    private String findType(Field field) {
        if (field.isAnnotationPresent(Type.class)) return field.getAnnotation(Type.class).value();
        Class<?> type = field.getType();
        if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(int.class)) return "INT";
        if (type.isAssignableFrom(double.class) || type.isAssignableFrom(float.class)) return "REAL";
        if (type.isAssignableFrom(UUID.class)) return "VARCHAR(36)";
        if (type.isAssignableFrom(String.class) || TableRowTypeAdapter.class.isAssignableFrom(type) || type.isEnum())
            return "TEXT";
        if (implementors.containsKey(type)) return implementors.get(type).type();
        throw new IllegalStateException("Could not determine a column type for field " + field);
    }

    /**
     * Runs an SQL query on the database to create the table if it doesn't exist.
     */
    void checkExistent() {
        new UpdateActionThread(database.getConnection(), createGenerationCode(), stmt1 -> stmt1).start();
    }

    /**
     * Cleans a row by saving its changed fields to the database, executing a statement for each field.
     *
     * @param row {@link T} instance to clean.
     */
    @SuppressWarnings("unchecked")
    void clean(T row) {
        if (!row.isDirty()) return;

        for (Field field : row.getClazz().getDeclaredFields()) {
            if (!row.isDirty(field.getName())) continue;
            field.setAccessible(true);
            row.getPrimaryField().setAccessible(true);
            Optional<SetterAction<Object>> setter = findSetter(field.getType());
            new UpdateActionThread(database.getConnection(), "UPDATE " + name + " SET " + field.getName() + "=? WHERE " + primaryKey.getName() + "= ?;", stmt -> {
                if (setter.isPresent()) {
                    if(implementors.containsKey(field.getType())) setter.get().set(stmt, 1, ((Implementor<Object,Object>)implementors.get(field.getType())).write(field.get(row)));
                    else setter.get().set(stmt, 1, field.get(row));
                }
                setField(row, row.getPrimaryField(), stmt, 2);
                return stmt;
            }).start();

        }
    }

    private boolean hasImplementor(Object o) {
        return implementors.containsKey(o.getClass());
    }

    @SuppressWarnings("unchecked")
    private Implementor<Object, Object> getImplementor(Object o) {
        return (Implementor<Object, Object>) implementors.get(o.getClass());
    }

    private Object writeUsingImplementor(Object o) {
        return hasImplementor(o) ? getImplementor(o).write(o) : o;
    }

    @SuppressWarnings("unchecked")
    private Object readUsingImplementor(Object o, Class<?> claz) {
        Implementor<Object, Object> implementor = (Implementor<Object, Object>) implementors.get(claz);
        return implementor != null ? implementor.read(o) : o;
    }

    /**
     * Creates a new row and inserts it to the database using one statement.
     *
     * @param propertyChanger Some code to run before inserting the row.
     * @return Created row as an instance if there are no errors, {@code null} otherwise.
     */
    public T insertRow(Consumer<T> propertyChanger) {
        try {
            Constructor<T> constructor = clazz.getConstructor(Class.class, Table.class);
            constructor.setAccessible(true);
            T instance = constructor.newInstance(clazz, this);
            propertyChanger.accept(instance);
            instance.cleanWithoutUpdate();

            new UpdateActionThread(database.getConnection(), createInsertionCode(), stmt -> {

                Field[] fields = clazz.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAnnotationPresent(AutoIncrement.class)) continue;
                    field.setAccessible(true);
                    Object value = field.get(instance);
                    Optional<SetterAction<Object>> setter = findSetter(value.getClass());
                    if (setter.isEmpty()) throw new NoSetterException(field);
                    setter.get().set(stmt, i + 1, writeUsingImplementor(value));
                }
                return stmt;
            }).start();

            return instance;
        } catch (Exception e) {
            log.error("Table row insertion error at table '" + name + "'", e);
            return null;
        }
    }

    private String createInsertionCode() {
        StringBuilder mainBuilder = new StringBuilder();
        mainBuilder.append("INSERT INTO ");
        mainBuilder.append(name);
        StringBuilder nameBuilder = new StringBuilder().append("(");
        StringBuilder valueBuilder = new StringBuilder().append("(");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoIncrement.class)) continue;
            if (!nameBuilder.toString().endsWith("(")) nameBuilder.append(", ");
            nameBuilder.append(field.getName());

            if (!valueBuilder.toString().endsWith("(")) valueBuilder.append(", ");
            valueBuilder.append("?");
        }
        nameBuilder.append(")");
        valueBuilder.append(")");
        return mainBuilder.append(" ").append(nameBuilder).append(" VALUES ").append(valueBuilder).toString();
    }

    /**
     * Queries rows by their primary keys which are defined in {@link T} using {@link Primary} annotation. Parameter
     * {@code key} must be the same type with the primary field of {@link T}.
     *
     * @param key Key to search rows.
     * @return An {@link Optional} that will have a {@link T} instance if the query was successfully executed and a row
     * was successfully found.
     * @throws IllegalStateException if {@code key} isn't the same type with {@link Primary} field of {@link T}.
     * @apiNote Does not use threads to execute query, might be slower than expected.
     */
    public Optional<T> getRow(Object key) {
        if (!primaryKey.getType().equals(key.getClass()))
            throw new IllegalStateException("Primary key of " + clazz.getName() + " is " + primaryKey.getType().getName() + ", not " + key.getClass().getName());
        try (PreparedStatement stmt = database.getConnection().prepareStatement(generateQueryCode())) {
            Optional<SetterAction<Object>> setter = findSetter(primaryKey.getType());
            if (setter.isEmpty()) throw new NoSetterException(primaryKey);
            setter.get().set(stmt, 1, key);
            ResultSet set = stmt.executeQuery();
            T i = null;

            while (set.next() && i == null) i = getFromRow(set);


            return Optional.ofNullable(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(clazz.getName() + " must have constructor " + clazz.getSimpleName() + "(Class,Table)");
        } catch (InstantiationException e) {
            throw new IllegalStateException(clazz.getName() + " cannot be instantiated because it is abstract");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException ignored) {
            return Optional.empty();
        }
    }

    private T getFromRow(ResultSet set) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        T i;
        Constructor<T> constructor = clazz.getConstructor(Class.class, Table.class);
        constructor.setAccessible(true);
        T instance = constructor.newInstance(clazz, this);

        // get all columns from set and assign them to instance fields
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Optional<? extends GetterAction<?>> getterOptional = findGetter(field.getType());
            if (getterOptional.isEmpty()) throw new NoGetterException(field);
            Object o = getterOptional.get().get(set, field.getName());
            field.set(instance, readUsingImplementor(o, field.getType()));
        }

        i = instance;
        return i;
    }

    private String generateQueryCode() {
        return "SELECT * FROM " +
                name +
                " WHERE " +
                primaryKey.getName() +
                " = ?;";
    }


    void setField(T row, Field field, PreparedStatement stmt, int index) {
        try {
            Optional<SetterAction<Object>> setter = findSetter(field.getType());
            if (setter.isEmpty()) throw new NoSetterException(field);
            field.setAccessible(true);
            setter.get().set(stmt, index, field.get(row));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Literally what the fuck. How did this even happen. I literally made it accessible the line before. What the fuck do you mean illegal access?", e);
        } catch (SQLException ignored) {
        }
    }


    Optional<SetterAction<Object>> findSetter(Class<?> c) {
        if (c == String.class) return Optional.of((stmt, index, value) -> stmt.setString(index, (String) value));
        if (c == UUID.class) return Optional.of((stmt, index, value) -> stmt.setString(index, value.toString()));
        if (c == int.class || c == Integer.class)
            return Optional.of((stmt, index, value) -> stmt.setInt(index, (int) value));
        if (c == double.class || c == Double.class)
            return Optional.of((stmt, index, value) -> stmt.setDouble(index, (double) value));
        if (c == float.class || c == Float.class)
            return Optional.of((stmt, index, value) -> stmt.setFloat(index, (float) value));
        if (c == short.class || c == Short.class)
            return Optional.of((stmt, index, value) -> stmt.setShort(index, (short) value));
        if (c == byte.class || c == Byte.class)
            return Optional.of((stmt, index, value) -> stmt.setByte(index, (byte) value));
        if (c == long.class || c == Long.class)
            return Optional.of((stmt, index, value) -> stmt.setLong(index, (long) value));
        if (TableRowTypeAdapter.class.isAssignableFrom(c)) return Optional.of((stmt, index, value) -> {
            TableRowTypeAdapter adapter = (TableRowTypeAdapter) value;
            stmt.setString(index, adapter.adapt());
        });
        if (c.isEnum()) return Optional.of((stmt, index, value) -> stmt.setString(index, value.toString()));
        if (implementors.containsKey(c)) return Optional.of((SetterAction<Object>) implementors.get(c).setter());
        return Optional.empty();
    }


    @SuppressWarnings("unchecked")
    <C> Optional<GetterAction<C>> findGetter(Class<C> c) {
        if (c == String.class) return Optional.of((s, columnName) -> (C) s.getString(columnName));
        if (c == UUID.class) return Optional.of((s, columnName) -> (C) UUID.fromString(s.getString(columnName)));
        if (c == int.class || c == Integer.class)
            return Optional.of((s, columnName) -> (C) (Integer) s.getInt(columnName));
        if (c == double.class || c == Double.class)
            return Optional.of((s, columnName) -> (C) (Double) s.getDouble(columnName));
        if (c == float.class || c == Float.class)
            return Optional.of((s, columnName) -> (C) (Float) s.getFloat(columnName));
        if (c == short.class || c == Short.class)
            return Optional.of((s, columnName) -> (C) (Short) s.getShort(columnName));
        if (c == byte.class || c == Byte.class) return Optional.of((s, columnName) -> (C) (Byte) s.getByte(columnName));
        if (c == long.class || c == Long.class) return Optional.of((s, columnName) -> (C) (Long) s.getLong(columnName));
        if (TableRowTypeAdapter.class.isAssignableFrom(c)) return Optional.of((s, columnName) -> {
            try {
                String string = s.getString(columnName);
                Method method = c.getDeclaredMethod("readAdapted", String.class);
                if (!Modifier.isStatic(method.getModifiers())) throw new IllegalStateException("");
                method.setAccessible(true);
                Object value = method.invoke(null, string);
                if (value == null) return null;
                else return (C) value;
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(c.getName() + " must have a static method readAdapted(String) that returns " + c.getName());
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException ignored) { /*literally what the fuck this is impossible*/ }
            return null;
        });
        if (c.isEnum()) return Optional.of((s, columnName) -> {
            try {
                Field field = c.getField(s.getString(columnName));
                return (C) field.get(null);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
        if (implementors.containsKey(c)) return Optional.of((GetterAction<C>) implementors.get(c).getter());
        return Optional.empty();
    }

    /**
     * Deletes a row from the database, executing one statement in the process.
     *
     * @param row Row to delete.
     * @apiNote <strong>DO NOT USE.</strong> Use {@link TableRow#delete()} instead.
     */
    void delete(T row) {
        Optional<SetterAction<Object>> setter = findSetter(primaryKey.getType());
        if (setter.isEmpty()) throw new NoSetterException(primaryKey);

        primaryKey.setAccessible(true);
        new UpdateActionThread(database.getConnection(), "DELETE FROM " + name + " WHERE " + primaryKey.getName() + "= ?;", stmt -> {
            try {
                setter.get().set(stmt, 1, primaryKey.get(row));
            } catch (Exception ignored) {
            }
            return stmt;
        }).start();
    }

    /**
     * Executes a specific query on the table and returns the results as a {@link QueryResult<T>}. An empty query can be
     * used to retrieve all rows.
     *
     * @param query A {@link Query} to execute.
     * @return A {@link QueryResult} that contains either an error or a list of {@link T}s.
     * @apiNote Does not use threads, might be slow.
     */
    public QueryResult<T> query(Query query) {
        try (PreparedStatement stmt = database.getConnection().prepareStatement(query.toSqlCode(name))) {
            ResultSet set = stmt.executeQuery();
            ArrayList<T> ts = new ArrayList<>();

            while (set.next()) ts.add(getFromRow(set));
            return new QueryResult<>(null, ts);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(clazz.getName() + " must have constructor " + clazz.getSimpleName() + "(Class,Table)");
        } catch (InstantiationException e) {
            throw new IllegalStateException(clazz.getName() + " cannot be instantiated because it is abstract");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            return new QueryResult<>(e, null);
        } catch (IllegalAccessException ignored) {
            return new QueryResult<>(null, null);
        }
    }

}
