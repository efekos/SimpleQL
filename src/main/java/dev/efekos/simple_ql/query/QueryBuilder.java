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
 * A class used to create a {@link Query} with ease. It is possible to construct a {@link Query} in one line using this
 * class, no matter how complicated the created {@link Query} is.
 * @since 1.0
 */
public class QueryBuilder {

    private final Query query;

    /**
     * Constructs a new builder.
     */
    public QueryBuilder() {
        this.query = new Query();
    }

    /**
     * Adds a sort that is by given field/column in ascending order.
     * @param fieldName Name of the field/column
     * @return {@code this}.
     */
    public QueryBuilder sortAscending(String fieldName) {
        query.addSort(new Sort(fieldName, true));
        return this;
    }

    /**
     * Adds a sort that is by given field/column in descending order.
     * @param fieldName Name of the field/column
     * @return {@code this}.
     */
    public QueryBuilder sortDescending(String fieldName) {
        query.addSort(new Sort(fieldName, false));
        return this;
    }

    /**
     * Adds given {@link Condition} to the final {@link Query}. Use {@link Conditions} or make a class similar to
     * {@link Conditions} if you have custom condition types.
     * @param condition A {@link Condition} instance, usually gathered using {@link Conditions}.
     * @return {@code this}.
     */
    public QueryBuilder filterWithCondition(Condition condition) {
        query.addCondition(condition);
        return this;
    }

    /**
     * Changes the limit of the final {@link Query}. Setting this to something more than {@code 0} will create a
     * {@code LIMIT} statement.
     * @param limit New limit of the final {@link Query}.
     * @return {@code this}.
     */
    public QueryBuilder limit(int limit) {
        query.setLimit(limit);
        return this;
    }


    /**
     * Changes the skip count of the final {@link Query}. Setting this to something more than {@code 0} will create a
     * {@code SKIP} statement.
     * @param skip New skip count of the final {@link Query}.
     * @return {@code this}.
     */
    public QueryBuilder skip(int skip) {
        query.setSkip(skip);
        return this;
    }

    /**
     * Returns the final {@link Query}.
     * @return Final {@link Query}.
     */
    public Query getQuery() {
        return query;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "QueryBuilder{" +
                "query=" + query +
                '}';
    }

}
