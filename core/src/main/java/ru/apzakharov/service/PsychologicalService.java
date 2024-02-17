package ru.apzakharov.service;

import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.mydbms.service.QueryService;

import java.util.List;
import java.util.Map;

public class PsychologicalService implements HealthService<PsychologicalHealing> {

    private final QueryService<PsychologicalHealing, List<Map<String, Object>>> dao;

    public PsychologicalService(QueryService<PsychologicalHealing, List<Map<String, Object>>> dao) {
        this.dao = dao;
    }

    @Override
    public PsychologicalHealing getSomeHeal() {
        return null;
    }

    @Override
    public void putHeal(PsychologicalHealing heal) {

    }


}
