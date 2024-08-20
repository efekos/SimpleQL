package dev.efekos.simple_ql.query;

public class StringRegexCondition implements Condition {

    private final String fieldName;
    private final String value;

    public StringRegexCondition(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public String toSqlCode() {
        return fieldName + " REGEXP '" + value + "'";
    }

}
