package dev.efekos.simple_ql.query;

import java.util.List;

public class AnyOfCondition implements Condition {

    private final List<Condition> conditions;

    public AnyOfCondition(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public AnyOfCondition(Condition... conditions) {
        this.conditions = List.of(conditions);
    }

    @Override
    public String toSqlCode() {
        StringBuilder builder = new StringBuilder().append("(");

        for (int i = 0; i < conditions.size(); i++) {
            String sql = conditions.get(i).toSqlCode();
            builder.append(sql);
            if (i != conditions.size() - 1) builder.append(" OR ");
        }

        return builder.append(")").toString();
    }

}
