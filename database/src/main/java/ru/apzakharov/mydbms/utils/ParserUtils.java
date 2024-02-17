package ru.apzakharov.mydbms.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@UtilityClass
public class ParserUtils {
    public static Map<String, Object> getTokens(String[] splitStr) {
        Map<String, Object> newRow = new HashMap<>();
        for (int i = 0; i < splitStr.length; i++) {
            String token = splitStr[i].replaceAll("‘","").toLowerCase();
            if (Objects.equals(token, "id")) {
                newRow.put("id", Integer.parseInt(splitStr[i + 2]));
            }
            if (Objects.equals(token, "age")) {
                newRow.put("age", Integer.parseInt(splitStr[i + 2]));
            }
            if (Objects.equals(token, "cost")) {
                newRow.put("cost", Double.parseDouble(splitStr[i + 2]));
            }
            if (Objects.equals(token, "lastname")) {
                newRow.put("lastname", splitStr[i + 2].substring(1, splitStr[i + 2].length() - 1));
            }
            if (Objects.equals(token, "active")) {
                boolean active = false;
                if (Objects.equals(splitStr[i + 2], "true")) {
                    active = true;
                } else {
                    if (Objects.equals(splitStr[i + 2], "false")) {
                        active = false;
                    }
                }
                newRow.put("active", active);
            }
        }
        return newRow;
    }
//
//    public static Predicate parsePredicate(String[] splitStr) {
//        Predicate predicate;
//
//    }

}
