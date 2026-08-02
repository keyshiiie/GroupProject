package mainMenu;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import registry.StrategyRegistry;
import strategy.count.CountStrategy;
import utils.StringList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование команды подсчёта CountingCommand")
class CountingCommandTest {

    private CountingCommand command;
    private CarList carsStorage;
    private StringList countResultStorage;
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private StrategyRegistry<CountStrategy> registry;

    @BeforeEach
    void setUp() {
        carsStorage = new CarList();
        countResultStorage = new StringList();
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        registry = new StrategyRegistry<>();
    }

    @Test
    @DisplayName("Команда возвращает корректный текст команды")
    void testGetCommandText() {
        command = new CountingCommand(
                registry,
                printStream,
                carsStorage,
                new Scanner(System.in),
                countResultStorage
        );
        assertEquals("count", command.getCommandText());
    }

    @Test
    @DisplayName("Команда возвращает корректное описание")
    void testGetUserGuide() {
        command = new CountingCommand(
                registry,
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
                registry,
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
                registry,
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
        // РЕГИСТРИРУЕМ стратегию
        registry.register(new TestCountStrategy());
        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                registry,
                printStream,
                carsStorage,
                new Scanner("abc\n"),
                countResultStorage
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Некорректный ввод. Введите число"));
    }

    @Test
    @DisplayName("Подсчёт с недопустимым номером стратегии (выход за границы)")
    void testExecuteWithInvalidChoice() {
        registry.register(new TestCountStrategy());
        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                registry,
                printStream,
                carsStorage,
                new Scanner("99\n"),
                countResultStorage
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Недопустимый номер. Выберите от 1 до "));
    }

    @Test
    @DisplayName("Успешный подсчёт и сохранение результата")
    void testExecuteSuccess() {
        // РЕГИСТРИРУЕМ стратегию
        registry.register(new TestCountStrategy());

        carsStorage.add(createCar("Toyota", 150, 2020));
        carsStorage.add(createCar("Toyota", 200, 2021));

        command = new CountingCommand(
                registry,
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
    }

    @Test
    @DisplayName("Обработка исключения, которое выбросила стратегия")
    void testExecuteWithExceptionInCount() {
        registry.register(new ThrowingCountStrategy());
        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                registry,
                printStream,
                carsStorage,
                new Scanner("1\n"),  // ← Выбираем стратегию номер 1
                countResultStorage
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Ошибка при подсчете: Ошибка БД!"));
    }

    @Test
    @DisplayName("Выбор стратегии с пустым вводом (Enter)")
    void testExecuteWithEmptyChoice() {
        registry.register(new TestCountStrategy());
        carsStorage.add(createCar("Toyota", 150, 2020));

        command = new CountingCommand(
                registry,
                printStream,
                carsStorage,
                new Scanner("\n"),
                countResultStorage
        );
        command.execute(new String[0]);

        String output = outputStream.toString();
        assertTrue(output.contains("Выбор отменён"));
    }

    @Test
    @DisplayName("Подсчёт по мощности с реальной стратегией")
    void testExecuteWithRealStrategy() {
        registry.register(new CountByPowerTestStrategy());

        carsStorage.add(createCar("BMW", 200, 2020));
        carsStorage.add(createCar("Audi", 200, 2021));
        carsStorage.add(createCar("Mercedes", 300, 2022));

        command = new CountingCommand(
                registry,
                printStream,
                carsStorage,
                new Scanner("1\n"),
                countResultStorage
        );
        command.execute(new String[0]);

        String output = outputStream.toString();

        assertTrue(output.contains("Найдено элементов: 2"),
                "Ожидалось сообщение о найденных элементах");

        // Проверяем, что результат сохранился в хранилище
        assertEquals(1, countResultStorage.size());
        assertTrue(countResultStorage.get(0).contains("200 - Найдено: 2"),
                "Результат должен содержать мощность и количество");
    }

    private static class TestCountStrategy implements CountStrategy {
        @Override
        public int count(CarList cars) {
            return 2;
        }

        @Override
        public String getLabel() {
            return "Тестовая стратегия";
        }

        @Override
        public String getSearchValue() {
            return "Toyota";
        }
    }

    private static class ThrowingCountStrategy implements CountStrategy {
        @Override
        public int count(CarList cars) {
            throw new RuntimeException("Ошибка БД!");
        }

        @Override
        public String getLabel() {
            return "Падающая стратегия";
        }

        @Override
        public String getSearchValue() {
            return "None";
        }
    }

    private static class CountByPowerTestStrategy implements CountStrategy {
        @Override
        public int count(CarList cars) {
            int targetPower = 200;
            int count = 0;
            for (int i = 0; i < cars.size(); i++) {
                if (cars.get(i).getPower() == targetPower) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public String getLabel() {
            return "Подсчет по мощности";
        }

        @Override
        public String getSearchValue() {
            return "200";
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