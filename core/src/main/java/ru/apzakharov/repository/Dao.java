package ru.apzakharov.repository;

import ru.apzakharov.healing.Healing;

public interface Dao<T extends Healing> {

        T executeCommand(String command);

}
