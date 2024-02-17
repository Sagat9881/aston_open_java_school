package ru.apzakharov.mydbms.query;

import lombok.Builder;

import java.util.Map;
import java.util.function.Predicate;

@Builder
public class UpdateQuery implements Query {
    private final Map<String, Object> tokenValues;

    private Predicate predicate;

    @Override
    public QueryType getType() {
        return QueryType.UPDATE;
    }

    @Override
    public <Q> Predicate<Q> getPredicate() {
        return predicate;
    }

    public Map<String, Object> getTokensWithValue() {
        return tokenValues;
    }
}
