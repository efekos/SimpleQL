package dev.efekos.simple_ql.query;

public class QueryBuilder {

    private final Query query;

    public QueryBuilder(String tableName) {
        this.query = new Query(tableName);
    }

    public QueryBuilder sortAscending(String fieldName){
        query.addSort(new Sort(fieldName,true));
        return this;
    }

    public QueryBuilder sortDescending(String fieldName){
        query.addSort(new Sort(fieldName,false));
        return this;
    }

    public QueryBuilder filterWithCondition(Condition condition){
        query.addCondition(condition);
        return this;
    }

    public QueryBuilder limit(int limit){
        query.setLimit(limit);
        return this;
    }

    public QueryBuilder skip(int skip){
        query.setSkip(skip);
        return this;
    }

    public Query getQuery(){
        return query;
    }

}
