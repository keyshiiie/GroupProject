package utils;

import car.Car;

import java.util.Comparator;
import java.util.List;

public class SortedChecker {
    public static boolean isSorted(List<Car> cars, Comparator<Car> comparator) {
        if (cars == null || cars.size() <= 1) {
            return true;
        }

        for (int i = 0; i < cars.size() - 1; i++) {
            if (comparator.compare(cars.get(i), cars.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}
