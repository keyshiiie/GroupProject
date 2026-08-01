package strategy.sort;

import comparators.CarModelComparator;
import car.Car;
import comparators.CarModelComparator;

import java.util.*;

public class SortByModelStrategy extends AbstractSortStrategy {
    @Override public String getLabel() { return "По модели (model)"; }
    private static final Comparator<Car> COMP = new CarModelComparator();

    public SortByModelStrategy() {
        super(COMP);
    }
}
