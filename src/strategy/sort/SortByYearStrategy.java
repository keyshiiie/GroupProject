package strategy.sort;

import comparators.CarYearComparator;
import car.Car;
import comparators.CarYearComparator;

import java.util.*;

public class SortByYearStrategy extends AbstractSortStrategy {
    private static final Comparator<Car> COMP = new CarYearComparator();

    public SortByYearStrategy() {
        super(COMP);
    }

    @Override public String getLabel() { return "По году выпуска (year)"; }
}