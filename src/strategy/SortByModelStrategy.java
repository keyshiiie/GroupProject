package strategy;

import car.Car;
import java.util.*;

public class SortByModelStrategy extends AbstractSortStrategy {
    @Override public String getLabel() { return "По модели (model)"; }
    @Override public Comparator<Car> getComparator() {
        return Comparator.comparing(Car::getModel);
    }
}
