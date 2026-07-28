package Initializer;

import car.Car;
import Reader.CarReader;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CarRandomInitializer {
    public static List<Car> initCarRandom(int size) throws Exception {
        List<Car> randomCars = new ArrayList<>(CarReader.readCarsFromFile("randomCarsFile.txt", 100));
        Collections.shuffle(randomCars);
        return randomCars.subList(0, size);
    }
}
