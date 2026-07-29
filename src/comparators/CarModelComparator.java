package comparators;

import car.Car;

import java.util.Comparator;

public class CarModelComparator implements Comparator<Car>{
    @Override
    public int compare(Car c1, Car c2) {
        if (c1.getModel() == null && c2.getModel() == null) return 0;
        if (c1.getModel() == null) return -1;
        if (c2.getModel() == null) return 1;
        return c1.getModel().compareTo(c2.getModel());
    }
}
