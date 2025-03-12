package ru.farpost.utils;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Утилитарный класс для хранения списка стоп-слов.
 * Загружает список слов из конфигурационного файла {@code application.yaml} с префиксом {@code stop}.
 */
@Data
@Component
@ConfigurationProperties(prefix = "stop")
public class StopWordsListUtil {

    /**
     * Список стоп-слов, загружаемый из конфигурации.
     */
    private List<String> words;

}
