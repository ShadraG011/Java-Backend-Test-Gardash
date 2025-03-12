package ru.farpost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Spring Boot.
 * Запускает Spring Boot приложение, инициируя запуск контекста приложения.
 */
@SpringBootApplication
public class JavaBackendTestApplication {

    /**
     * Точка входа в приложение.
     * Запускает Spring Boot приложение и инициализирует контекст.
     */
    public static void main(String[] args) {
        SpringApplication.run(JavaBackendTestApplication.class, args);
    }

}
