package dev.efekos.simple_ql.query;

public class StringContainsCondition implements Condition {

    private final String fieldName;
    private final String value;

    public StringContainsCondition(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public String toSqlCode() {
        return fieldName + " LIKE '%" + value + "%'";
    }

}
