package test;
import Utils.QuickSortUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortUtilTest {

    @Test
    @DisplayName("Сортировка массива целых чисел по возрастанию")
    void quickSort_integers_ascending() {
        Integer[] arr = {5, 2, 9, 1, 5, 6};
        QuickSortUtil.quickSort(arr, Comparator.naturalOrder());

        assertEquals(Arrays.asList(1, 2, 5, 5, 6, 9), Arrays.asList(arr));
    }

    @Test
    @DisplayName("Сортировка массива строк в лексикографическом порядке")
    void quickSort_strings_lexicographical() {
        String[] arr = {"banana", "apple", "cherry", "date"};
        QuickSortUtil.quickSort(arr, Comparator.naturalOrder());

        assertEquals(Arrays.asList("apple", "banana", "cherry", "date"), Arrays.asList(arr));
    }

    @Test
    @DisplayName("Сортировка с кастомным компаратором (по убыванию)")
    void quickSort_customComparator_descending() {
        Integer[] arr = {3, 1, 4, 1, 5, 9};
        QuickSortUtil.quickSort(arr, Comparator.reverseOrder());

        assertEquals(Arrays.asList(9, 5, 4, 3, 1, 1), Arrays.asList(arr));
    }

    @Test
    @DisplayName("Массив из одного элемента остаётся неизменным")
    void quickSort_singleElement_unchanged() {
        Integer[] arr = {42};
        QuickSortUtil.quickSort(arr, Comparator.naturalOrder());

        assertEquals(42, arr[0]);
        assertEquals(1, arr.length);
    }

    @Test
    @DisplayName("Пустой массив не вызывает ошибок и остаётся пустым")
    void quickSort_emptyArray_noError() {
        Integer[] arr = {};
        QuickSortUtil.quickSort(arr, Comparator.naturalOrder());

        assertEquals(0, arr.length);
    }

    @Test
    @DisplayName("Передача null-массива не вызывает исключений")
    void quickSort_nullArray_noException() {
        assertDoesNotThrow(() -> QuickSortUtil.quickSort(null, Comparator.naturalOrder()));
    }

    @Test
    @DisplayName("Передача null-компаратора не вызывает исключений и массив не изменяется")
    void quickSort_nullComparator_noException_and_unchanged() {
        Integer[] arr = {3, 2, 1};
        Integer[] original = arr.clone();

        assertDoesNotThrow(() -> QuickSortUtil.quickSort(arr, null));

        assertArrayEquals(original, arr);
    }

    @Test
    @DisplayName("Массив с повторяющимися элементами сортируется корректно")
    void quickSort_duplicates_sortedCorrectly() {
        Integer[] arr = {7, 3, 7, 1, 3, 7};
        QuickSortUtil.quickSort(arr, Comparator.naturalOrder());

        assertEquals(Arrays.asList(1, 3, 3, 7, 7, 7), Arrays.asList(arr));
    }

    @Test
    @DisplayName("Уже отсортированный массив остаётся корректным (устойчивость порядка для равных элементов не гарантируется, но порядок сохраняется по значениям)")
    void quickSort_alreadySorted_remainsValid() {
        Integer[] arr = {1, 2, 3, 4, 5};
        QuickSortUtil.quickSort(arr, Comparator.naturalOrder());

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(arr));
    }

    @Test
    @DisplayName("Обратно отсортированный массив корректно сортируется по возрастанию")
    void quickSort_reverseSorted_becomesAscending() {
        Integer[] arr = {5, 4, 3, 2, 1};
        QuickSortUtil.quickSort(arr, Comparator.naturalOrder());

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(arr));
    }
}
