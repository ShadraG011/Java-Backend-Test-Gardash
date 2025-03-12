package ru.farpost.components;

import lombok.Getter;

/**
 * Перечисление для хранения сообщений об ошибках и соответствующих им кодов состояния.
 * Используется для управления сообщениями об ошибках в приложении.
 */
@Getter
public enum ExceptionsMessage {

    /**
     * Сообщение для случая, когда документ не найден.
     */
    DOCUMENT_NOT_FOUND("Документ не найден!", 404),

    /**
     * Сообщение для случая, когда документы не найдены.
     */
    DOCUMENTS_NOT_FOUND("Документы не найден!", 404),

    /**
     * Сообщение для случая, когда произошла ошибка при создании документа.
     */
    CREATE_ERROR("Ошибка при создании документа!", 400);

    /**
     * Сообщение об ошибке.
     */
    private final String message;

    /**
     * Код ошибки.
     */
    private final int code;

    /**
     * Конструктор для инициализации значений сообщения и кода.
     *
     * @param message сообщение об ошибке.
     * @param code код состояния.
     */
    ExceptionsMessage(String message, int code) {
        this.message = message;
        this.code = code;
    }

}
