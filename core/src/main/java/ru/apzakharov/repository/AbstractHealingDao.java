package ru.apzakharov.repository;

import ru.apzakharov.healing.Healing;
import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.mydbms.service.QueryService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Базовая, абстрактная версия объекта, поставляющего данные из хранилища,
 * принимающего запросы в виде SQL подобного языка, т.е. строку в определенном, похожем на SQL формате.
 * @param <T> объект, в который сериализуются полученные из хранилища данные.
 * @param <STORAGE> тип данных хранилища, должен уметь принимать запросы с типом String
 */
public abstract class AbstractHealingDao<T extends Healing, STORAGE> implements Dao<T> {

    protected final QueryService<String, STORAGE> queryService;

    protected AbstractHealingDao(QueryService<String, STORAGE> queryService) {
        this.queryService = queryService;
    }

    @Override
    public List<T> executeCommand(String command) {
        STORAGE storage = queryService.processCommand(command);
        return buildHealings(storage);
    }



    public abstract List<T> buildHealings(STORAGE rows);

    public abstract T buildHealing(Map<String, Object> row) ;

}
