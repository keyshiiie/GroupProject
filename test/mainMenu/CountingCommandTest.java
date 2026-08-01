package mainMenu;

import car.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import registry.StrategyRegistry;
import strategy.count.CountStrategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование команды подсчёта CountingCommand")
class CountingCommandTest {

    private CountingCommand command;
    private List<Car> carsStorage;
    private List<String> countResultStorage;
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;

    @BeforeEach
    void setUp() {
        carsStorage = new ArrayList<>();
        countResultStorage = new ArrayList<>();
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
    }

    @Test
    @DisplayName("Команда возвращает корректный текст команды")
    void testGetCommandText() {
        command = new CountingCommand(
                new StrategyRegistry<>(),
                cars -> {},
                printStream,
                carsStorage,
                new Scanner(System.in),
                countResultStorage
        );
        assertEquals("strategy/count", command.getCommandText());
    }

    @Test
    @DisplayName("Команда возвращает корректное описание")
    void testGetUserGuide() {
        command = new CountingCommand(
                new StrategyRegistry<>(),
                cars -> {},
                printStream,
                carsStorage,
                new Scanner(System.in),
                countResultStorage
        );
        assertTrue(command.getUserGuide().contains("Подсчет количества"));
    }

    @Test
    @DisplayName("Подсчёт когда список автомобилей пуст - сообщение об ошибке")
    void testExecuteWhenCarsEmpty() {
        command = new CountingCommand(
                new StrategyRegistry<>(),
                cars -> {},
                printStream,
                carsStorage,
                new Scanner(""),
                countResultStorage
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Нет автомобилей для подсчета"));
    }

    @Test
    @DisplayName("Подсчёт когда нет зарегистрированных стратегий")
    void testExecuteWhenNoStrategies() {
        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                new StrategyRegistry<>(),
                cars -> {},
                printStream,
                carsStorage,
                new Scanner(""),
                countResultStorage
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Нет зарегистрированных стратегий подсчета"));
    }

    @Test
    @DisplayName("Подсчёт с неверным вводом (буквы вместо числа)")
    void testExecuteWithInvalidInput() {
        StrategyRegistry<CountStrategy> registry = new StrategyRegistry<>();
        registry.register(new TestCountStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                registry,
                cars -> {},
                printStream,
                carsStorage,
                new Scanner("abc\n"),
                countResultStorage
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Некорректный ввод. Введите число"));
    }

    @Test
    @DisplayName("Подсчёт с недопустимым номером стратегии (выход за границы)")
    void testExecuteWithInvalidChoice() {
        StrategyRegistry<CountStrategy> registry = new StrategyRegistry<>();
        registry.register(new TestCountStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                registry,
                cars -> {},
                printStream,
                carsStorage,
                new Scanner("99\n"),
                countResultStorage
        );
        command.execute(new String[0]);
        assertTrue(outputStream.toString().contains("Недопустимый номер. Выберите от 1 до "));
    }

    @Test
    @DisplayName("Успешный подсчёт и вызов Consumer (колбэка)")
    void testExecuteSuccessAndCallback() {
        StrategyRegistry<CountStrategy> registry = new StrategyRegistry<>();
        registry.register(new TestCountStrategy()); // Эта стратегия вернёт 2

        carsStorage.add(createCar("Toyota", 150, 2020));
        carsStorage.add(createCar("Toyota", 200, 2021));

        final int[] consumerCallCount = {0};
        Consumer<List<Car>> consumer = cars -> consumerCallCount[0]++;

        command = new CountingCommand(
                registry,
                consumer,
                printStream,
                carsStorage,
                new Scanner("1\n"),
                countResultStorage
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Найдено элементов: 2"));

        assertEquals(1, countResultStorage.size());
        assertTrue(countResultStorage.get(0).contains("Toyota - Найдено: 2"));

        assertEquals(1, consumerCallCount[0]);
    }

    @Test
    @DisplayName("Обработка исключения, которое выбросила стратегия")
    void testExecuteWithExceptionInCount() {
        StrategyRegistry<CountStrategy> registry = new StrategyRegistry<>();
        registry.register(new ThrowingCountStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                registry,
                cars -> {},
                printStream,
                carsStorage,
                new Scanner("1\n"),
                countResultStorage
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Ошибка при подсчете: Ошибка БД!"));
    }

    private static class TestCountStrategy implements CountStrategy {
        @Override
        public int count(List<Car> cars) { return 2; }
        @Override
        public String getLabel() { return "Тестовая стратегия"; }
        @Override
        public String getSearchValue() { return "Toyota"; }
    }

    private static class ThrowingCountStrategy implements CountStrategy {
        @Override
        public int count(List<Car> cars) { throw new RuntimeException("Ошибка БД!"); }
        @Override
        public String getLabel() { return "Падающая стратегия"; }
        @Override
        public String getSearchValue() { return "None"; }
    }

    private Car createCar(String model, int power, int year) {
        return new Car.Builder()
                .setModel(model)
                .setPower(power)
                .setYear(year)
                .build();
    }
}