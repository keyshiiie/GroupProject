package strategy;

import Comparators.CarPowerComparator;
import Utils.QuickSortUtil;
import car.Car;
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