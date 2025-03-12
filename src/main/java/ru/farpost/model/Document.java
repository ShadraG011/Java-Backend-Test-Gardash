package ru.farpost.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность для хранения данных документа в базе данных.
 * Используется для представления документа в таблице {@code documents}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "documents")
public class Document {

    /**
     * Идентификатор документа.
     * Является первичным ключом в таблице {@code documents}.
     */
    @Id
    private Long id;

    /**
     * Текст документа.
     * Хранится в поле {@code text} таблицы {@code documents}.
     */
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

}
