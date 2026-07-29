package strategy;

import Utils.QuickSortUtil;
import car.Car;
import java.util.*;

public class SortByPowerEvenStrategy extends AbstractSortStrategy {
    @Override
    public String getLabel() {
        return "По мощности (чётные только)";
    }

    @Override
    public Comparator<Car> getComparator() {
        return new Comparators.CarPowerEvenComparator();
    }
}