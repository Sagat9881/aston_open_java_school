package ru.apzakharov.mydbms.queryprocessors;

import lombok.extern.log4j.Log4j2;
import ru.apzakharov.mydbms.exceptions.ProcessorException;
import ru.apzakharov.mydbms.query.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Log4j2
public class ListMapQueryProcessor implements QueryProcessor<List<Map<String, Object>>> {
    /**
     * Выполнение 'Q query' к представленному хранилищу 'storage'
     *
     * @param query   - запрос к хранилищу
     * @param storage - Представленное хранилище данных
     * @param <Q>
     * @return
     */
    @Override
    public <Q extends Query> List<Map<String, Object>> processQuery(Q query, List<Map<String, Object>> storage) {
        try {
            return doProcessQuery(query, storage);
        } catch (ClassCastException e) {
            throw ProcessorException.failCastExpression(e);
        } catch (Exception e) {
            throw new ProcessorException(e.getMessage(), e);
        }
    }

    private <Q extends Query> List<Map<String, Object>> doProcessQuery(Q query, List<Map<String, Object>> storage) {
        final QueryType type = query.getType();
        //т.к. раз уж кастуем типы в рантайме, то лучше все обернуть в try-catch
        switch (type) {
            case UPDATE:
                return tryToProcess(
                        () -> processUpdate((UpdateQuery) query, storage)
                );

            case INSERT:
                return tryToProcess(
                        () -> processInsert((InsertQuery) query, storage)
                );
            case SELECT:
                final ArrayList<Map<String, Object>> sample = new ArrayList<>();
                tryToProcess(
                        () -> processSelect((SelectQuery) query, storage, sample)
                );
                return sample;
            case DELETE:
                return tryToProcess(
                        () -> processDelete((DeleteQuery) query, storage)
                );
            default:
                throw ProcessorException.failIdentifyQueryType(query);
        }
    }

    private List<Map<String, Object>> processDelete(DeleteQuery deleteQuery, List<Map<String, Object>> storage) {
        storage = storage.stream()
                .filter(deleteQuery.getPredicate().negate())
                .collect(Collectors.toList());
        return storage;
    }

    private List<Map<String, Object>> processSelect(SelectQuery selectQuery, List<Map<String, Object>> storage, List<Map<String, Object>> sample) {
        sample.addAll(storage.stream()
                .filter(row -> selectQuery.getPredicate().test(row))
                .collect(Collectors.toList()));
        log.info(sample);
        return storage;
    }

    private List<Map<String, Object>> processInsert(InsertQuery insertQuery, List<Map<String, Object>> storage) {
        final List<Map<String, Object>> newRows = insertQuery.getRows();
        storage.addAll(newRows);
        return storage;
    }

    private List<Map<String, Object>> processUpdate(UpdateQuery updateQuery, List<Map<String, Object>> storage) {
        final Map<String, Object> tokensWithValues = updateQuery.getTokensWithValue();
        storage.stream()
                .filter(row -> updateQuery.getPredicate().test(row))
                .forEach(row -> {
                    tokensWithValues.keySet().forEach(tokenName -> {
                        final Object newValue = tokensWithValues.get(tokenName);
                        row.replace(tokenName, newValue);
                    });
                });

        return storage;
    }

    private <R> R tryToProcess(Callable<R> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new ProcessorException(e.getMessage());
        }
    }
}
