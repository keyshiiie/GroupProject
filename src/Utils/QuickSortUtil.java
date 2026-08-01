package Utils;

import java.util.Comparator;

public class QuickSortUtil {

    public static <T> void quickSort(T[] arr, Comparator<? super T> comparator) {
        if (arr == null || arr.length <= 1 || comparator == null) {
            return;
        }

        quickSort(arr, 0, arr.length - 1, comparator);
    }

    private static <T> void quickSort(T[] arr, int low, int high, Comparator<? super T> comparator) {
        if (low >= high) {
            return;
        }

        int pivotIndex = partition(arr, low, high, comparator);
        quickSort(arr, low, pivotIndex - 1, comparator);
        quickSort(arr, pivotIndex + 1, high, comparator);
    }

    private static <T> int partition(T[] arr, int low, int high, Comparator<? super T> comparator) {
        int mid = low + (high - low) / 2;
        T pivot = arr[mid];
        swap(arr, mid, high);
        int i = low;

        for (int j = low; j < high; j++) {
            if (comparator.compare(arr[j], pivot) <= 0) {
                swap(arr, i, j);
                i++;
            }
        }

        swap(arr, i, high);

        return i;
    }

    private static <T> void swap(T[] arr, int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
