package dev.efekos.simple_ql.data;

import dev.efekos.simple_ql.annotation.AutoIncrement;
import dev.efekos.simple_ql.annotation.Primary;
import dev.efekos.simple_ql.annotation.Type;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class Table<T extends TableRow<T>> {

    private final Database database;
    private final String name;
    private final Class<T> clazz;

    Table(Database database,String name,Class<T> clazz) {
        this.database = database;
        this.name = name;
        this.clazz = clazz;
    }

    private String createGenerationCode(){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS");
        builder.append(name);
        builder.append(" (");
        for (Field field : clazz.getDeclaredFields()) {
            String columnName = field.getName();
            boolean primary = field.isAnnotationPresent(Primary.class);
            boolean autoIncrement = field.isAnnotationPresent(AutoIncrement.class);

            builder.append(columnName);
            builder.append(" ");
            builder.append(findType(field));
            if (primary) builder.append(" PRIMARY KEY UNIQUE");
            if (autoIncrement) builder.append(" AUTO_INCREMENT");
        }
        return builder.append(")").toString();
    }

    private String findType(Field field) {
        if(field.isAnnotationPresent(Type.class)) return field.getAnnotation(Type.class).value();
        Class<?> type = field.getType();
        if(type.isAssignableFrom(boolean.class)||type.isAssignableFrom(int.class)) return "INT";
        if(type.isAssignableFrom(double.class)||type.isAssignableFrom(float.class)) return "REAL";
        if(type.isAssignableFrom(UUID.class)) return "VARCHAR(36)";
        if(type.isAssignableFrom(String.class)) return "TEXT";
        throw new IllegalStateException("Could determine a column type for field "+field);
    }

    void checkExistent(){
        try(PreparedStatement stmt = database.getConnection().prepareStatement(createGenerationCode())) {
            stmt.executeUpdate();
        } catch (SQLException ignored){}
    }

    void clean(TableRow<T> row){
        if(!row.isDirty()) return;

        for (Field field : row.getClazz().getDeclaredFields()) {
            if(!row.isDirty(field.getName()))continue;
            field.setAccessible(true);
            row.getPrimaryField().setAccessible(true);
            try(PreparedStatement stmt = database.getConnection().prepareStatement("UPDATE "+name+" SET "+field.getName()+"=? WHERE "+field.getName()+"=?")) {
                Optional<SetterAction<Object>> setter = row.findSetter(field.getType());
                if(setter.isPresent()) setter.get().set(stmt,1,field.get(row));
                row.setField(row.getPrimaryField(),stmt,2);
                stmt.executeUpdate();
            } catch (Exception ignored){}
        }
    }

}
