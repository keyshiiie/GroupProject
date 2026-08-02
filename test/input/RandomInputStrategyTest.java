package input;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.input.RandomInputStrategy;
import utils.CarRandomList;

import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RandomInputStrategyTest {

    @BeforeEach
    @AfterEach
    void resetSingleton() throws Exception {
        Field initializedField = CarRandomList.class.getDeclaredField("initialized");
        initializedField.setAccessible(true);
        initializedField.setBoolean(null, false);

        Field carsField = CarRandomList.class.getDeclaredField("cars");
        carsField.setAccessible(true);
        carsField.set(null, null);
    }

    @Test
    void shouldReturnSubListOfRequestedSize() throws Exception {
        CarRandomList.initCars();

        String input = "500\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();
        assertEquals(500, result.size());

        for (int i = 0; i < result.size(); i++) {
            Car car = result.get(i);
            assertTrue(car.getPower() >= 20 && car.getPower() <= 1000);
            assertTrue(car.getYear() >= 1950 && car.getYear() <= 2026);
        }
    }

    @Test
    void shouldReturnAllCarsWhenRequestedMoreThanAvailable() throws Exception {
        CarRandomList.initCars();

        String input = "2000000\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();
        assertEquals(2000000, result.size());
    }

    @Test
    void shouldReturnEmptyListOnInvalidInput() {
        String input = "not a number\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenCarRandomListNotInitialized() {
        String input = "10\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();
        assertTrue(result.isEmpty());
    }

    @Test
    void getLabelShouldReturnCorrectString() {
        Scanner scanner = new Scanner("");
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);
        assertEquals("Случайное заполнение списка автомобилей", strategy.getLabel());
    }
}