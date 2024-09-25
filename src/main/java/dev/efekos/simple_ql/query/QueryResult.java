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
 * Represents the result of an executed {@link Query}.
 * @param exception Resulted exception if any occurred.
 * @param result A list of rows if the query was successfully executed.
 * @param <T> Type of the {@link dev.efekos.simple_ql.data.TableRow}s of the {@link dev.efekos.simple_ql.data.Table}
 *            the {@link Query} was executed on.
 * @since 1.0
 */
public record QueryResult<T>(Exception exception, List<T> result) {

    /**
     * Returns whether {@link #result} is {@code null} or not.
     * @return Whether {@link #result} is {@code null} or not.
     */
    public boolean hasResult() {
        return result != null;
    }

    /**
     * Returns whether {@link #exception} is {@code null} or not.
     * @return Whether {@link #exception} is {@code null} or not.
     */
    public boolean hasException() {
        return exception != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "QueryResult{" +
                "exception=" + exception +
                ", result=" + result +
                '}';
    }

}
