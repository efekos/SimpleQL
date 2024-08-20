package dev.efekos.simple_ql.query;

import java.util.Objects;

public class RangeCondition implements Condition {

    private final Integer start;
    private final Integer end;
    private final String fieldName;

    public RangeCondition(Integer start, Integer end, String fieldName) {
        this.start = start;
        this.end = end;
        this.fieldName = fieldName;
        if (start == null && end == null) throw new IllegalArgumentException("start and end must not be null");
        if ((start != null && end != null) && start > end) throw new IllegalArgumentException("start > end");
    }

    @Override
    public String toString() {
        return "RangeCondition{" +
                "start=" + start +
                ", end=" + end +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }

    @Override
    public String toSqlCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(fieldName);

        if (Objects.equals(start, end)) builder.append(" = ").append(start);
        else if (start != null && end != null) builder.append(" BETWEEN ").append(start).append(" AND ").append(end);
        else if (start != null) builder.append(" > ").append(start);
        else builder.append(" < ").append(end);
        return builder.toString();
    }

}
