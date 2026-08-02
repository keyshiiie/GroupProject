package Utils;

import car.Car;

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

    public static void quickSort(LinkedList list, Comparator<? super Car> comparator) {
        if (list == null || comparator == null || list.size() <= 1) return;
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private static void quickSort(LinkedList<Car> list, int left, int right, Comparator<? super Car> comparator) {
        int i = left, j = right;
        Car pivot = list.get(left + (right - left) / 2);

        while (i <= j) {
            while (comparator.compare(list.get(i), pivot) < 0) i++;
            while (comparator.compare(list.get(j), pivot) > 0) j--;

            if (i <= j) {
                Car a = list.get(i);
                Car b = list.get(j);
                list.set(i, b);
                list.set(j, a);
                i++;
                j--;
            }
        }

        if (left < j) quickSort(list, left, j, comparator);
        if (i < right) quickSort(list, i, right, comparator);
    }
}
