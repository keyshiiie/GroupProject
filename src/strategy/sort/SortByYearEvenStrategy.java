package strategy.sort;

import car.Car;

import java.util.*;

public class SortByYearEvenStrategy extends AbstractSortStrategy {
    @Override
    public String getLabel() {
        return "По году (чётные только)";
    }

    @Override
    public Comparator<Car> getComparator() {
        return new comparators.CarYearEvenComparator();
    }
}