package dev.efekos.simple_ql.query;

import java.util.List;

public class StringOneOfCondition implements Condition {

    private final String fieldName;
    private final List<String> value;

    public StringOneOfCondition(String fieldName, String... value) {
        this.fieldName = fieldName;
        this.value = List.of(value);
    }

    @Override
    public String toSqlCode() {
        return fieldName + " IN (" + String.join(",", value.stream().map(s -> "'" + s + "'").toList()) + ")";
    }

}
