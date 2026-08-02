package strategy.sort;

import car.Car;
import car.CarList;
import utils.QuickSortUtil;

import java.util.*;

public abstract class AbstractEvenSortStrategy extends AbstractSortStrategy {

    protected AbstractEvenSortStrategy(Comparator<Car> comparator) {
        super(comparator);
    }

    protected abstract boolean isEven(Car car);

    @Override
    public List<Car> sort(CarList cars) {
        if (cars == null || cars.isEmpty()) {
            return new ArrayList<>();
        }

        List<Car> result = new ArrayList<>();
        result.addAll(cars);

        List<Integer> evenIndices = new ArrayList<>();
        List<Car> evenCars = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            Car c = result.get(i);
            if (isEven(c)) {
                evenIndices.add(i);
                evenCars.add(c);
            }
        }

        if (evenCars.size() <= 1) {
            return result;
        }

        Car[] evenArray = evenCars.toArray(new Car[0]);
        QuickSortUtil.quickSort(evenArray, super.comparator);

        for (int i = 0; i < evenIndices.size(); i++) {
            int index = evenIndices.get(i);
            result.set(index, evenArray[i]);
        }

        return result;
    }
}