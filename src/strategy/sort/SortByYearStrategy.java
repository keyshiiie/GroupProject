package strategy.sort;

import car.Car;

import java.util.*;

public class SortByYearStrategy extends AbstractSortStrategy {
    @Override public String getLabel() { return "По году выпуска (year)"; }
    @Override public Comparator<Car> getComparator() {
        return Comparator.comparingInt(Car::getYear);
    }
}