package ru.apzakharov;

import ru.apzakharov.utils.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServiceLocator {

    private static final Map<Class<? extends Detectable<?>>, List<Detectable<?>>> applicationContext = new ConcurrentHashMap<>();

    public static <T extends Detectable<?>> void add(Class<T> clazz, T detectable) {
        applicationContext.merge(clazz, Collections.singletonList(detectable), (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        });
    }


    public static <T extends Detectable<?>> T add(Class<T> clazz, Object... initialArgs) {
        T value = (T) tryGetInstance(clazz, initialArgs);
        add(clazz, value);
        return value;
    }


    private static Detectable<?> tryGetInstance(Class<? extends Detectable<?>> clazz, Object... args) {
        try {

            Constructor<? extends Detectable<?>> constructor = findConstructor(clazz, args);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new ServiceLocatorException("", e);
        }

    }

    private static Constructor<? extends Detectable<?>> findConstructor(Class<? extends Detectable<?>> clazz, Object[] args) throws NoSuchMethodException {
        //Нет смысла пытаться что то мэтчить,т.к. если нет аргументов, то нужен конструктор по умолчанию
        if (args.length == 0) return clazz.getConstructor();

        List<Constructor<?>> constructors = Arrays.stream(clazz.getConstructors()).collect(Collectors.toList());
        Constructor<?> targetConstructor = null;

        for (Constructor<?> c : constructors) {
            if (matchConstructorArgs(args, c)) {
                targetConstructor = c;
            }
        }

        return targetConstructor != null ? (Constructor<? extends Detectable<?>>) targetConstructor : null;
    }

    private static boolean matchConstructorArgs(Object[] args, Constructor<?> c) {
        boolean matchArgsType = false;
        Class<?>[] constructorParameterTypes = c.getParameterTypes();
        if (constructorParameterTypes.length == args.length) {
            matchArgsType =
                    IntStream.range(0, args.length)
                            .allMatch(i ->
                                    constructorParameterTypes[i].isInstance(args[i])
                            );
        }
        return matchArgsType;
    }

    public static <T extends Detectable<?>> T getForClass(Class<T> clazz, Object... initialArgs) {
        List<Detectable<?>> classInstanceList = applicationContext.getOrDefault(clazz, Collections.emptyList());
        if (classInstanceList.isEmpty()) {
            Class interfaceClass = ReflectionUtils.getGenericParameterClass(clazz, Detectable.class, 0);

            classInstanceList = applicationContext.getOrDefault(interfaceClass, Collections.emptyList())
                    .stream()
                    .filter(inst -> inst.getClass().equals(clazz))
                    .collect(Collectors.toList());
        }

        Detectable<?> service = classInstanceList.stream().findAny().orElse(add(clazz, initialArgs));

        return service != null ? (T) service : null;
    }

    public static <T extends Detectable<?>> List<T> getAllForClass(Class<T> clazz, Object... initialArgs) {
        List<Detectable<?>> classInstanceList = applicationContext.getOrDefault(clazz, Collections.emptyList());
        if (classInstanceList.isEmpty()) {
            Class interfaceClass = ReflectionUtils.getGenericParameterClass(clazz, Detectable.class, 0);

            classInstanceList = applicationContext.getOrDefault(interfaceClass, Collections.emptyList())
                    .stream()
                    .filter(inst -> inst.getClass().equals(clazz))
                    .collect(Collectors.toList());
        }

        return classInstanceList.isEmpty() ? Collections.singletonList(add(clazz, initialArgs)) : (List<T>) classInstanceList;


    }


}
