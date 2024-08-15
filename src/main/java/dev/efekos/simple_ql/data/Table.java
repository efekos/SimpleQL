package dev.efekos.simple_ql.data;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Optional;

public class Table<T extends TableRow<T>> {

    private final Database database;
    private final String name;
    private final Class<T> clazz;

    Table(Database database,String name,Class<T> clazz) {
        this.database = database;
        this.name = name;
        this.clazz = clazz;
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
