package strategy;

import java.util.Comparator;
import java.util.List;
import car.Car;

public interface SortStrategy extends NamedStrategy {
    List<Car> sort(List<Car> cars);
}
