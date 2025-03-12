package ru.farpost.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Компаратор для сортировки слов по убыванию их частоты.
 * Используется для сортировки списка слов по количеству вхождений в тексте.
 */
public class TopWordsSorter implements Comparator<Map.Entry<String, Integer>> {

    /**
     * Метод для сравнения двух записей по значению (частоте встречаемости слов).
     * Сортировка выполняется в порядке убывания.
     *
     * @param o1 первая запись, содержащая слово и его количество вхождений.
     * @param o2 вторая запись, содержащая слово и его количество вхождений.
     * @return -1, если {@code o2} больше {@code o1},
     *         1, если {@code o1} больше {@code o2},
     *         и 0, если частоты одинаковы.
     */
    @Override
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        return o2.getValue().compareTo(o1.getValue());
    }

}
