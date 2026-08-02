package count;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import strategy.count.BaseCountStrategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование абстрактного класса BaseCountStrategy")
class BaseCountStrategyTest {

    private CarList cars;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        cars = new CarList();
        cars.add(createCar("Toyota Camry", 150, 2020));
        cars.add(createCar("BMW X5", 200, 2021));
        cars.add(createCar("Audi A4", 180, 2022));

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Подсчет с null списком - возвращает 0 и выводит сообщение")
    void testCountWithNullList() {
        Scanner scanner = new Scanner("test\n");
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        int result = strategy.count(null);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Список автомобилей пуст"));
    }

    @Test
    @DisplayName("Подсчет с пустым списком - возвращает 0 и выводит сообщение")
    void testCountWithEmptyList() {
        Scanner scanner = new Scanner("test\n");
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        int result = strategy.count(new CarList());

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Список автомобилей пуст"));
    }

    @Test
    @DisplayName("Пустой ввод пользователя - возвращает 0 и выводит сообщение об отмене")
    void testCountWithEmptyInput() {
        Scanner scanner = new Scanner("\n");
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ввод отменен"));
    }

    @Test
    @DisplayName("Null ввод пользователя - возвращает 0 и выводит сообщение об отмене")
    void testCountWithNullInput() {
        Scanner scanner = new Scanner("\n");
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ввод отменен"));
    }

    @Test
    @DisplayName("Успешный парсинг и подсчет - возвращает корректное количество")
    void testCountSuccess() {
        String input = "test\n";
        Scanner scanner = new Scanner(input);
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(3, result); // Все машины подходят под условие (always true)
        assertEquals("test", strategy.getSearchValue());
        assertTrue(outputStream.toString().contains("Введите значение:"));
    }

    @Test
    @DisplayName("Неуспешный парсинг - возвращает 0")
    void testCountWithFailedParsing() {
        String input = "invalid\n";
        Scanner scanner = new Scanner(input);
        FailingParseStrategy strategy = new FailingParseStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertTrue(outputStream.toString().contains("Ошибка парсинга"));
    }

    @Test
    @DisplayName("Проверка, что getLabel() возвращает корректное значение")
    void testGetLabel() {
        Scanner scanner = new Scanner("test\n");
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        assertEquals("Тестовая стратегия", strategy.getLabel());
    }

    @Test
    @DisplayName("Проверка, что getSearchValue() возвращает значение после парсинга")
    void testGetSearchValue() {
        String input = "test_value\n";
        Scanner scanner = new Scanner(input);
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        strategy.count(cars);

        assertEquals("test_value", strategy.getSearchValue());
    }

    @Test
    @DisplayName("Проверка, что getSearchValue() возвращает null до подсчета")
    void testGetSearchValueBeforeCount() {
        Scanner scanner = new Scanner("test\n");
        TestCountStrategy strategy = new TestCountStrategy(scanner);

        assertNull(strategy.getSearchValue());
    }

    @Test
    @DisplayName("Подсчет с условием, которое фильтрует часть автомобилей")
    void testCountWithFilteringCondition() {
        String input = "filtered\n";
        Scanner scanner = new Scanner(input);
        FilteringStrategy strategy = new FilteringStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(2, result); // Только машины с мощностью > 150
        assertEquals("filtered", strategy.getSearchValue());
    }

    @Test
    @DisplayName("Подсчет с условием, которое не находит совпадений")
    void testCountWithNoMatches() {
        String input = "none\n";
        Scanner scanner = new Scanner(input);
        NoMatchStrategy strategy = new NoMatchStrategy(scanner);

        int result = strategy.count(cars);

        assertEquals(0, result);
        assertEquals("none", strategy.getSearchValue());
    }

    // Вспомогательный класс - стратегия, которая всегда возвращает true
    private static class TestCountStrategy extends BaseCountStrategy {
        private String value;

        public TestCountStrategy(Scanner scanner) {
            super(scanner);
        }

        @Override
        protected String promptUser() {
            System.out.print("Введите значение: ");
            return scanner.nextLine().trim();
        }

        @Override
        protected boolean parseInput(String input) {
            this.value = input;
            return true;
        }

        @Override
        protected Predicate<Car> getCondition() {
            return car -> true;
        }

        @Override
        public String getLabel() {
            return "Тестовая стратегия";
        }

        @Override
        public String getSearchValue() {
            return value;
        }
    }

    // Вспомогательный класс - стратегия с ошибкой парсинга
    private static class FailingParseStrategy extends BaseCountStrategy {
        public FailingParseStrategy(Scanner scanner) {
            super(scanner);
        }

        @Override
        protected String promptUser() {
            System.out.print("Введите значение: ");
            return scanner.nextLine().trim();
        }

        @Override
        protected boolean parseInput(String input) {
            System.out.println("Ошибка парсинга");
            return false;
        }

        @Override
        protected Predicate<Car> getCondition() {
            return car -> true;
        }

        @Override
        public String getLabel() {
            return "Падающая стратегия";
        }

        @Override
        public String getSearchValue() {
            return null;
        }
    }

    // Вспомогательный класс - стратегия с фильтрацией
    private static class FilteringStrategy extends BaseCountStrategy {
        private String value;

        public FilteringStrategy(Scanner scanner) {
            super(scanner);
        }

        @Override
        protected String promptUser() {
            System.out.print("Введите значение: ");
            return scanner.nextLine().trim();
        }

        @Override
        protected boolean parseInput(String input) {
            this.value = input;
            return true;
        }

        @Override
        protected Predicate<Car> getCondition() {
            return car -> car.getPower() > 150;
        }

        @Override
        public String getLabel() {
            return "Фильтрующая стратегия";
        }

        @Override
        public String getSearchValue() {
            return value;
        }
    }

    // Вспомогательный класс - стратегия без совпадений
    private static class NoMatchStrategy extends BaseCountStrategy {
        private String value;

        public NoMatchStrategy(Scanner scanner) {
            super(scanner);
        }

        @Override
        protected String promptUser() {
            System.out.print("Введите значение: ");
            return scanner.nextLine().trim();
        }

        @Override
        protected boolean parseInput(String input) {
            this.value = input;
            return true;
        }

        @Override
        protected Predicate<Car> getCondition() {
            return car -> car.getPower() > 1000;
        }

        @Override
        public String getLabel() {
            return "Без совпадений";
        }

        @Override
        public String getSearchValue() {
            return value;
        }
    }

    private Car createCar(String model, int power, int year) {
        return new Car.Builder()
                .setModel(model)
                .setPower(power)
                .setYear(year)
                .build();
    }
}