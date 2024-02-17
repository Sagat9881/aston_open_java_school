package ru.apzakharov.repository;

import ru.apzakharov.healing.Healing;
import ru.apzakharov.mydbms.service.QueryService;

import java.util.List;

public abstract class AbstractHealingDao<T extends Healing, STORAGE> implements Dao<T> {

    protected final QueryService<String, STORAGE> service;

    protected AbstractHealingDao(QueryService<String, STORAGE> service) {
        this.service = service;
    }

    @Override
    public T executeCommand(String command) {
        STORAGE storage = service.processCommand(command);
        return buildHealing(storage);
    }


    public abstract List<T> buildHealings(STORAGE command);

    public T buildHealing(STORAGE command) {
        return buildHealings(command).stream().findFirst().orElseThrow(RuntimeException::new);
    }

}
