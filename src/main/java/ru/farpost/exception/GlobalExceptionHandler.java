package ru.farpost.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.farpost.dto.ExceptionDTO;


/**
 * Глобальный обработчик исключений для приложения.
 * Перехватывает исключения {@link ErrorResponse} и возвращает структурированный ответ с информацией об ошибке.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Метод для обработки исключений типа {@link ErrorResponse}.
     * Преобразует исключение в объект {@link ExceptionDTO} и возвращает его в теле ответа.
     *
     * @param e исключение типа {@link ErrorResponse}, содержащее информацию об ошибке.
     * @return {@link ResponseEntity} с объектом {@link ExceptionDTO}, содержащим код и сообщение ошибки.
     */
    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<ExceptionDTO> handleException(ErrorResponse e) {
        ExceptionDTO errorResponse = new ExceptionDTO(e.getCode(), e.getMessage());
        return ResponseEntity.ok(errorResponse);
    }

}
