package Utils;

import java.util.Comparator;

public class SortedChecker {

    public static <T> boolean isSorted(LinkedList<T> list, Comparator<? super T> comparator) {
        if (list == null || list.size() <= 1) {
            return true;
        }

        for (int i = 0; i < list.size() - 1; i++) {
            if (comparator.compare(list.get(i), list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}
