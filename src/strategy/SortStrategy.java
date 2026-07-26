package strategy;

import java.util.Comparator;
import java.util.List;
import Car.Car;

public interface SortStrategy extends NamedStrategy {
    Comparator<Car> getComparator();
    List<Car> sort(List<Car> cars);
}
