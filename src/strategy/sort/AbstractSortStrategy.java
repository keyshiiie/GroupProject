package strategy.sort;

import car.Car;
import car.CarList;
import utils.QuickSortUtil;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractSortStrategy implements SortStrategy {
    protected final Comparator<Car> comparator;

    protected AbstractSortStrategy(Comparator<Car> comparator) {
        this.comparator = comparator;
    }

    @Override
    public CarList sort(CarList cars) {
        if (cars == null || cars.isEmpty()) {
            return new CarList();
        }

        Car[] arr = cars.toArray(new Car[0]);
        QuickSortUtil.quickSort(arr, comparator);

        return Stream.of(arr)
                .collect(
                        CarList::new,
                        CarList::add,
                        CarList::addAll
                );
    }
}