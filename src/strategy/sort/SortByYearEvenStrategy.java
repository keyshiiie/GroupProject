package strategy.sort;

import comparators.CarYearComparator;
import utils.QuickSortUtil;
import car.Car;
import strategy.sort.AbstractEvenSortStrategy;

import java.util.*;

public class SortByYearEvenStrategy extends AbstractEvenSortStrategy {
    private static final Comparator<Car> COMP = new CarYearComparator();

    public SortByYearEvenStrategy() {
        super(COMP);
    }

    @Override
    public String getLabel() {
        return "По году (чётные только)";
    }

    @Override
    protected boolean isEven(Car car) {
        return car.getYear() % 2 == 0;
    }
}