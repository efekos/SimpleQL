package dev.efekos.simple_ql.query;

public class BooleanMatchCondition implements Condition {

    private final String fieldName;
    private final boolean value;

    public BooleanMatchCondition(String fieldName, boolean value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public String toSqlCode() {
        return fieldName + " = " + (value ? '1' : '0');
    }

}
