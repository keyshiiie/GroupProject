package mainMenu;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import registry.StrategyRegistry;
import strategy.sort.SortStrategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование команды сортировки SortingCommand")
class SortingCommandTest {

    private SortingCommand command;
    private CarList carsStorage;
    private CarList sortedCarsStorage;
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private StrategyRegistry<SortStrategy> registry;

    @BeforeEach
    void setUp() {
        carsStorage = new CarList();
        sortedCarsStorage = new CarList();
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        registry = new StrategyRegistry<>();
    }

    @Test
    @DisplayName("Команда возвращает корректный текст команды")
    void testGetCommandText() {
        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner(System.in)
        );
        assertEquals("sort", command.getCommandText());
    }

    @Test
    @DisplayName("Команда возвращает корректное описание")
    void testGetUserGuide() {
        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner(System.in)
        );
        assertTrue(command.getUserGuide().contains("Сортировка автомобилей"));
    }

    @Test
    @DisplayName("Сортировка когда список автомобилей пуст - сообщение об ошибке")
    void testExecuteWhenCarsEmpty() {
        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("")
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Список автомобилей пуст"));
    }

    @Test
    @DisplayName("Сортировка когда список автомобилей null - сообщение об ошибке")
    void testExecuteWhenCarsNull() {
        command = new SortingCommand(
                registry,
                cars -> {},
                () -> null,
                printStream,
                new Scanner("")
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Список автомобилей пуст"));
    }

    @Test
    @DisplayName("Сортировка когда нет зарегистрированных стратегий")
    void testExecuteWhenNoStrategies() {
        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("")
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Нет зарегистрированных стратегий сортировки"));
    }

    @Test
    @DisplayName("Сортировка с неверным вводом (буквы вместо числа)")
    void testExecuteWithInvalidInput() {
        registry.register(new TestSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("abc\n")
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Некорректный ввод"));
    }

    @Test
    @DisplayName("Сортировка с недопустимым номером стратегии (выход за границы)")
    void testExecuteWithInvalidChoice() {
        registry.register(new TestSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("99\n")
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Недопустимый номер"));
    }

    @Test
    @DisplayName("Сортировка с пустым вводом (отмена)")
    void testExecuteWithEmptyInput() {
        registry.register(new TestSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("\n")
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Выбор отменён"));
    }

    @Test
    @DisplayName("Сортировка с вводом только пробелов (отмена)")
    void testExecuteWithWhitespaceInput() {
        registry.register(new TestSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("   \n")
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Выбор отменён"));
    }

    @Test
    @DisplayName("Успешная сортировка и вызов Consumer (колбэка)")
    void testExecuteSuccessAndCallback() {
        registry.register(new TestSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));
        carsStorage.add(createCar("BMW", 200, 2021));

        final int[] consumerCallCount = {0};
        Consumer<List<Car>> consumer = sortedCars -> {
            consumerCallCount[0]++;
            sortedCarsStorage.addAll(sortedCars);
        };

        command = new SortingCommand(
                registry,
                consumer,
                () -> carsStorage,
                printStream,
                new Scanner("1\n")
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Выбрана стратегия: Тестовая сортировка"));
        assertTrue(output.contains("Сортировка выполнена"));

        assertEquals(1, consumerCallCount[0]);
        assertEquals(2, sortedCarsStorage.size());
    }

    @Test
    @DisplayName("Обработка исключения, которое выбросила стратегия")
    void testExecuteWithExceptionInSort() {
        registry.register(new ThrowingSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("1\n")
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Ошибка сортировки: Ошибка сортировки!"));
    }

    @Test
    @DisplayName("Сортировка с несколькими зарегистрированными стратегиями")
    void testExecuteWithMultipleStrategies() {
        registry.register(new TestSortStrategy());
        registry.register(new AnotherSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));
        carsStorage.add(createCar("BMW", 200, 2021));

        command = new SortingCommand(
                registry,
                cars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("2\n")
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("1. Тестовая сортировка"));
        assertTrue(output.contains("2. Другая сортировка"));
        assertTrue(output.contains("Выбрана стратегия: Другая сортировка"));
    }

    @Test
    @DisplayName("Проверка что исходный список не изменяется при сортировке")
    void testOriginalListNotModified() {
        registry.register(new TestSortStrategy());

        Car car1 = createCar("Toyota", 150, 2020);
        Car car2 = createCar("BMW", 200, 2021);
        carsStorage.add(car1);
        carsStorage.add(car2);

        CarList originalListCopy = new CarList();
        originalListCopy.addAll(carsStorage);

        command = new SortingCommand(
                registry,
                sortedCars -> {},
                () -> carsStorage,
                printStream,
                new Scanner("1\n")
        );
        command.execute(new String[0]);

        assertEquals(originalListCopy.size(), carsStorage.size());
        assertTrue(carsStorage.containsAll(originalListCopy));
    }

    @Test
    @DisplayName("Сортировка успешно выполняется со списком")
    void testExecuteSuccess() {
        registry.register(new TestSortStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));
        carsStorage.add(createCar("BMW", 200, 2021));

        final int[] consumerCallCount = {0};
        Consumer<List<Car>> consumer = sortedCars -> {
            consumerCallCount[0]++;
            sortedCarsStorage.addAll(sortedCars);
        };

        command = new SortingCommand(
                registry,
                consumer,
                () -> carsStorage,
                printStream,
                new Scanner("1\n")
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Выбрана стратегия: Тестовая сортировка"));
        assertTrue(output.contains("Сортировка выполнена"));

        assertEquals(1, consumerCallCount[0]);
        assertEquals(2, sortedCarsStorage.size());

        assertTrue(sortedCarsStorage.stream().anyMatch(c -> c.getModel().equals("Toyota")));
        assertTrue(sortedCarsStorage.stream().anyMatch(c -> c.getModel().equals("BMW")));
    }

    private static class TestSortStrategy implements SortStrategy {
        @Override
        public List<Car> sort(CarList cars) {
            List<Car> result = new ArrayList<>();
            for (int i = 0; i < cars.size(); i++) {
                result.add(cars.get(i));
            }
            return result;
        }

        @Override
        public String getLabel() {
            return "Тестовая сортировка";
        }
    }

    private static class AnotherSortStrategy implements SortStrategy {
        @Override
        public List<Car> sort(CarList cars) {
            List<Car> result = new ArrayList<>();
            for (int i = 0; i < cars.size(); i++) {
                result.add(cars.get(i));
            }
            return result;
        }

        @Override
        public String getLabel() {
            return "Другая сортировка";
        }
    }

    private static class ThrowingSortStrategy implements SortStrategy {
        @Override
        public List<Car> sort(CarList cars) {
            throw new RuntimeException("Ошибка сортировки!");
        }

        @Override
        public String getLabel() {
            return "Падающая сортировка";
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