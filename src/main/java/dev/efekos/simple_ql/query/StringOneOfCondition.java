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

import java.util.List;

/**
 * A condition type used for {@link String} fields / {@code TEXT} and {@code VARCHAR} columns. Checks if an array of
 * {@link String}s contain the value of a column using the SQL {@code IN} statement.
 * @since 1.0
 */
public class StringOneOfCondition implements Condition {

    private final String fieldName;
    private final List<String> value;

    /**
     * Creates a new instance.
     * @param fieldName Name of the field/column.
     * @param value An array of values.
     */
    public StringOneOfCondition(String fieldName, String... value) {
        this.fieldName = fieldName;
        this.value = List.of(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toSqlCode() {
        return fieldName + " IN (" + String.join(",", value.stream().map(s -> "'" + s + "'").toList()) + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "StringOneOfCondition{" +
                "fieldName='" + fieldName + '\'' +
                ", value=" + value +
                '}';
    }

}
