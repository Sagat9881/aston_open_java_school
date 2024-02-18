package ru.apzakharov;

public class ServiceLocatorException extends RuntimeException {
    private static final String COMMON_MESSAGE = "Ошибка работы службы поиска зависимостей: \n     ";

    public ServiceLocatorException(String message, Exception e) {
        super(message, e);
    }
    public ServiceLocatorException(String message) {
        super(message);
    }

    public static ServiceLocatorException serviceByClassNotFound(Class<?> clazz) {
        final String message = COMMON_MESSAGE + "Объект класса " + clazz.getSimpleName() + " не найден в контексте локатора";
        return new ServiceLocatorException(message);
    }
}
