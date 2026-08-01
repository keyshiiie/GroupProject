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

@DisplayName("Тестирование стратегии подсчета по мощности")
class CountByPowerStrategyTest {

    private List<Car> cars;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        cars = new ArrayList<>();
        cars.add(createCar("Toyota Camry", 150, 2020));
        cars.add(createCar("BMW X5", 200, 2021));
        cars.add(createCar("Audi A4", 150, 2022));
        cars.add(createCar("Mercedes-Benz", 300, 2021));
        cars.add(createCar("Honda Accord", 150, 2020));

        // Перехватываем System.out для проверки вывода
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Подсчет по мощности - успешный поиск")
    void testCountByPowerSuccess() {
        String input = "150\n";
        Scanner scanner = new Scanner(input);
        CountByPowerStrategy strategy = new CountByPowerStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(3, result);
        assertEquals("150", strategy.getSearchValue());
        assertEquals("Подсчет по мощности двигателя", strategy.getLabel());
    }

    @Test
    @DisplayName("Подсчет по мощности - неверный ввод (буквы)")
    void testCountByPowerInvalidInput() {
        String input = "abc\n";
        Scanner scanner = new Scanner(input);
        CountByPowerStrategy strategy = new CountByPowerStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ошибка: введите корректное число"));
    }

    @Test
    @DisplayName("Подсчет по мощности - отрицательное число")
    void testCountByPowerNegativeNumber() {
        String input = "-100\n";
        Scanner scanner = new Scanner(input);
        CountByPowerStrategy strategy = new CountByPowerStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result); // Нет машин с отрицательной мощностью
    }

    @Test
    @DisplayName("Подсчет по мощности - пустой список")
    void testCountByPowerEmptyList() {
        String input = "150\n";
        Scanner scanner = new Scanner(input);
        CountByPowerStrategy strategy = new CountByPowerStrategy(scanner);

        int result = strategy.count(new ArrayList<>());

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Список автомобилей пуст"));
    }

    @Test
    @DisplayName("Подсчет по мощности - пустой ввод (отмена)")
    void testCountByPowerEmptyInput() {
        String input = "\n";
        Scanner scanner = new Scanner(input);
        CountByPowerStrategy strategy = new CountByPowerStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ввод отменен"));
    }

    @Test
    @DisplayName("Подсчет по мощности - мощность не найдена")
    void testCountByPowerNotFound() {
        String input = "999\n";
        Scanner scanner = new Scanner(input);
        CountByPowerStrategy strategy = new CountByPowerStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Подсчет по мощности - нулевая мощность")
    void testCountByPowerZero() {
        String input = "0\n";
        Scanner scanner = new Scanner(input);
        CountByPowerStrategy strategy = new CountByPowerStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result); // Нет машин с нулевой мощностью
    }

    private Car createCar(String model, int power, int year) {
        return new Car.Builder()
                .setModel(model)
                .setPower(power)
                .setYear(year)
                .build();
    }
}