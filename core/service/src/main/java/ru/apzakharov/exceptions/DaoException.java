package ru.apzakharov.exceptions;

public class DaoException extends RuntimeException {
    private final static String COMMON_MESSAGE = "Ошибка при попытке получить данные из хранилища: \n   ";

    public DaoException(Exception e) {
       super(COMMON_MESSAGE + e.getMessage(), e);
    }
}
