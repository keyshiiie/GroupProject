package strategy.count;

import car.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование стратегии подсчета по году")
class CountByYearStrategyTest {

    private List<Car> cars;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        cars = new ArrayList<>();
        cars.add(createCar("Toyota Camry", 150, 2020));
        cars.add(createCar("BMW X5", 200, 2021));
        cars.add(createCar("Audi A4", 180, 2020));
        cars.add(createCar("Mercedes-Benz", 250, 2022));
        cars.add(createCar("Honda Accord", 140, 2020));

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Подсчет по году - успешный поиск")
    void testCountByYearSuccess() {
        String input = "2020\n";
        Scanner scanner = new Scanner(input);
        CountByYearStrategy strategy = new CountByYearStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(3, result);
        assertEquals("2020", strategy.getSearchValue());
        assertEquals("Подсчет по году выпуска", strategy.getLabel());
    }

    @Test
    @DisplayName("Подсчет по году - год не найден")
    void testCountByYearNotFound() {
        String input = "2019\n";
        Scanner scanner = new Scanner(input);
        CountByYearStrategy strategy = new CountByYearStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Подсчет по году - неверный ввод (буквы)")
    void testCountByYearInvalidInput() {
        String input = "2020a\n";
        Scanner scanner = new Scanner(input);
        CountByYearStrategy strategy = new CountByYearStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ошибка: введите корректное число"));
    }

    @Test
    @DisplayName("Подсчет по году - пустой ввод (отмена)")
    void testCountByYearEmptyInput() {
        String input = "\n";
        Scanner scanner = new Scanner(input);
        CountByYearStrategy strategy = new CountByYearStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ввод отменен"));
    }

    @Test
    @DisplayName("Подсчет по году - пустой список")
    void testCountByYearEmptyCars() {
        String input = "2020\n";
        Scanner scanner = new Scanner(input);
        CountByYearStrategy strategy = new CountByYearStrategy(scanner);

        int result = strategy.count(new ArrayList<>());

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Список автомобилей пуст"));
    }

    @Test
    @DisplayName("Подсчет по году - будущий год")
    void testCountByYearFuture() {
        String input = "2030\n";
        Scanner scanner = new Scanner(input);
        CountByYearStrategy strategy = new CountByYearStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Подсчет по году - отрицательный год")
    void testCountByYearNegative() {
        String input = "-2020\n";
        Scanner scanner = new Scanner(input);
        CountByYearStrategy strategy = new CountByYearStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
    }

    private Car createCar(String model, int power, int year) {
        return new Car.Builder()
                .setModel(model)
                .setPower(power)
                .setYear(year)
                .build();
    }
}