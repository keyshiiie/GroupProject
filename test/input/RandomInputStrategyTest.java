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

    private static final int TEST_SIZE = 100;

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

        int requestedSize = 50;
        String input = requestedSize + "\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();
        assertEquals(requestedSize, result.size());

        for (int i = 0; i < result.size(); i++) {
            Car car = result.get(i);
            assertTrue(car.getPower() >= 20 && car.getPower() <= 1000,
                    "Мощность вне диапазона: " + car.getPower());
            assertTrue(car.getYear() >= 1950 && car.getYear() <= 2026,
                    "Год вне диапазона: " + car.getYear());
        }
    }

    @Test
    void shouldReturnAllCarsWhenRequestedMoreThanAvailable() throws Exception {
        // Создаем тестовый список с небольшим количеством машин
        CarList testCars = new CarList();
        for (int i = 0; i < TEST_SIZE; i++) {
            testCars.add(new Car.Builder()
                    .setModel("Test" + i)
                    .setPower(100 + i)
                    .setYear(2000 + i % 26)
                    .build());
        }

        // Подменяем реальный список тестовым через рефлексию
        Field carsField = CarRandomList.class.getDeclaredField("cars");
        carsField.setAccessible(true);
        carsField.set(null, testCars);

        Field initializedField = CarRandomList.class.getDeclaredField("initialized");
        initializedField.setAccessible(true);
        initializedField.setBoolean(null, true);

        int requestedSize = TEST_SIZE + 10;
        String input = requestedSize + "\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();

        assertEquals(TEST_SIZE, result.size());
        assertTrue(result.size() < requestedSize);
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

    @Test
    void shouldHandleZeroSize() throws Exception {
        CarRandomList.initCars();

        String input = "0\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleSmallSize() throws Exception {
        CarRandomList.initCars();

        int requestedSize = 1;
        String input = requestedSize + "\n";
        Scanner scanner = new Scanner(input);
        RandomInputStrategy strategy = new RandomInputStrategy(scanner);

        CarList result = strategy.setCars();
        assertEquals(requestedSize, result.size());

        Car car = result.get(0);
        assertNotNull(car);
        assertTrue(car.getPower() > 0);
        assertTrue(car.getYear() >= 1886);
    }

    @Test
    void shouldReturnDifferentCarsOnMultipleCalls() throws Exception {
        CarRandomList.initCars();

        String input1 = "5\n";
        Scanner scanner1 = new Scanner(input1);
        RandomInputStrategy strategy1 = new RandomInputStrategy(scanner1);
        CarList result1 = strategy1.setCars();

        String input2 = "5\n";
        Scanner scanner2 = new Scanner(input2);
        RandomInputStrategy strategy2 = new RandomInputStrategy(scanner2);
        CarList result2 = strategy2.setCars();

        assertEquals(5, result1.size());
        assertEquals(5, result2.size());

        assertNotSame(result1, result2);
    }
}