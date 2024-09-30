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

package dev.efekos.simple_ql.query;

/**
 * A condition type made specifically for boolean columns. Used to check if a column is a specified boolean value.
 * @since 1.0
 */
public class BooleanMatchCondition implements Condition {

    private final String fieldName;
    private final boolean value;

    /**
     * Creates a new instance.
     * @param fieldName Name of the field/column.
     * @param value Boolean match value.
     */
    public BooleanMatchCondition(String fieldName, boolean value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toSqlCode() {
        return fieldName + " = " + (value ? '1' : '0');
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BooleanMatchCondition{" +
                "fieldName='" + fieldName + '\'' +
                ", value=" + value +
                '}';
    }

}
