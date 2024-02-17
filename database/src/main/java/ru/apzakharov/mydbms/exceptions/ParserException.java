package ru.apzakharov.mydbms.exceptions;

import static ru.apzakharov.mydbms.utils.ExceptionUtils.buildMessage;

/**
 * Класс, описывающий ошибки парсинга запросов к СУБД из выходящих данных
 */
public class ParserException extends RuntimeException {
    private static final String ACCEPTED_START_TOKENS = "SELECT, INSERT, UPDATE, DELETE";
    private static final String COMMON_MESSAGE = "Ошибка парсинга запроса: \n";

    public ParserException(String message) {
        super(COMMON_MESSAGE+message);
    }

    /**
     * Ошбика первого токена в строке
     * @param startToken - стартовый токен, с которого начинается парсинг строки
     * @return
     */
    public static ParserException notAcceptedStartToken(String startToken) {
        final String message = "Недопустимый стартовый токен: "+startToken+" ; Список допустимых: [ "+ACCEPTED_START_TOKENS+"]";
        return new ParserException(buildMessage(message));
    }

    /**
     * Ошибка парсинга токена, выделенного из поданной на парсинг строки
     * @param tokenValue
     * @return
     */
    public static ParserException notAcceptedTokenPattern(String tokenValue) {
        final String message = "Распознанный токен не соотвествует шаблону.\nТокен: [ "+tokenValue+" ]\nШаблон: [ key=value ] ";
        return new ParserException(buildMessage(message));
    }
}
