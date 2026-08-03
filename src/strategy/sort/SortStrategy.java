package strategy.sort;

import java.util.Comparator;
import java.util.List;
import car.Car;
import car.CarList;
import strategy.NamedStrategy;

public interface SortStrategy extends NamedStrategy {
    CarList sort(CarList cars);
}
