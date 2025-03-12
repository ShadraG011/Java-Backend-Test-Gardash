package ru.farpost.exception;

import lombok.Getter;
import ru.farpost.components.ExceptionsMessage;

/**
 * Класс для представления ошибки в виде исключения.
 * Содержит код ошибки и сообщение, которое может быть использовано для создания ответа с ошибкой.
 */
@Getter
public class ErrorResponse extends RuntimeException {

    /**
     * Код ошибки.
     */
    private final int code;

    /**
     * Конструктор для создания объекта {@link ErrorResponse} с сообщением и кодом ошибки.
     *
     * @param ex объект {@link ExceptionsMessage}, содержащий сообщение и код ошибки.
     */
    public ErrorResponse(ExceptionsMessage ex) {
        super(ex.getMessage());
        this.code = ex.getCode();
    }
}
