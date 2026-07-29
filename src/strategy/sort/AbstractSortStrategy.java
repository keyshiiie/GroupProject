package strategy.sort;

import utils.QuickSortUtil;
import car.Car;

import java.util.*;

public abstract class AbstractSortStrategy implements SortStrategy {
    @Override
    public List<Car> sort(List<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return new ArrayList<>();
        }

        Car[] arr = cars.toArray(new Car[0]);
        QuickSortUtil.quickSort(arr, getComparator());
        return Arrays.asList(arr);
    }
}
