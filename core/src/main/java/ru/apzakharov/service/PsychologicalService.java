package ru.apzakharov.service;

import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.repository.Dao;
import ru.apzakharov.repository.ListMapDao;

public class PsychologicalService implements HealthService<PsychologicalHealing> {

    private final Dao<PsychologicalHealing> dao;

    public PsychologicalService(ListMapDao dao) {
        this.dao = dao;
    }

    @Override
    public PsychologicalHealing getSomeHeal() {

        return dao.executeCommand("");
    }

    @Override
    public void putHeal(PsychologicalHealing heal) {

    }


}
