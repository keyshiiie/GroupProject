package strategy.sort;

import java.util.Comparator;
import java.util.List;

import Utils.LinkedList;
import car.Car;
import strategy.NamedStrategy;

public interface SortStrategy extends NamedStrategy {
    Comparator<Car> getComparator();
    LinkedList<Car> sort(LinkedList<Car> cars);
}
