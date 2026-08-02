package count;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import strategy.count.CountByModelStrategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование стратегии подсчета по модели")
class CountByModelStrategyTest {

    private CarList cars;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        cars = new CarList();
        cars.add(createCar("Toyota Camry", 150, 2020));
        cars.add(createCar("BMW X5", 200, 2021));
        cars.add(createCar("Toyota Camry", 180, 2022));
        cars.add(createCar("Mercedes-Benz", 250, 2021));

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Подсчет по модели - успешный поиск")
    void testCountByModelSuccess() {
        String input = "Toyota Camry\n";
        Scanner scanner = new Scanner(input);
        CountByModelStrategy strategy = new CountByModelStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(2, result);
        assertEquals("Toyota Camry", strategy.getSearchValue());
        assertEquals("Подсчет по модели автомобиля", strategy.getLabel());
    }

    @Test
    @DisplayName("Подсчет по модели - регистронезависимый поиск")
    void testCountByModelCaseInsensitive() {
        String input = "toyota camry\n";
        Scanner scanner = new Scanner(input);
        CountByModelStrategy strategy = new CountByModelStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(2, result);
    }

    @Test
    @DisplayName("Подсчет по модели - модель не найдена")
    void testCountByModelNotFound() {
        String input = "Honda Civic\n";
        Scanner scanner = new Scanner(input);
        CountByModelStrategy strategy = new CountByModelStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Подсчет по модели - пустой ввод (отмена)")
    void testCountByModelEmptyInput() {
        String input = "\n";
        Scanner scanner = new Scanner(input);
        CountByModelStrategy strategy = new CountByModelStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ввод отменен"));
    }

    @Test
    @DisplayName("Подсчет по модели - пустой список автомобилей")
    void testCountByModelEmptyCars() {
        String input = "Toyota Camry\n";
        Scanner scanner = new Scanner(input);
        CountByModelStrategy strategy = new CountByModelStrategy(scanner);

        int result = strategy.count(new CarList());

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Список автомобилей пуст"));
    }

    @Test
    @DisplayName("Подсчет по модели - с пробелами в начале и конце")
    void testCountByModelWithSpaces() {
        String input = "  Toyota Camry  \n";
        Scanner scanner = new Scanner(input);
        CountByModelStrategy strategy = new CountByModelStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(2, result);
        assertEquals("Toyota Camry", strategy.getSearchValue());
    }

    private Car createCar(String model, int power, int year) {
        return new Car.Builder()
                .setModel(model)
                .setPower(power)
                .setYear(year)
                .build();
    }
}