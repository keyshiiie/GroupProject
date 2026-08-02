package strategy.sort;

import Utils.LinkedList;
import Utils.QuickSortUtil;
import car.Car;

import java.util.*;

public abstract class AbstractSortStrategy implements SortStrategy {
    @Override
    public LinkedList<Car> sort(LinkedList<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return new LinkedList<>();
        }

        Car[] arr = cars.toArray(new Car[0]);
        QuickSortUtil.quickSort(arr, getComparator());

        return Arrays.asList(arr);
    }
}
