package ru.apzakharov;

import ru.apzakharov.utils.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServiceLocator {

    private static final Map<Class<? extends Detectable>, List<Detectable>> classContext = new ConcurrentHashMap<>();

    public static <T extends Detectable> void add(Class<T> clazz, T detectable) {
        classContext.merge(clazz, Collections.singletonList(detectable), (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        });
    }


    public static <T extends Detectable> T add(Class<T> clazz, Object... initialArgs) {
        T value = (T) tryGetInstance(clazz, initialArgs);
        add(clazz, value);
        return value;
    }


    private static Detectable tryGetInstance(Class<? extends Detectable> clazz, Object... args) {
        final Detectable instance;
        try {

            Class<?>[] argClasses = getArgClasses(args);
            Constructor<? extends Detectable> constructor = args.length == 0 ? clazz.getConstructor() : clazz.getConstructor(argClasses);
            instance = args.length == 0 ? constructor.newInstance() : constructor.newInstance(args);

        } catch (Exception e) {
            throw new ServiceLocatorException("", e);
        }

        return instance;
    }

    private static Class<?>[] getArgClasses(Object[] args) {
        if (args == null || args.length == 0) {
            return new Class[0];
        }
        Class<?>[] argClasses = new Class<?>[args.length];
        IntStream.rangeClosed(0, args.length).forEach(i -> argClasses[i] = args[i].getClass());
        return argClasses;
    }

    public static <T extends Detectable> T getForClass(Class<T> clazz, Object... initialArgs) {
        List<Detectable> classInstanceList = classContext.getOrDefault(clazz, Collections.emptyList());
        if (classInstanceList.isEmpty()) {
            Class interfaceClass = ReflectionUtils.getGenericParameterClass(clazz, Detectable.class, 0);

            classInstanceList = classContext.getOrDefault(interfaceClass, Collections.emptyList())
                    .stream()
                    .filter(inst -> inst.getClass().equals(clazz))
                    .collect(Collectors.toList());
        }


        Detectable service = classInstanceList.stream().findAny().orElse(add(clazz, initialArgs));

        return service != null ? (T) service : null;
    }


}
