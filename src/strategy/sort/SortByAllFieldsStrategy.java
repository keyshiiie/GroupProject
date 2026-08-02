
package strategy.sort;

import car.Car;
import comparators.CarModelComparator;
import comparators.CarYearComparator;
import comparators.CarPowerComparator;

import java.util.Comparator;

public class SortByAllFieldsStrategy extends AbstractSortStrategy {
    private static final Comparator<Car> COMP = new CarModelComparator()
            .thenComparing(new CarYearComparator())
            .thenComparing(new CarPowerComparator());

    public SortByAllFieldsStrategy() {
        super(COMP);
    }

    @Override
    public String getLabel() {
        return "По всем полям (модель → год → мощность)";
    }
}