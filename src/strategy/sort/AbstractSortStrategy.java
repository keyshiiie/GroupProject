package strategy.sort;

import car.Car;
import car.CarList;
import utils.QuickSortUtil;

import java.util.*;

public abstract class AbstractSortStrategy implements SortStrategy {
    protected final Comparator<Car> comparator;

    protected AbstractSortStrategy(Comparator<Car> comparator) {
        this.comparator = comparator;
    }

    @Override
    public List<Car> sort(CarList cars) {
        if (cars == null || cars.isEmpty()) {
            return new ArrayList<>();
        }

        Car[] arr = cars.toArray(new Car[0]);
        QuickSortUtil.quickSort(arr, comparator);

        return Arrays.asList(arr);
    }
}