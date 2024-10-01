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

/**
 * Represents a row of a {@link Table} that has the same {@code T} type with {@link T}. Used to change the values of a
 * row, delete the row or update it.
 * @since 1.0
 * @param <T> Type that extends this class.
 */
public abstract class TableRow<T extends TableRow<T>> {

    private final Class<T> clazz;
    private final List<String> dirtyFields = new ArrayList<>();
    private final Table<T> parentTable;
    private boolean deleted;

    /**
     * Creates a new instance.
     * @param clazz An instance of {@link T}'s class.
     * @param parentTable Parent {@link Table} that generated this row.
     */
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

    /**
     * Returns {@code true} if this row has fields that should be synced with the database.
     * @return Whether if this row has any dirty fields or not.
     */
    public boolean isDirty() {
        return !dirtyFields.isEmpty();
    }

    /**
     * Returns {@code true} if the field with the given name should be synced with the database.
     * @param fieldName Name of the field/column.
     * @return Whether the field with the name {@code fieldName} is dirty or not.
     */
    boolean isDirty(String fieldName) {
        return dirtyFields.contains(fieldName);
    }

    /**
     * Marks all fields of this row dirty, so calling {@link #clean()} will sync all of them with the database no matter
     * if they actually changed or not.
     */
    public void markDirty() {
        for (Field field : clazz.getDeclaredFields()) markDirty(field.getName());
    }

    /**
     * Marks a specific field dirty. Should be used by EVERY setter in order for a {@link TableRow} subclass to work.
     * @param name Name of the field/column.
     */
    protected void markDirty(String name) {
        dirtyFields.add(name);
    }

    /**
     * Syncs all dirty fields with the database, then resets their status.
     */
    @SuppressWarnings("unchecked")
    public void clean() {
        parentTable.clean((T) this);
        dirtyFields.clear();
    }

    void cleanWithoutUpdate() {
        dirtyFields.clear();
    }

    /**
     * Deletes this row from the database. Can only be done once every instance
     * @throws IllegalStateException if this row has been already deleted..
     */
    @SuppressWarnings("unchecked")
    public void delete() {
        if (deleted) throw new IllegalStateException("This row is already deleted.");
        parentTable.delete((T) this);
        deleted = true;
    }

}
