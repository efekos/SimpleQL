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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a built SQL query, ready to be executed.
 * @since 1.0
 */
public class Query {

    private List<Sort> sorts = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();
    private int limit = 0;
    private int skip = 0;

    /**
     * Constructs a built query.
     * @param sorts A list of {@link Sort}s.
     * @param conditions A list of {@link Condition}s.
     */
    public Query(List<Sort> sorts, List<Condition> conditions) {
        this.sorts = sorts;
        this.conditions = conditions;
    }

    /**
     * Constructs a new query.
     */
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

    /**
     * Returns the limit of the query.
     * @return Limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Changes the limit of the query. Setting a limit that isn't 0 will add {@code LIMIT X} to the created statement.
     * @param limit New limit.
     * @throws IllegalArgumentException if {@code limit} is a negative number.
     */
    public void setLimit(int limit) {
        if (limit <= 0) throw new IllegalArgumentException("Limit must be greater than 0");
        this.limit = limit;
    }

    /**
     * Adds a sort to the sort list, which will add that sort as a {@code SORT} statement.
     * @param sort A {@link Sort} instance.
     */
    public void addSort(Sort sort) {
        sorts.add(sort);
    }

    /**
     * Adds a condition to the condition list, which will add the condition as a {@code WHERE} statement.
     * @param condition A {@link Condition} instance.
     */
    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    /**
     * Returns the sort list.
     * @return Sort list.
     */
    public List<Sort> getSorts() {
        return sorts;
    }

    /**
     * Returns the condition list.
     * @return Condition list.
     */
    public List<Condition> getConditions() {
        return conditions;
    }

    /**
     * Returns the amount of rows this query will skip.
     * @return Skip count.
     */
    public int getSkip() {
        return skip;
    }

    /**
     * Changes the amount of rows this query should skip, which will add a {@code SKIP} statement to the query if skip
     * is not 0.
     * @param skip New skip count.
     */
    public void setSkip(int skip) {
        if (skip <= 0) throw new IllegalArgumentException("Skip must be greater than 0");
        this.skip = skip;
    }

    /**
     * Generates an SQL statement that this {@link Query} represents.
     * @param tableName Name of the table that is using this query.
     * @return Generated query.
     */
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