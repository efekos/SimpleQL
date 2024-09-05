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
import dev.efekos.simple_ql.exception.TableException;
import dev.efekos.simple_ql.implementor.Implementor;
import dev.efekos.simple_ql.query.Query;
import dev.efekos.simple_ql.query.QueryResult;
import dev.efekos.simple_ql.thread.UpdateActionThread;

import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class Table<T extends TableRow<T>> {

    private final Database database;
    private final String name;
    private final Class<T> clazz;
    private Field primaryKey = null;
    private final Map<Class<?>,Implementor<?,?>> implementors = new HashMap<>();

    public void putImplementor(Class<?> clazz, Implementor<?,?> implementor) {
        implementors.put(clazz,implementor);
    }

    Table(Database database, String name, Class<T> clazz) {
        this.database = database;
        this.name = name;
        this.clazz = clazz;

        for (Field field : clazz.getDeclaredFields())
            if (field.isAnnotationPresent(Primary.class)) {
                primaryKey = field;
                break;
            }
        if (primaryKey == null)
            throw new IllegalArgumentException("At least one primary key is required for " + clazz.getName());
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
        throw new IllegalStateException("Could not determine a column type for field " + field);
    }

    void checkExistent() {
        try (PreparedStatement stmt = database.getConnection().prepareStatement(createGenerationCode())) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TableException("Could not create table '" + name + "': " + e.getMessage());
        }
    }

    void clean(T row) {
        if (!row.isDirty()) return;

        for (Field field : row.getClazz().getDeclaredFields()) {
            if (!row.isDirty(field.getName())) continue;
            field.setAccessible(true);
            row.getPrimaryField().setAccessible(true);
            Optional<SetterAction<Object>> setter = findSetter(field.getType());
            new UpdateActionThread(database.getConnection(), "UPDATE " + name + " SET " + field.getName() + "=? WHERE " + primaryKey.getName() + "= ?;", stmt -> {
                if (setter.isPresent()) setter.get().set(stmt, 1, field.get(row));
                setField(row, row.getPrimaryField(), stmt, 2);
                return stmt;
            }).start();

        }
    }

    public T insertRow(Consumer<T> propertyChanger) {
        try {
            Constructor<T> constructor = clazz.getConstructor(Class.class, Table.class);
            constructor.setAccessible(true);
            T instance = constructor.newInstance(clazz, this);
            propertyChanger.accept(instance);
            instance.cleanWithoutUpdate();
            try (PreparedStatement stmt = database.getConnection().prepareStatement(createInsertionCode())) {

                Field[] fields = clazz.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAnnotationPresent(AutoIncrement.class)) continue;
                    field.setAccessible(true);
                    Object value = field.get(instance);
                    Optional<SetterAction<Object>> setter = findSetter(value.getClass());
                    if (setter.isEmpty()) throw new NoSetterException(field);
                    setter.get().set(stmt, i + 1, value);
                }

                stmt.executeUpdate();
                return instance;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            field.set(instance, o);
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
            return Optional.of((stmt, index, value) -> stmt.setByte(index, (byte) value));
        if (TableRowTypeAdapter.class.isAssignableFrom(c)) return Optional.of((stmt, index, value) -> {
            TableRowTypeAdapter adapter = (TableRowTypeAdapter) value;
            stmt.setString(index, adapter.adapt());
        });
        if (c.isEnum()) return Optional.of((stmt, index, value) -> stmt.setString(index, value.toString()));
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
        return Optional.empty();
    }

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
