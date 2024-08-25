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

import java.util.ArrayList;
import java.util.List;

public class Query {

    private List<Sort> sorts = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();
    private int limit = 0;
    private int skip = 0;

    public Query(List<Sort> sorts, List<Condition> conditions) {
        this.sorts = sorts;
        this.conditions = conditions;
    }

    public Query() {
    }

    @Override
    public String toString() {
        return "Query{" +
                "sorts=" + sorts +
                ", conditions=" + conditions +
                ", limit=" + limit +
                ", skip=" + skip +
                '}';
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit <= 0) throw new IllegalArgumentException("Limit must be greater than 0");
        this.limit = limit;
    }

    public void addSort(Sort sort) {
        sorts.add(sort);
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String toSqlCode(String tableName) {
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT * FROM ");
        builder.append(tableName);

        if (!conditions.isEmpty()) builder.append(" WHERE ");
        for (int i = 0; i < getConditions().size(); i++) {
            Condition condition = getConditions().get(i);
            if (i != 0) builder.append(" AND ");
            builder.append("(");
            builder.append(condition.toSqlCode());
            builder.append(")");
        }

        for (Sort sort : getSorts()) {
            builder.append(" ORDER BY ");
            builder.append(sort.fieldName());
            if (sort.ascending()) builder.append(" ASC");
            else builder.append(" DESC");
        }


        if (limit != 0) {
            builder.append(" LIMIT ");
            builder.append(limit);
        }
        if (skip != 0) {
            builder.append(" OFFSET ");
            builder.append(skip);
        }
        builder.append(";");
        return builder.toString();
    }

}