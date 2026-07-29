package comparators;

import car.Car;

import java.util.Comparator;

public class CarYearEvenComparator implements Comparator<Car> {
    @Override
    public int compare(Car o1, Car o2) {
        int year1 = o1.getYear();
        int year2 = o2.getYear();

        boolean year1Even = year1 % 2 == 0;
        boolean year2Even = year2 % 2 == 0;

        if (!year1Even || !year2Even) {
            return 0;
        }

        return Integer.compare(year1, year2);
    }
}
