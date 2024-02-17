package ru.apzakharov.mydbms.query;

import lombok.Builder;

import java.util.function.Predicate;
@Builder
public class SelectQuery implements Query {
    private Predicate predicate;
    @Override
    public QueryType getType() {
        return QueryType.SELECT;
    }

    @Override
    public <Q> Predicate<Q> getPredicate() {
        return predicate;
    }
}
