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

public class QueryBuilder {

    private final Query query;

    public QueryBuilder() {
        this.query = new Query();
    }

    public QueryBuilder sortAscending(String fieldName) {
        query.addSort(new Sort(fieldName, true));
        return this;
    }

    public QueryBuilder sortDescending(String fieldName) {
        query.addSort(new Sort(fieldName, false));
        return this;
    }

    public QueryBuilder filterWithCondition(Condition condition) {
        query.addCondition(condition);
        return this;
    }

    public QueryBuilder limit(int limit) {
        query.setLimit(limit);
        return this;
    }

    public QueryBuilder skip(int skip) {
        query.setSkip(skip);
        return this;
    }

    public Query getQuery() {
        return query;
    }

}
