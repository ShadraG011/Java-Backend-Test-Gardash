package ru.farpost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для документа.
 * Содержит информацию о документе, которая передается между слоями приложения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    /**
     * Идентификатор документа.
     */
    private Long id;

    /**
     * Текст документа.
     */
    private String text;

}
