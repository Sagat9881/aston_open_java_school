package ru.apzakharov.mydbms.query;

import java.util.function.Predicate;

/**
 * Абстракция запроса к СУБД
 */
public interface Query {

    QueryType getType();

    <Q> Predicate<Q> getPredicate();
}
