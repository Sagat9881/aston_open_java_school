package ru.apzakharov.repository;

import ru.apzakharov.healing.Healing;

import java.util.List;

public interface Dao<T extends Healing> {

        List<T> executeCommand(String command);

}
