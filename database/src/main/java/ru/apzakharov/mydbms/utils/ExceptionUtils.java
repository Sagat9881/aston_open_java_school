package ru.apzakharov.mydbms.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class ExceptionUtils {

    public static  String buildMessage(String... messages) {
        return Arrays.stream(messages)
                .collect(
                        Collectors.joining(
                                "|\n"
                                ,"\n [ "
                                ," ]"
                        )
                );
    }
}
