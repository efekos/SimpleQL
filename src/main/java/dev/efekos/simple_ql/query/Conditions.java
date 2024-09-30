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
 * A utility class used to quickly create condition classes.
 * @since 1.0
 */
public class Conditions {

    /**
     * Creates a new {@link StringMatchCondition} that will match rows where the value of {@code fieldName} is exactly
     * {@code match}.
     * @param fieldName Name of the field/column.
     * @param match String match value.
     * @return A {@link StringMatchCondition} instance.
     */
    public static StringMatchCondition matchTextExact(String fieldName, String match) {
        return new StringMatchCondition(fieldName, match);
    }

    /**
     * Creates a new {@link RangeCondition} that will match rows where the value of {@code fieldName} is exactly
     * {@code match}.
     * @param fieldName Name of the field/column
     * @param number Number match value.
     * @return A {@link RangeCondition} where both minimum and maximum value is the same value to match an exact number.
     */
    public static RangeCondition exactNumber(String fieldName, int number) {
        return new RangeCondition(number, number, fieldName);
    }

    /**
     * Creates a new {@link RangeCondition} that will match rows where the value of {@code fieldName} is not equal to
     * and less than {@code number}.
     * @param fieldName Name of the field/column.
     * @param number Number value.
     * @return A {@link RangeCondition} without a minimum value.
     */
    public static RangeCondition lessThan(String fieldName, int number) {
        return new RangeCondition(null, number, fieldName);
    }

    /**
     * Creates a new {@link RangeCondition} that will match rows where the value of {@code fieldName} is not equal to
     * and more than {@code n}.
     * @param fieldName Name of the field/column.
     * @param n Number value.
     * @return A {@link RangeCondition} without a maximum value.
     */
    public static RangeCondition greaterThan(String fieldName, int n) {
        return new RangeCondition(n, null, fieldName);
    }

    /**
     * Creates a new {@link BooleanMatchCondition} with the value {@code true} so it matches rows where {@code fieldName}
     * is {@code true} or {@code 1}.
     * @param fieldName Name of the field/column.
     * @return A {@link BooleanMatchCondition} with the value {@code true}.
     */
    public static BooleanMatchCondition matchTrue(String fieldName) {
        return new BooleanMatchCondition(fieldName, true);
    }


    /**
     * Creates a new {@link BooleanMatchCondition} with the value {@code false} so it matches rows where {@code fieldName}
     * is {@code false} or {@code 0}.
     * @param fieldName Name of the field/column.
     * @return A {@link BooleanMatchCondition} with the value {@code false}.
     */
    public static BooleanMatchCondition matchFalse(String fieldName) {
        return new BooleanMatchCondition(fieldName, false);
    }


    /**
     * Creates a new {@link BooleanMatchCondition} with the value of {@code value} so it matches rows where
     * {@code fieldName} is the same value as {@code value} or it's number representation ({@code 0} for {@code false} and
     * {@code 1} for {@code true}).
     * @param fieldName Name of the field/column.
     * @return A {@link BooleanMatchCondition} with the value {@code true}.
     */
    public static BooleanMatchCondition matchBool(String fieldName, boolean value) {
        return new BooleanMatchCondition(fieldName, value);
    }

    /**
     * Creates a new {@link StringEndsWithCondition} that will match rows where the value of {@code fieldName} is either
     * a {@code TEXT} or a {@code VARCHAR} and it ends with {@code match}.
     * @param fieldName Name of the field/column.
     * @param match String match value.
     * @return A {@link StringEndsWithCondition} instance.
     */
    public static StringEndsWithCondition matchTextEndingWith(String fieldName, String match) {
        return new StringEndsWithCondition(fieldName, match);
    }


    /**
     * Creates a new {@link StringStartsWithCondition} that will match rows where the value of {@code fieldName} is either
     * a {@code TEXT} or a {@code VARCHAR} and it starts with {@code match}.
     * @param fieldName Name of the field/column.
     * @param match String match value.
     * @return A {@link StringStartsWithCondition} instance.
     */
    public static StringStartsWithCondition matchTextStartingWith(String fieldName, String match) {
        return new StringStartsWithCondition(fieldName, match);
    }

    /**
     * Creates a new {@link StringContainsCondition} that will match rows where the value of {@code fieldName} is either
     * a {@code TEXT} or a {@code VARCHAR} and it contains {@code match}.
     * @param fieldName Name of the field/column.
     * @param match String match value.
     * @return A {@link StringContainsCondition} instance.
     */
    public static StringContainsCondition matchTextContains(String fieldName, String match) {
        return new StringContainsCondition(fieldName, match);
    }

    /**
     * Creates a new {@link StringRegexCondition} that will match rows where the value of {@code fieldName} is either a
     * {@code TEXT} or a {@code VARCHAR} and it matches the given regular expression.
     * @param fieldName Name of the field/column.
     * @param regex Regular expression to match.
     * @return A {@link StringRegexCondition} instance with the value {@code regex}.
     */
    public static StringRegexCondition matchTextRegex(String fieldName, String regex) {
        return new StringRegexCondition(fieldName, regex);
    }

    /**
     * Creates a new {@link AnyOfCondition} that will merge multiple conditions using the SQL {@code OR} operator.
     * @param conditions A list of {@link Condition}s.
     * @return A {@link AnyOfCondition} instance.
     */
    public static AnyOfCondition anyOf(Condition... conditions) {
        return new AnyOfCondition(conditions);
    }

    /**
     * Creates a new {@link StringOneOfCondition} that will match rows where the array of strings given contains the
     * value of {@code fieldName}.
     * @param fieldName Name of the field/column.
     * @param matches An array of matches.
     * @return A {@link StringOneOfCondition} instance.
     */
    public static StringOneOfCondition matchOneOf(String fieldName, String... matches) {
        return new StringOneOfCondition(fieldName, matches);
    }

}
