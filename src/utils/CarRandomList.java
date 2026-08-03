package utils;

import car.Car;
import car.CarList;
import reader.RandomCarReader;

import java.util.ArrayList;
import java.util.List;

public class CarRandomList {
    private static CarList cars;
    private static volatile boolean initialized = false;
    public static synchronized void initCars() throws Exception {
        if (!initialized) {
            cars = new CarList(RandomCarReader.readRandomCarsFromFile());
            initialized = true;
        }
    }
    public static CarList getCars(){
        return cars;
    }
    private CarRandomList() {}
}
