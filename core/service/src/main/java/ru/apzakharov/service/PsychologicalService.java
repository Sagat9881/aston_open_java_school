package ru.apzakharov.service;

import ru.apzakharov.ServiceLocator;
import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.mydbms.service.StringListMapQueryService;
import ru.apzakharov.repository.Dao;
import ru.apzakharov.repository.ListMapDao;

import java.util.List;
import java.util.Random;

public class PsychologicalService implements HealthService<PsychologicalHealing> {
    public static final String HEAL_PHRASE = "healphrase";
    private final Dao<PsychologicalHealing> dao;

    public PsychologicalService(Dao<PsychologicalHealing> dao) {
        this.dao = dao;
        registerForClass();
    }

    public PsychologicalService() {
        this.dao = new ListMapDao<>(
                new StringListMapQueryService(),
                (map) -> new PsychologicalHealing(map.getOrDefault(HEAL_PHRASE, HEAL_PHRASE).toString()));

        registerForClass();
    }

    @Override
    public PsychologicalHealing getSomeHeal() {
        final Random r = new Random();
        final List<PsychologicalHealing> psychologicalHealings = dao.executeCommand("SELECT WHERE *");

        return psychologicalHealings.stream()
                .skip(r.nextInt(psychologicalHealings.size()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void putHeal(PsychologicalHealing heal) {
        dao.executeCommand("INSERT VALUES " + HEAL_PHRASE + "=" + heal.getHeal());
    }


}
