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

import java.util.List;

/**
 * A condition type used to merge multiple conditions under the SQL {@code OR} statement.
 * @since 1.0
 */
public class AnyOfCondition implements Condition {

    private final List<Condition> conditions;

    /**
     * Creates a new instance using the list of conditions.
     * @param conditions A list of conditions.
     */
    public AnyOfCondition(List<Condition> conditions) {
        this.conditions = conditions;
    }

    /**
     * Creates a new instance using the array of conditions.
     * @param conditions An array of conditions.
     */
    public AnyOfCondition(Condition... conditions) {
        this.conditions = List.of(conditions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toSqlCode() {
        StringBuilder builder = new StringBuilder().append("(");

        for (int i = 0; i < conditions.size(); i++) {
            String sql = conditions.get(i).toSqlCode();
            builder.append(sql);
            if (i != conditions.size() - 1) builder.append(" OR ");
        }

        return builder.append(")").toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AnyOfCondition{" +
                "conditions=" + conditions +
                '}';
    }

}
