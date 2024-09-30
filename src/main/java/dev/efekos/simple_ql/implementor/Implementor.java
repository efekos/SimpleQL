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

package dev.efekos.simple_ql.implementor;

import dev.efekos.simple_ql.data.GetterAction;
import dev.efekos.simple_ql.data.SetterAction;

/**
 * Represents a class used to handle the process of supporting a custom type, mainly used for
 * {@link dev.efekos.simple_ql.data.Database#registerTable(String, Class, Implementor[])} or
 * {@link dev.efekos.simple_ql.data.AdaptedList}s. Works as a serializer between the types {@link T} and {@link V}.
 * {@link V} will usually be {@link String} as it is one of the most expensive column types in SQL and probably what
 * should be used for a java object.
 * @param <T> Type of the class that this {@link Implementor} will be for.
 * @param <V> Type of the value this {@link Implementor} serializes {@link T} into. Usually {@link String} when used for
 *            SimpleQL.
 */
public interface Implementor<T, V> {

    /**
     * Serializes given {@link T} instance into a {@link V} instance.
     * @param value A {@link T} instance.
     * @return A {@link V} instance that represents the given {@link T} instance.
     */
    V write(T value);

    /**
     * Deserializes given {@link V} instance into a {@link T} instance.
     * @param value A {@link V} instance.
     * @return A {@link T} instance that represents the given {@link V} instance.
     */
    T read(V value);

    /**
     * Returns a method to put a {@link V} instance into a {@link java.sql.PreparedStatement}. Will usually return
     * {@link java.sql.PreparedStatement#setString(int, String)} as {@link V} is usually {@link String} when using
     * {@link Implementor}s with their intended purpose.
     * @return A {@link SetterAction} for {@link V}.
     */
    SetterAction<V> setter();

    /**
     * Returns a method to get a {@link V} instance from a {@link java.sql.ResultSet}. Will usually return
     * {@link java.sql.ResultSet#getString(int)} as {@link V} is usually {@link String} when using {@link Implementor}s
     * with their intended purpose.
     * @return A {@link GetterAction} for {@link V}.
     */
    GetterAction<V> getter();

    /**
     * Returns the SQL column type this {@link Implementor} should use.
     * @return An SQL column type.
     */
    String type();

}