package ru.apzakharov.mydbms.query;

import lombok.Builder;

import java.util.function.Predicate;
@Builder
public class DeleteQuery implements Query {
    @Override
    public QueryType getType() {
        return QueryType.DELETE;
    }

    private Predicate predicate;

    @Override
    public <Q> Predicate<Q> getPredicate() {
        return predicate;
    }
}
