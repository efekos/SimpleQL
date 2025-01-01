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

import java.util.Objects;

/**
 * One of the builtin condition types to use in {@link Query}s. Checks for a number to be in a specific range where both
 * directions can go to infinity, which means it is possible to also create bigger than/smaller than conditions using
 * this condition type.
 * @since 1.0
 */
public class RangeCondition implements Condition {

    private final Integer start;
    private final Integer end;
    private final String fieldName;

    /**
     * Constructs a new {@link RangeCondition}.
     * @param start Start of the range. Can be {@code null} to imply infinity in the left direction.
     * @param end End of the range. Can be {@code null} to imply infinity in the right direction.
     * @param fieldName Name of the field/column that this condition will check on. Used for SQL code generation.
     * @throws IllegalArgumentException If both {@code start} and {@code end} are {@code null} or {@code start} is
     *                                  larger than {@code end}.
     */
    public RangeCondition(Integer start, Integer end, String fieldName) {
        this.start = start;
        this.end = end;
        this.fieldName = fieldName;
        if (start == null && end == null) throw new IllegalArgumentException("start and end must not be null");
        if ((start != null && end != null) && start > end) throw new IllegalArgumentException("start > end");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "RangeCondition{" +
                "start=" + start +
                ", end=" + end +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toSqlCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(fieldName);

        if (Objects.equals(start, end)) builder.append(" = ").append(start);
        else if (start != null && end != null) builder.append(" BETWEEN ").append(start).append(" AND ").append(end);
        else if (start != null) builder.append(" > ").append(start);
        else builder.append(" < ").append(end);
        return builder.toString();
    }

}
