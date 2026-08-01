package strategy;

import Comparators.CarPowerComparator;
import car.Car;
import java.util.*;

public class SortByPowerStrategy extends AbstractSortStrategy {
    private static final Comparator<Car> COMP = new CarPowerComparator();

    public SortByPowerStrategy() {
        super(COMP);
    }

    @Override public String getLabel() { return "По мощности (power)"; }
}
