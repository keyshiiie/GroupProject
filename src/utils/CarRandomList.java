package utils;

import car.Car;
import reader.RandomCarReader;

import java.util.ArrayList;
import java.util.List;

public class CarRandomList {
    private static List<Car> cars;
    private static volatile boolean initialized = false;
    public static synchronized void initCars() throws Exception {
        if (!initialized) {
            cars = new ArrayList<>(RandomCarReader.readRandomCarsFromFile());
            initialized = true;
        }
    }
    public static List<Car> getCars(){
        return cars;
    }
    private CarRandomList() {}
}
