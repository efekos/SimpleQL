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

import java.util.Objects;

public class RangeCondition implements Condition {

    private final Integer start;
    private final Integer end;
    private final String fieldName;

    public RangeCondition(Integer start, Integer end, String fieldName) {
        this.start = start;
        this.end = end;
        this.fieldName = fieldName;
        if (start == null && end == null) throw new IllegalArgumentException("start and end must not be null");
        if ((start != null && end != null) && start > end) throw new IllegalArgumentException("start > end");
    }

    @Override
    public String toString() {
        return "RangeCondition{" +
                "start=" + start +
                ", end=" + end +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }

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
