package strategy.sort;

import utils.QuickSortUtil;
import car.Car;

import java.util.*;

public abstract class AbstractSortStrategy implements SortStrategy {
    protected final Comparator<Car> comparator;

    protected AbstractSortStrategy(Comparator<Car> comparator) {
        this.comparator = comparator;
    }

    @Override
    public List<Car> sort(List<Car> cars) {
        cars = handleNullOrEmpty(cars);

        if (cars.isEmpty()) return new ArrayList<>();

        Car[] arr = cars.toArray(new Car[0]);
        QuickSortUtil.quickSort(arr, comparator);

        return Arrays.asList(arr);
    }

    protected final List<Car> handleNullOrEmpty(List<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return new java.util.ArrayList<>();
        }

        return cars;
    }
}
