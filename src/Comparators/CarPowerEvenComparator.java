package Comparators;

import car.Car;

import java.util.Comparator;

public class CarPowerEvenComparator implements Comparator<Car> {
    @Override
    public int compare(Car o1, Car o2) {
        int power1 = o1.getPower();
        int power2 = o2.getPower();

        boolean power1Even = power1 % 2 == 0;
        boolean power2Even = power2 % 2 == 0;

        if (!power1Even || !power2Even) {
            return 0;
        }

        return Integer.compare(power1, power2);
    }
}