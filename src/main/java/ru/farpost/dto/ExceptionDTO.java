package ru.farpost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для представления ошибки.
 * Содержит информацию о коде и сообщении ошибки.
 */
@Setter
@Getter
@AllArgsConstructor
public class ExceptionDTO {

    /**
     * Код ошибки.
     */
    private int code;

    /**
     * Сообщение ошибки.
     */
    private String message;

}
