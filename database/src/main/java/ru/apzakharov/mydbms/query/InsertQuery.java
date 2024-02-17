package ru.apzakharov.mydbms.query;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Builder
public class InsertQuery implements Query {
    private final List<Map<String, Object>> rows;

    @Override
    public QueryType getType() {
        return QueryType.INSERT;
    }

    @Override
    public <Q> Predicate<Q> getPredicate() {
        return null;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }
}
