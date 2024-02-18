package ru.apzakharov.mydbms.service;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import ru.apzakharov.mydbms.parsers.QueryParser;
import ru.apzakharov.mydbms.query.Query;
import ru.apzakharov.mydbms.query.SelectQuery;
import ru.apzakharov.mydbms.queryprocessors.QueryProcessor;

@Log4j2
@SuperBuilder
public abstract class AbstractQueryService<Q, S> implements QueryService<Q, S> {
    private final QueryParser<Q> parser;
    private final QueryProcessor<S> processor;

    public S getStorage() {
        return storage;
    }

    @Getter
    private S storage;

    protected AbstractQueryService(QueryParser<Q> parser, QueryProcessor<S> processor, S storage) {
        this.parser = parser;
        this.processor = processor;
        this.storage = storage;
    }

    /**
     * Метод выполняет введную подьзовательскую команду
     *
     * @param inputCommand - содержание пользоватльской команды
     */
    @Override
    public S processCommand(Q inputCommand) {
        S s;
        try {
            final Query query = parser.parseQuery(inputCommand);
            s = processor.processQuery(query, storage);
            //Если это не операция выборки из хранилища, то нужно обновить текущее хранилище новой версией
            if (!(query instanceof SelectQuery)) {
                this.storage = s;
            }
        } catch (Exception e) {
            log.error(e);
            s = null;
        }
        return s;
    }


}
