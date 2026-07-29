package strategy.sort;

import car.Car;

import java.util.*;

public class SortByPowerEvenStrategy extends AbstractSortStrategy {
    @Override
    public String getLabel() {
        return "По мощности (чётные только)";
    }

    @Override
    public Comparator<Car> getComparator() {
        return new comparators.CarPowerEvenComparator();
    }
}