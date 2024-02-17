package ru.apzakharov.repository;

import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.mydbms.service.StringListMapQueryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListMapDao extends AbstractHealingDao<PsychologicalHealing, List<Map<String, Object>>> {


    public static final String HEAL_PHRASE = "healPhrase";

    public ListMapDao(StringListMapQueryService service) {
        super(service);
    }


    @Override
    public List<PsychologicalHealing> buildHealings(List<Map<String, Object>> command) {
        return command.stream()
                .map(row -> new PsychologicalHealing(row.getOrDefault(HEAL_PHRASE, HEAL_PHRASE).toString()))
                .collect(Collectors.toList());
    }
}
