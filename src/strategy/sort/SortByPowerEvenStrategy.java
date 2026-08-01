package strategy.sort;

import comparators.CarPowerComparator;
import utils.QuickSortUtil;
import car.Car;
import comparators.CarPowerComparator;
import strategy.sort.AbstractEvenSortStrategy;

import java.util.*;

public class SortByPowerEvenStrategy extends AbstractEvenSortStrategy {
    private static final Comparator<Car> COMP = new CarPowerComparator();

    public SortByPowerEvenStrategy() {
        super(COMP);
    }

    @Override
    public String getLabel() {
        return "По мощности (чётные только)";
    }

    @Override
    protected boolean isEven(Car car) {
        return car.getPower() % 2 == 0;
    }
}