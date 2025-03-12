package ru.farpost.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.farpost.model.Document;

import java.util.*;

/**
 * Компонент, содержащий аналитические функции для обработки текста документов.
 */
@Component
public class DocumentsAnalyticFunctions {

    /**
     * Утилита для работы со списком стоп-слов.
     */
    private final StopWordsListUtil stopWordsUtil;

    /**
     * Метод для создания экземпляра с внедренной зависимостью {@link StopWordsListUtil}.
     *
     * @param stopWordsUtil утилита для обработки стоп-слов.
     */
    @Autowired
    DocumentsAnalyticFunctions(StopWordsListUtil stopWordsUtil) {
        this.stopWordsUtil = stopWordsUtil;
    }

    /**
     * Приватный метод для нормализации текста документа.
     * Удаляет знаки препинания, приводит текст к нижнему регистру и удаляет стоп-слова.
     *
     * @param text текст документа, который необходимо нормализовать.
     * @return список нормализованных слов из текста документа.
     */
    private List<String> getNormalizeWords(String text) {
        Set<String> stopWords = new HashSet<>(stopWordsUtil.getWords());
        String[] words =
                text
                .replaceAll("[^a-zA-Zа-яА-ЯёЁ\\s]", "")
                .toLowerCase()
                .split("\\s+");
        return Arrays.stream(words).filter(word -> !stopWords.contains(word)).toList();
    }

    /**
     * Метод для нормализации текста документа.
     *
     * @param text текст документа, который необходимо нормализовать.
     * @return нормализованный текст в виде строки.
     */
    public String normalizeDocumentText(String text) {
        return String.join(" ", getNormalizeWords(text));
    }


    /**
     * Метод для получения статистики по тексту документа.
     *
     * @param text текст документа, по которому необходимо вычислить статистику.
     * @return карта, содержащая ключевые метрики текста:
     *         <ul>
     *             <li>{@code word_count} – общее количество слов.</li>
     *             <li>{@code uniq_word_count} – количество уникальных слов.</li>
     *             <li>{@code avg_word_length} – средняя длина слова.</li>
     *             <li>{@code sentences_count} – количество предложений.</li>
     *         </ul>
     */
    public Map<String, Integer> getDocumentStatistic(String text) {

        List<String> normalizeWords = getNormalizeWords(text);
        Map<String, Integer> documentStatistic = new LinkedHashMap<>();

        int wordCount = normalizeWords.size();
        int uniqWordCount = (int) normalizeWords.stream().distinct().count();
        int avgWordCount = normalizeWords.stream().mapToInt(String::length).sum() / wordCount;
        int sentencesCount = (int) Arrays.stream(text.split("[.!?]+")).map(String::trim).filter(it -> !it.isEmpty()).count();

        documentStatistic.put("word_count", wordCount);
        documentStatistic.put("uniq_word_count", uniqWordCount);
        documentStatistic.put("avg_word_length", avgWordCount);
        documentStatistic.put("sentences_count", sentencesCount);

        return documentStatistic;

    }

    /**
     * Метод для получения списка идентификаторов документов, содержащих заданное слово.
     * Поиск осуществляется по нормализованному тексту документов.
     *
     * @param documents список документов, среди которых выполняется поиск.
     * @param word слово, которое необходимо найти в документах.
     * @return список идентификаторов документов, содержащих указанное слово.
     */
    public List<Long> getDocumentIdByWords(List<Document> documents, String word) {

        List<Long> documentIds = new ArrayList<>();

        for (Document document : documents) {
            if (getNormalizeWords(document.getText()).contains(word.toLowerCase()))
                documentIds.add(document.getId());
        }

        return documentIds;

    }

    /**
     * Метод для обработки запроса на получение общей статистики по всем документам.
     * Объединяет тексты всех документов и вычисляет их суммарную статистику.
     *
     * @param documents список документов, по которым необходимо вычислить общую статистику.
     * @return карта, содержащая ключевые метрики для всех документов:
     *         <ul>
     *             <li>{@code documents_count} – количество документов.</li>
     *             <li>{@code word_count} – общее количество слов.</li>
     *             <li>{@code uniq_word_count} – количество уникальных слов.</li>
     *             <li>{@code avg_word_length} – средняя длина слова.</li>
     *             <li>{@code sentences_count} – количество предложений.</li>
     *         </ul>
     */
    public Map<String, Integer> getAllDocumentStatistic(List<Document> documents) {

        Map<String, Integer> allDocumentsStatistic = new LinkedHashMap<>();
        StringBuilder allTextsFromDocuments = new StringBuilder();

        for (Document document : documents) {
            allTextsFromDocuments.append(document.getText()).append(" ");
        }

        allDocumentsStatistic.put("documents_count", documents.size());
        allDocumentsStatistic.putAll(getDocumentStatistic(String.valueOf(allTextsFromDocuments).trim()));

        return allDocumentsStatistic;

    }

    /**
     * Метод для получения топ-10 наиболее часто встречающихся слов в тексте.
     * Текст предварительно нормализуется, после чего выполняется подсчет количества вхождений каждого слова.
     *
     * @param text текст документа, в котором необходимо найти наиболее частотные слова.
     * @return карта, содержащая до 10 самых часто встречающихся слов и их количество.
     */
    public Map<String, Integer> getTopWords(String text) {

        List<String> normalizeWords = getNormalizeWords(text);
        Map<String, Integer> wordsCount = new HashMap<>();
        Map<String, Integer> topWords = new LinkedHashMap<>();

        for (String word : normalizeWords) {
            if (wordsCount.containsKey(word)) {
                wordsCount.put(word, wordsCount.get(word) + 1);
            } else {
                wordsCount.put(word, 1);
            }
        }

        ArrayList<Map.Entry<String, Integer>> entryWordsCount = new ArrayList<>(wordsCount.entrySet());

        entryWordsCount.sort(new TopWordsSorter());

        int minWordsCount = Math.min(10, entryWordsCount.size());

        for (int i = 0; i < minWordsCount; i++) {
            topWords.put(entryWordsCount.get(i).getKey(), entryWordsCount.get(i).getValue());
        }

        return topWords;

    }

}
