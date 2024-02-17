package ru.apzakharov.mydbms.parsers;

import ru.apzakharov.mydbms.query.Query;
public interface QueryParser<O> {
    /**
     * Метод парсит входные данные в конкретные запросы к СУБД
     * @param input - объект, который нуно распарсить для  получения объекта-запроса
     * @return
     * @param <Q>
     */
    <Q extends Query> Q parseQuery(O input);
}
