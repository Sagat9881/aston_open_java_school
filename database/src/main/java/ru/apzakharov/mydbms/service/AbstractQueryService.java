package ru.apzakharov.mydbms.service;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import ru.apzakharov.mydbms.parsers.QueryParser;
import ru.apzakharov.mydbms.query.Query;
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
      * @param inputCommand - содержание пользоватльской команды
     */
    @Override
    public void processCommand(Q inputCommand) {
        try {
            final Query query = parser.parseQuery(inputCommand);

            this.storage = processor.processQuery(query, storage);
        } catch (Exception e) {
            log.error(e);
        }
    }



}
