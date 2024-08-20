package dev.efekos.simple_ql.query;

public class Conditions {

    public static StringMatchCondition matchText(String fieldName, String match) {
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

}
