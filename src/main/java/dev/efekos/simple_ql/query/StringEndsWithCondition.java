package dev.efekos.simple_ql.query;

public class StringEndsWithCondition implements Condition {

    private final String fieldName;
    private final String value;

    public StringEndsWithCondition(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public String toSqlCode() {
        return fieldName + " LIKE '%" + value + "'";
    }

}
