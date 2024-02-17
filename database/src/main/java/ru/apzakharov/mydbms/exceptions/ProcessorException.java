package ru.apzakharov.mydbms.exceptions;

import ru.apzakharov.mydbms.query.Query;
import ru.apzakharov.mydbms.query.QueryType;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ru.apzakharov.mydbms.utils.ExceptionUtils.buildMessage;

public class ProcessorException extends RuntimeException {

    private static final String COMMON_MESSAGE = "Ошибка выполнения запроса: \n";

    public ProcessorException(String message) {
        super(COMMON_MESSAGE + message);
    }


    private static final String ACCEPTED_TYPES = Arrays.stream(QueryType.values())
            .map(QueryType::getTypeString)
            .collect(
                    Collectors.joining(", ", "[ ", " ]")
            );

    public ProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Продуцирование ошибки в поптыке идентифицировать тип запроса к СУБД
     * @param query
     * @return
     * @param <Q>
     */
    public static <Q extends Query> ProcessorException failIdentifyQueryType(Q query) {
        String queryType = query == null ? null : query.getType().getTypeString();
        return new ProcessorException("Неизвестный тип: [ " + queryType + " ] ; Допустимые типы: " + ACCEPTED_TYPES);
    }

    /**
     * Cast - с англ, попытка привести один тип данных, к другому. Если ты это читаешь, то попытка, очевидно, неудачная ._.
     * @param e
     * @return
     */
    public static RuntimeException failCastExpression(ClassCastException e) {
        String exceptionMessage = e.getMessage();
        String message = "Не удалось определить тип объекта-запроса.\nПричина: [ " + exceptionMessage + " ]";
        return new ProcessorException(buildMessage(message));
    }
}
