package dev.efekos.simple_ql.query;

import java.util.ArrayList;
import java.util.List;

public class Query {

    private List<Sort> sorts = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();
    private String tableName;
    private Integer limit;
    private int skip;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Query(List<Sort> sorts, List<Condition> conditions, String tableName) {
        this.sorts = sorts;
        this.conditions = conditions;
        this.tableName = tableName;
    }

    public Query(String tableName) {
        this.tableName = tableName;
    }

    public void addSort(Sort sort){
        sorts.add(sort);
    }

    public void addCondition(Condition condition){
        conditions.add(condition);
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getSkip() {
        return skip;
    }
}