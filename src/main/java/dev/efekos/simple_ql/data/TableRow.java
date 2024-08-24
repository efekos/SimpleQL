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

import dev.efekos.simple_ql.annotation.Primary;
import dev.efekos.simple_ql.exception.NoPrimaryKeyException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class TableRow<T extends TableRow<T>> {

    private final Class<T> clazz;
    private final List<String> dirtyFields = new ArrayList<>();
    private final Table<T> parentTable;
    private boolean deleted;

    public TableRow(Class<T> clazz, Table<T> parentTable) {
        this.clazz = clazz;
        this.parentTable = parentTable;
    }

    Field getPrimaryField() {
        for (Field field : clazz.getDeclaredFields()) if (field.isAnnotationPresent(Primary.class)) return field;
        throw new NoPrimaryKeyException(clazz.getName() + " Does not have any fields annotated with " + Primary.class.getName());
    }


    Class<T> getClazz() {
        return clazz;
    }

    public boolean isDirty() {
        return !dirtyFields.isEmpty();
    }

    boolean isDirty(String fieldName) {
        return dirtyFields.contains(fieldName);
    }

    public void markDirty() {
        for (Field field : clazz.getDeclaredFields()) markDirty(field.getName());
    }

    protected void markDirty(String name) {
        dirtyFields.add(name);
    }

    @SuppressWarnings("unchecked")
    public void clean() {
        parentTable.clean((T) this);
        dirtyFields.clear();
    }

    void cleanWithoutUpdate() {
        dirtyFields.clear();
    }

    @SuppressWarnings("unchecked")
    public void delete() {
        if (deleted) throw new IllegalStateException("This row is already deleted.");
        parentTable.delete((T) this);
        deleted = true;
    }

}
