package ru.apzakharov.repository;

import ru.apzakharov.healing.Healing;
import ru.apzakharov.mydbms.service.QueryService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListMapDao<T extends Healing> extends AbstractHealingDao<T, List<Map<String, Object>>> {

    private final Function<Map<String, Object>, T> mapFunction;

    public ListMapDao(QueryService<String, List<Map<String, Object>>> service,
                      Function<Map<String, Object>, T> mapFunction) {
        super(service);
        this.mapFunction = mapFunction;

    }

    @Override
    public List<T> buildEntities(List<Map<String, Object>> rows) {
        return rows.stream()
                .map(this::buildEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public T buildEntity(Map<String, Object> row) {
        return mapFunction.apply(row);
    }


}
