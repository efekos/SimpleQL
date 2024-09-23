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

package dev.efekos.simple_ql.exception;

import java.lang.reflect.Field;

/**
 * An exception thrown by SimpleQL when initializing a table fails because a {@link dev.efekos.simple_ql.data.GetterAction}
 * suitable for a field could not be found.
 *
 * @since 1.0
 */
public class NoGetterException extends RuntimeException {

    /**
     * Creates a new NoGetterException.
     *
     * @param field A field that has no {@link dev.efekos.simple_ql.data.GetterAction}s added for it.
     */
    public NoGetterException(Field field) {
        super("No getter found for field " + field.toString() + ". Please only use primitive types, String, UUID and subtypes of TableRowTypeAdapter");
    }

}
