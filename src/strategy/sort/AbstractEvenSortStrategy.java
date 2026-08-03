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
    public CarList  sort(CarList cars) {
        if (cars == null || cars.isEmpty()) {
            return new CarList();
        }

        CarList result = new CarList(cars);
        CarList evenCars = new CarList();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (isEven(car)) {
                evenCars.add(car);
            }
        }

        if (evenCars.size() <= 1) {
            return result;
        }

        Car[] evenArray = evenCars.toArray(new Car[0]);
        QuickSortUtil.quickSort(evenArray, comparator);

        int evenIndex = 0;
        for (int i = 0; i < result.size(); i++) {
            Car car = result.get(i);
            if (isEven(car)) {
                result.set(i, evenArray[evenIndex++]);
            }
        }

        return result;
    }
}