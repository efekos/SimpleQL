package dev.efekos.simple_ql.data;

import java.lang.reflect.Field;
import java.util.*;

public abstract class TableRow<T extends TableRow<T>> {

    private final Class<T> clazz;
    private final List<String> dirtyFields = new ArrayList<>();
    private final Table<T> parentTable;

    TableRow(Class<T> clazz, Table<T> parentTable) {
        this.clazz = clazz;
        this.parentTable = parentTable;
    }

    Class<T> getClazz() {
        return clazz;
    }

    public boolean isDirty(){
        return !dirtyFields.isEmpty();
    }

    boolean isDirty(String fieldName){
        return dirtyFields.contains(fieldName);
    }

    public void markDirty(){
        for (Field field : clazz.getDeclaredFields()) markDirty(field.getName());
    }

    private void markDirty(String name) {
        dirtyFields.add(name);
    }

    void clean(){
        parentTable.clean(this);
        dirtyFields.clear();
    }

    <TY> Optional<SetterAction<TY>> findSetter(Class<?> c){
        if(c.isAssignableFrom(String.class)) return Optional.of((stmt, index, value) -> stmt.setString(index,(String) value));
        if(c.isAssignableFrom(UUID.class)) return Optional.of((stmt, index, value) -> stmt.setString(index, value.toString()));
        if(c.isAssignableFrom(int.class)||c.isAssignableFrom(Integer.class)) return Optional.of((stmt, index, value) -> stmt.setInt(index,(int) value));
        if(c.isAssignableFrom(double.class)||c.isAssignableFrom(Double.class)) return Optional.of((stmt, index, value) -> stmt.setDouble(index,(double) value));
        if(c.isAssignableFrom(float.class)||c.isAssignableFrom(Float.class)) return Optional.of((stmt, index, value) -> stmt.setFloat(index,(float) value));
        if(c.isAssignableFrom(short.class)||c.isAssignableFrom(Short.class)) return Optional.of((stmt, index, value) -> stmt.setShort(index,(short) value));
        if(c.isAssignableFrom(byte.class)||c.isAssignableFrom(Byte.class)) return Optional.of((stmt, index, value) -> stmt.setByte(index,(byte) value));
        if(c.isAssignableFrom(long.class)||c.isAssignableFrom(Long.class)) return Optional.of((stmt, index, value) -> stmt.setByte(index,(byte) value));
        if(c.isAssignableFrom(TableRowTypeAdapter.class)) return Optional.of((stmt, index, value) -> {
            TableRowTypeAdapter adapter = (TableRowTypeAdapter) value;
            stmt.setString(index,adapter.adapt());
        });
        return Optional.empty();
    }


}
