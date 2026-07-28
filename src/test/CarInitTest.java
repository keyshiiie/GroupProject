package test;

import car.Car;
import Initializer.CarRandomInitializer;
import Reader.CarReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CarInitTest {

    private static List<Car> fullList;
    @BeforeAll
    static void init() throws Exception {
        fullList = new ArrayList<>(CarRandomInitializer.initCarRandom(100));
    }
    @Test
    void testRandomInit() throws Exception {
        List<Car> randomCars = new ArrayList<>(CarRandomInitializer.initCarRandom(8));
        assertEquals(8, randomCars.size());

        assertTrue(fullList.containsAll(randomCars));

        Set<Car> set = new HashSet<>(randomCars);
        assertEquals(randomCars.size(), set.size(), "В выборке есть дубликаты");
    }
    @Test
    void testCarReader() throws Exception {
        List<Car> carsFromFile = CarReader.readCarsFromFile("randomCarsFile.txt",8);
        assertEquals(8, carsFromFile.size());
        assertTrue(fullList.containsAll(carsFromFile));

        Set<Car> set = new HashSet<>(carsFromFile);
        assertEquals(carsFromFile.size(), set.size(), "В выборке есть дубликаты");
    }
}