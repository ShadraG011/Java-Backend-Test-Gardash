package ru.farpost;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.farpost.dto.DocumentDTO;
import ru.farpost.model.Document;
import ru.farpost.repository.DocumentRepository;
import ru.farpost.service.DocumentService;
import ru.farpost.utils.DocumentsAnalyticFunctions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JavaBackendTestApplicationTests {

    private final String testText = "Lorem, ipsum odor odor odor a amet, consectetuer adipiscing elit. " +
            "Viverra& fermentum neque: tellus euismod gravida duis gravida of mi! " +
            "Conubia Conubia Arcu nibh; the leo leo leo leo leo platea enim lacinia? " +
            "Принимая во внимание внимание внимание внимание показатели успешности, дальнейшее развитие различных форм деятельности способствует повышению качества глубокомысленных рассуждений. " +
            "И нет сомнений, что активно развивающиеся страны третьего мира освещают чрезвычайно интересные особенности картины в целом, однако конкретные выводы, разумеется, заблокированы в рамках своих собственных рациональных ограничений. " +
            "Есть над чем задуматься: действия представителей оппозиции превращены в посмешище, хотя само их существование приносит несомненную пользу обществу.";

    private final String normalizedTestText = "lorem ipsum odor odor odor amet consectetuer adipiscing elit " +
            "viverra fermentum neque tellus euismod gravida duis gravida mi " +
            "conubia conubia arcu nibh leo leo leo leo leo platea enim lacinia " +
            "принимая внимание внимание внимание внимание показатели успешности дальнейшее развитие различных форм деятельности способствует повышению качества глубокомысленных рассуждений " +
            "нет сомнений активно развивающиеся страны третьего мира освещают чрезвычайно интересные особенности картины целом однако конкретные выводы разумеется заблокированы рамках своих собственных рациональных ограничений " +
            "есть задуматься действия представителей оппозиции превращены посмешище само существование приносит несомненную пользу обществу";

    @InjectMocks
    private DocumentService documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentsAnalyticFunctions analyticFunctions;

    @Test
    public void testSaveDocument() {

        Document document = new Document();
        document.setId(1L);
        document.setText(testText);

        documentService.saveDocument(document);

        Mockito.verify(documentRepository, Mockito.times(1)).save(document);

    }

    @Test
    public void testFindDocument() {

        long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        document.setText(testText);

        Mockito.when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        DocumentDTO documentDTO = documentService.findDocument(documentId);

        assertNotNull(documentDTO);
        assertEquals(documentId, documentDTO.getId());
        assertEquals(testText, documentDTO.getText());

    }


    @Test
    public void testGetNormalizedDocument() {

        long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        document.setText(testText);

        Mockito.when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        Mockito.when(analyticFunctions.normalizeDocumentText(document.getText())).thenReturn(normalizedTestText);

        DocumentDTO documentDTO = documentService.getNormalizedDocument(documentId);

        assertNotNull(documentDTO);
        assertEquals(normalizedTestText, documentDTO.getText());

    }

    @Test
    public void testGetDocumentStatistics() {

        long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        document.setText(testText);

        Map<String, Integer> statistics = new LinkedHashMap<>();
        statistics.put("word_count", 72);
        statistics.put("niq_word_count", 71);
        statistics.put("avg_word_length", 7);
        statistics.put("sentences_count", 6);

        Mockito.when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        Mockito.when(analyticFunctions.getDocumentStatistic(document.getText())).thenReturn(statistics);

        Map<String, Integer> testResult = documentService.getDocumentStatistics(documentId);

        assertNotNull(testResult);
        assertEquals(4, testResult.size());
        assertEquals(statistics, testResult);
        assertEquals(Integer.valueOf(72), testResult.get("word_count"));
        assertEquals(Integer.valueOf(71), testResult.get("niq_word_count"));
        assertEquals(Integer.valueOf(7), testResult.get("avg_word_length"));
        assertEquals(Integer.valueOf(6), testResult.get("sentences_count"));
    }

    @Test
    public void testGetAllDocumentsStatistics() {

        List<Document> documents = Arrays.asList(
                new Document(1L, "Тестовый текст. Для документа 1!"),
                new Document(2L, "Текст тестовый, для документа 2?"),
                new Document(4L, "Для документа 4. Тестовый текст."),
                new Document(5L, "Текст для документа 5 тестовый.")
        );

        Map<String, Integer> allStatistics = new LinkedHashMap<>();
        allStatistics.put("documents_count", 4);
        allStatistics.put("word_count", 16);
        allStatistics.put("uniq_word_count", 4);
        allStatistics.put("avg_word_length", 6);
        allStatistics.put("sentences_count", 6);

        Mockito.when(documentRepository.findAll()).thenReturn(documents);
        Mockito.when(analyticFunctions.getAllDocumentStatistic(documents)).thenReturn(allStatistics);

        Map<String, Integer> testResult = documentService.getAllDocumentsStatistics();

        assertNotNull(testResult);
        assertEquals(5, testResult.size());
        assertEquals(allStatistics, testResult);

    }

    @Test
    public void testGetTopWordInDocument() {

        Long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        document.setText(testText);

        Map<String, Integer> topWords = new LinkedHashMap<>();
        topWords.put("leo", 5);
        topWords.put("внимание", 4);
        topWords.put("odor", 3);
        topWords.put("conubia", 2);
        topWords.put("gravida", 2);
        topWords.put("качества", 1);
        topWords.put("adipiscing", 1);
        topWords.put("чрезвычайно", 1);
        topWords.put("принимая", 1);
        topWords.put("дальнейшее", 1);

        Mockito.when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        Mockito.when(analyticFunctions.getTopWords(document.getText())).thenReturn(topWords);

        Map<String, Integer> testResult = documentService.getTopWordInDocument(documentId);

        assertNotNull(testResult);
        assertEquals(10, testResult.size());
        assertEquals(topWords, testResult);

    }

    @Test
    public void testGetDocumentIdByWords() {

        String word = "тестовый";
        List<Document> documents = Arrays.asList(
                new Document(1L, "Тестовый текст для документа 1"),
                new Document(2L, "Текст тестовый для документа 2"),
                new Document(4L, "Для документа 4 тестовый текст"),
                new Document(5L, "Текст для документа 5 тестовый")
        );
        List<Long> documentIds = List.of(1L, 2L, 4L, 5L);

        Mockito.when(documentRepository.findAll()).thenReturn(documents);
        Mockito.when(analyticFunctions.getDocumentIdByWords(documents, word)).thenReturn(documentIds);

        List<Long> testResult = documentService.getDocumentIdByWords(word);

        assertNotNull(testResult);
        assertEquals(4, testResult.size());
        assertEquals(documentIds, testResult);
        assertTrue(testResult.contains(1L));
        assertTrue(testResult.contains(2L));
        assertTrue(testResult.contains(4L));
        assertTrue(testResult.contains(5L));

    }

}
