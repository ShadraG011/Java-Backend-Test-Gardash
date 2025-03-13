package ru.farpost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.farpost.components.ExceptionsMessage;
import ru.farpost.dto.DocumentDTO;
import ru.farpost.exception.ErrorResponse;
import ru.farpost.model.Document;
import ru.farpost.repository.DocumentRepository;
import ru.farpost.utils.DocumentsAnalyticFunctions;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с документами.
 * Обеспечивает бизнес-логику для операций над {@link Document} и взаимодействия с репозиторием {@link DocumentRepository}.
 */
@Service
public class DocumentService {

    /**
     * Репозиторий для работы с {@link Document}.
     */
    private final DocumentRepository documentRepository;

    /**
     * Класс для аналитических функций, связанных с документами.
     */
    private final DocumentsAnalyticFunctions analyticFunctions;

    /**
     * Конструктор для создания сервиса с внедренными зависимостями.
     *
     * @param documentRepository репозиторий для работы с {@link Document}.
     * @param analyticFunctions класс для аналитических функций с {@link Document}.
     */
    @Autowired
    DocumentService(DocumentRepository documentRepository, DocumentsAnalyticFunctions analyticFunctions) {
        this.documentRepository = documentRepository;
        this.analyticFunctions = analyticFunctions;
    }

    /**
     * Метод для преобразования {@link Document} в {@link DocumentDTO}.
     *
     * @param document объект {@link Document}, который необходимо преобразовать в {@link DocumentDTO}.
     * @return объект {@link DocumentDTO}, содержащий данные из {@link Document}.
     */
    private DocumentDTO convertToDTO(Document document) {
        return new DocumentDTO(document.getId(), document.getText());
    }

    /**
     * Метод для сохранения документа в базе данных.
     *
     * @param document объект {@link Document}, который нужно сохранить.
     */
    public void saveDocument(Document document) {
        documentRepository.save(document);
    }

    /**
     * Метод для поиска документа по его идентификатору.
     * Если документ не найден, выбрасывается исключение {@link ErrorResponse} с сообщением {@link ExceptionsMessage#DOCUMENT_NOT_FOUND}.
     *
     * @param id идентификатор документа.
     * @return объект {@link DocumentDTO}, содержащий данные найденного документа.
     */
    public DocumentDTO findDocument(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ErrorResponse(ExceptionsMessage.DOCUMENT_NOT_FOUND));
        return convertToDTO(document);
    }

    /**
     * Метод для нормализации текста в документе.
     * Если документ не найден, выбрасывается исключение {@link ErrorResponse} с сообщением {@link ExceptionsMessage#DOCUMENT_NOT_FOUND}.
     *
     * @param id идентификатор документа.
     * @return объект {@link DocumentDTO}, содержащий документ с нормализованным текстом.
     */
    public DocumentDTO getNormalizedDocument(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ErrorResponse(ExceptionsMessage.DOCUMENT_NOT_FOUND));
        document.setText(analyticFunctions.normalizeDocumentText(document.getText()));
        return convertToDTO(document);
    }

    /**
     * Метод для получения статистики по документу.
     * Если документ не найден, выбрасывается исключение {@link ErrorResponse} с сообщением {@link ExceptionsMessage#DOCUMENT_NOT_FOUND}.
     *
     * @param id идентификатор документа.
     * @return карта, содержащая статистику по документу.
     */
    public Map<String, Integer> getDocumentStatistics(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ErrorResponse(ExceptionsMessage.DOCUMENT_NOT_FOUND));
        return analyticFunctions.getDocumentStatistic(document.getText());
    }

    /**
     * Метод для получения статистики по всем документам.
     * Если документов в БД нет, то выбрасывается исключение {@link ErrorResponse} с сообщением {@link ExceptionsMessage#DOCUMENTS_NOT_FOUND}.
     *
     * @return карта, содержащая статистику по всем документам.
     */
    public Map<String, Integer> getAllDocumentsStatistics() {
        List<Document> documents = documentRepository.findAll();
        if (documents.isEmpty()) {
            throw new ErrorResponse(ExceptionsMessage.DOCUMENTS_NOT_FOUND);
        }
        return analyticFunctions.getAllDocumentStatistic(documents);
    }

    /**
     * Метод для получения статистики по наиболее часто встречающимся словам в документе.
     * Если документ не найден, выбрасывается исключение {@link ErrorResponse} с сообщением {@link ExceptionsMessage#DOCUMENT_NOT_FOUND}.
     *
     * @param id идентификатор документа.
     * @return карта, содержащая 10 наиболее часто встречающихся слов в документе и их частоту.
     */
    public Map<String, Integer> getTopWordInDocument(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ErrorResponse(ExceptionsMessage.DOCUMENT_NOT_FOUND));
        return analyticFunctions.getTopWords(document.getText());
    }

    /**
     * Метод для получения идентификаторов документов, содержащих заданное слово.
     * Если документов в БД нет, то выбрасывается исключение {@link ErrorResponse} с сообщением {@link ExceptionsMessage#DOCUMENTS_NOT_FOUND}.
     *
     * @param word слово, которое необходимо искать в документах.
     * @return список идентификаторов документов, содержащих заданное слово.
     */
    public List<Long> getDocumentIdByWords(String word) {
        List<Document> documents = documentRepository.findAll();
        if (documents.isEmpty()) {
            throw new ErrorResponse(ExceptionsMessage.DOCUMENTS_NOT_FOUND);
        }
        return analyticFunctions.getDocumentIdByWords(documents, word);
    }

    public Map<String, Integer> getBigramsInDocument(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ErrorResponse(ExceptionsMessage.DOCUMENT_NOT_FOUND));
        return analyticFunctions.getBigramsInDocument(document.getText());
    }
}
