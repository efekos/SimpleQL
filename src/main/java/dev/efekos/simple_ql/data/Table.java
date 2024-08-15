package dev.efekos.simple_ql.data;

import java.lang.reflect.Field;

public class Table<T extends TableRow<T>> {

    void clean(TableRow<T> row){
        if(!row.isDirty()) return;

        for (Field field : row.getClazz().getDeclaredFields()) {
            if(!row.isDirty(field.getName()))continue;
            field.setAccessible(true);

        }
    }

}
