package ru.apzakharov.dao;

import ru.apzakharov.Detectable;
import ru.apzakharov.healing.Healing;

import java.util.List;

public interface Dao<T extends Healing> extends Detectable<Dao<T>> {

        List<T> executeCommand(String command);

}
