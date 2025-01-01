/*
 * MIT License
 *
 * Copyright (c) 2025 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.efekos.simple_ql.query;

/**
 * One of the builtin conditions types to use in {@link Query}s. Checks if a {@code VARCHAR}/{@code TEXT} field contains
 * a {@link String}.
 * @since 1.0
 */
public class StringContainsCondition implements Condition {

    private final String fieldName;
    private final String value;

    /**
     * Constructs a new {@link StringContainsCondition}.
     * @param fieldName Name of the field/column. Used for SQL code generation.
     * @param value {@link String} value.
     */
    public StringContainsCondition(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toSqlCode() {
        return fieldName + " LIKE '%" + value + "%'";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "StringContainsCondition{" +
                "fieldName='" + fieldName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
