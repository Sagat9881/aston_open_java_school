package ru.apzakharov;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServiceLocator {

    private static final Map<Class<?>, Object> classContext = new HashMap<>();

    public static void add(Class<?> clazz, Object object) {
        classContext.put(clazz, object);
    }


    public static <T> T add(Class<T> clazz) {
        T value = tryGetInstance(clazz);
        classContext.put(clazz, value);
        return value;
    }

    private static <T> T tryGetInstance(Class<T> clazz) {
        final T instance;
        try {
            Constructor<T> defaultConstructor = clazz.getConstructor();
            instance = defaultConstructor.newInstance();
        } catch (Exception e) {
            throw new ServiceLocatorException("", e);
        }

        return instance;
    }

    public static <T> T getForClass(Class<T> clazz) {
        Object service = classContext.get(clazz);
        if (service == null) {
            service = classContext.values()
                    .stream()
                    .filter(value -> value.getClass().isAssignableFrom(clazz))
                    .findAny()
                    .orElse(add(clazz));
        }

        return service != null ? (T) service : null;
    }
}
