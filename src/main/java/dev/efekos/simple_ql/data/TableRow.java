package dev.efekos.simple_ql.data;

import dev.efekos.simple_ql.annotation.Primary;
import dev.efekos.simple_ql.exception.NoPrimaryKeyException;
import dev.efekos.simple_ql.exception.NoSetterException;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public abstract class TableRow<T extends TableRow<T>> {

    private final Class<T> clazz;
    private final List<String> dirtyFields = new ArrayList<>();
    private final Table<T> parentTable;

    public TableRow(Class<T> clazz, Table<T> parentTable) {
        this.clazz = clazz;
        this.parentTable = parentTable;
    }

    Field getPrimaryField(){
        for (Field field : clazz.getDeclaredFields()) if(field.isAnnotationPresent(Primary.class))return field;
        throw new NoPrimaryKeyException(clazz.getName()+" Does not have any fields annotated with "+Primary.class.getName());
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

    protected void markDirty(String name) {
        dirtyFields.add(name);
    }

    public void clean(){
        parentTable.clean((T)this);
        dirtyFields.clear();
    }

    void cleanWithoutUpdate(){
        dirtyFields.clear();
    }



}
