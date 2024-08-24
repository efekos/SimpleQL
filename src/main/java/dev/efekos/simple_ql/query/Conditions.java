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

public class Conditions {

    public static StringMatchCondition matchTextExact(String fieldName, String match) {
        return new StringMatchCondition(fieldName, match);
    }

    public static RangeCondition exactNumber(String fieldName, int number) {
        return new RangeCondition(number, number, fieldName);
    }

    public static RangeCondition lessThan(String fieldName, int number) {
        return new RangeCondition(null, number, fieldName);
    }

    public static RangeCondition greaterThan(String fieldName, int n) {
        return new RangeCondition(n, null, fieldName);
    }

    public static BooleanMatchCondition matchTrue(String fieldName) {
        return new BooleanMatchCondition(fieldName, true);
    }

    public static BooleanMatchCondition matchFalse(String fieldName) {
        return new BooleanMatchCondition(fieldName, false);
    }

    public static BooleanMatchCondition matchBool(String fieldName, boolean value) {
        return new BooleanMatchCondition(fieldName, value);
    }

    public static StringEndsWithCondition matchTextEndingWith(String fieldName, String match) {
        return new StringEndsWithCondition(fieldName, match);
    }

    public static StringStartsWithCondition matchTextStartingWith(String fieldName, String match) {
        return new StringStartsWithCondition(fieldName, match);
    }

    public static StringContainsCondition matchTextContains(String fieldName, String match) {
        return new StringContainsCondition(fieldName, match);
    }

    public static StringRegexCondition matchTextRegex(String fieldName, String match) {
        return new StringRegexCondition(fieldName, match);
    }

    public static AnyOfCondition anyOf(Condition... conditions) {
        return new AnyOfCondition(conditions);
    }

    public static StringOneOfCondition matchOneOf(String fieldName, String... matches) {
        return new StringOneOfCondition(fieldName, matches);
    }

}
