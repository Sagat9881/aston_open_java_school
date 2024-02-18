package ru.apzakharov.mydbms.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@UtilityClass
public class PredicateUtils {

    public static final String OR = "or";
    public static final String AND = "and";
    public static final String GREATER = ">";
    public static final String LESS = "<";
    public static final String EQUALS = "=";

    public static <Q extends Comparable<Q>> Predicate<Map<String, Q>> less(String key, Q filter) {
        return (row) -> Objects.nonNull(row.get(key)) && row.get(key).compareTo(filter) < 0;
    }

    public static <Q extends Comparable<Q>> Predicate<Map<String, Q>> greater(String key, Q filter) {
        return (row) -> Objects.nonNull(row.get(key)) && row.get(key).compareTo(filter) > 0;
    }

    public static <Q extends Comparable<Q>> Predicate<Map<String, Q>> equals(String key, Q filter) {
        return (row) -> Objects.nonNull(row.get(key)) && row.get(key).compareTo(filter) == 0;
    }

    public static Predicate and(Predicate... one) {
        return Arrays.stream(one).reduce((pred, pred1) -> pred.and(pred1)).orElse(null);
    }

    public static Predicate or(Predicate one, Predicate two) {
        return one.or(two);
    }

    /**
     * Метод принимает на вход тело предиката (без WHERE).
     *
     * Если встречает OR или AND - делит строку надвое по токену и вызывает сам себя, (рекурсия)
     * передавая получившиеся предикаты (т.к. эти операторы всегда содержат вложенные предикаты, которые нужно распарсить)
     *
     * Если встречает что то из [>, <, =] - делит получившуюся строку надвое по символу, парсит каждый символ в значение
     * @param input
     * @return
     */
    public static Predicate<Map<String, Object>> buildPredicateFromString(String input) {
        if("*".equals(input)){
            return (map)-> true;
        }
        input = input.toLowerCase();

        Predicate predicate = null;
        boolean containsEscapeSequence = input.contains(OR) || input.contains(AND);
        // Оператор OR или AND всегда включают два других предиката, которые точно нужно парсить до значений
        if (containsEscapeSequence) {
            if (input.contains(OR)) {
                String[] or = input.split(OR);
                predicate = or(
                        buildPredicateFromString(or[0].trim())
                        , buildPredicateFromString(or[1].trim())
                );
            }
            if (input.contains(AND)) {
                String[] and = input.split(AND);
                predicate = and(
                          buildPredicateFromString(and[0].trim())
                        , buildPredicateFromString(and[1].trim())
                );
            }
        } else {
            if (input.contains(EQUALS)) {
                String[] split = input.split(EQUALS);
                Comparable value = split[1].trim();
                String key = split[0].trim();
                predicate = predicate == null ? equals(key, value) : predicate.and(equals(key, value));
            }
            if (input.contains(LESS)) {
                String[] split = input.split(LESS);
                Comparable value = split[1].trim();
                String key = split[0].trim();
                predicate = predicate == null ? less(key, value) : predicate.and(less(key, value));
            }

            if (input.contains(GREATER)) {
                String[] split = input.split(GREATER);
                Comparable value = split[1].trim();
                String key = split[0].trim();
                predicate = predicate == null ? greater(key, value) : predicate.and(greater(key, value));
            }
        }
        return predicate;
    }
}
