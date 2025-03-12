package ru.farpost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.farpost.components.ExceptionsMessage;
import ru.farpost.dto.DocumentDTO;
import ru.farpost.exception.ErrorResponse;
import ru.farpost.model.Document;
import ru.farpost.service.DocumentService;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для обработки HTTP-запросов по управлению документами.
 */
@Controller
@RequestMapping("/api/documents")
public class DocumentController {

    /**
     * Сервис для работы с документами.
     * Обеспечивает бизнес-логику для операций над {@link DocumentDTO} и {@link Document}.
     */
    private final DocumentService documentService;

    /**
     * Конструктор для создания экземпляра контроллера документов с внедренным сервисом (@Autowired).
     *
     * @param documentService сервис для работы с документами
     */
    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Метод для обработки запроса на создание нового документа и сохранения его в системе.
     *
     * @param document объект документа, переданный в теле запроса.
     * @return {@link ResponseEntity} с сообщением о статусе создания документа.
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> createDocument(@RequestBody Document document) {
        if (document.getId() == null || document.getText().isEmpty() || document.getText().isBlank()) {
            throw new ErrorResponse(ExceptionsMessage.CREATE_ERROR);
        }
        documentService.saveDocument(document);
        var response = Map.of("createStatus", "Документ успешно сохранен!");
        return ResponseEntity.ok(response);
    }

    /**
     * Метод для обработки запроса на получение документа по его идентификатору.
     *
     * @param id идентификатор документа, переданный в параметре пути.
     * @return {@link ResponseEntity} с объектом {@link DocumentDTO}, содержащим данные документа.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public  ResponseEntity<DocumentDTO> getDocument(@PathVariable("id") Long id) {
        DocumentDTO document = documentService.findDocument(id);
        return ResponseEntity.ok(document);
    }

    /**
     * Метод для обработки запроса на получение нормализованного документа по его идентификатору.
     *
     * @param id идентификатор документа, переданный в параметре пути.
     * @return {@link ResponseEntity} с объектом {@link DocumentDTO}, содержащим нормализованные данные документа.
     */
    @RequestMapping(value = "/{id}/normalized", method = RequestMethod.GET)
    public ResponseEntity<DocumentDTO> getNormalizedDocument(@PathVariable("id") Long id) {
        DocumentDTO document = documentService.getNormalizedDocument(id);
        return ResponseEntity.ok(document);
    }

    /**
     * Метод для обработки запроса на получение статистики по документу.
     *
     * @param id идентификатор документа, переданный в параметре пути.
     * @return {@link ResponseEntity} с картой, содержащей статистические данные по документу.
     */
    @RequestMapping(value = "/{id}/statistics", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> getDocumentStatistics(@PathVariable("id") Long id) {
        return ResponseEntity.ok(documentService.getDocumentStatistics(id));
    }

    /**
     * Метод для обработки запроса на получение статистики по всем документам.
     *
     * @return {@link ResponseEntity} с картой, содержащей статистические данные по всем {@link Document}.
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> getAllDocumentsStatistics() {
        return ResponseEntity.ok(documentService.getAllDocumentsStatistics());
    }

    /**
     * Метод для обработки запроса на получение наиболее часто встречающихся слов в документе по его идентификатору.
     *
     * @param id идентификатор документа, переданный в параметре пути.
     * @return {@link ResponseEntity} с картой, содержащей топ-слова в {@link Document}.
     */
    @RequestMapping(value = "/{id}/top-words", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> getTopWordInDocument(@PathVariable("id") Long id) {
        return ResponseEntity.ok(documentService.getTopWordInDocument(id));
    }

    /**
     * Метод для обработки запроса на поиск идентификаторов документов по заданному слову.
     *
     * @param word слово для поиска в документах.
     * @return {@link ResponseEntity} со списком идентификаторов документов, содержащих указанное слово.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<Long>> getDocumentIdByWords(@RequestParam("word") String word) {
        return ResponseEntity.ok(documentService.getDocumentIdByWords(word));
    }

}
