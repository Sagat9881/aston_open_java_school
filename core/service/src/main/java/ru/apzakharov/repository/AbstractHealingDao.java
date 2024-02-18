package ru.apzakharov.repository;

import ru.apzakharov.Detectable;
import ru.apzakharov.exceptions.DaoException;
import ru.apzakharov.healing.Healing;
import ru.apzakharov.mydbms.service.QueryService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Базовая, абстрактная версия объекта, поставляющего данные из хранилища,
 * принимающего запросы в виде SQL подобного языка, т.е. строку в определенном, похожем на SQL формате.
 *
 * @param <T>       объект, в который сериализуются полученные из хранилища данные.
 * @param <STORAGE> тип данных хранилища, должен уметь принимать запросы с типом String
 */
public abstract class AbstractHealingDao<T extends Healing, STORAGE> implements Dao<T>, Detectable<AbstractHealingDao<T, STORAGE>> {

    protected final QueryService<String, STORAGE> queryService;

    protected AbstractHealingDao(QueryService<String, STORAGE> queryService) {
        this.queryService = queryService;
        registerForClass();
    }

    private <R> R doExecute(Callable<R> call) {
        try {
            return call.call();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<T> executeCommand(String command) {
        return doExecute(() -> buildEntities(queryService.processCommand(command)));
    }


    public abstract List<T> buildEntities(STORAGE rows);

    public abstract T buildEntity(Map<String, Object> row);

}
