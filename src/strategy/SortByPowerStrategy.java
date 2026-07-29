package strategy;

import car.Car;
import java.util.*;

public class SortByPowerStrategy extends AbstractSortStrategy {
    @Override public String getLabel() { return "По мощности (power)"; }
    @Override public Comparator<Car> getComparator() {
        return Comparator.comparingInt(Car::getPower);
    }
}
