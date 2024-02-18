package ru.apzakharov.service;

import ru.apzakharov.Detectable;
import ru.apzakharov.healing.Healing;

public interface HealthService<T extends Healing> extends Detectable {

    T getSomeHeal();

    void putHeal(T heal);

}
