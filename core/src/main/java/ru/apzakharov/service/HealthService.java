package ru.apzakharov.service;

import ru.apzakharov.healing.Healing;

public interface HealthService<T extends Healing> {

    T getSomeHeal();

    void putHeal(T heal);

}
