package ru.apzakharov.service;

import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.repository.Dao;
import ru.apzakharov.repository.ListMapDao;

public class PsychologicalService implements HealthService<PsychologicalHealing> {
    public static final String HEAL_PHRASE = "healPhrase";
    private final Dao<PsychologicalHealing> dao;

    public PsychologicalService(Dao<PsychologicalHealing> dao) {
        this.dao = dao;
    }

    @Override
    public PsychologicalHealing getSomeHeal() {

        return dao.executeCommand("SELECT WHERE *").stream().findAny().orElseThrow(RuntimeException::new);
    }

    @Override
    public void putHeal(PsychologicalHealing heal) {
        dao.executeCommand("INSERT VALUES "+HEAL_PHRASE+"="+heal.getHeal());
    }


}
