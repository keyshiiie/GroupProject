package mainMenu;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import registry.StrategyRegistry;
import strategy.export.ExportStrategy;
import utils.StringList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ExportCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream inContent;

    private CarList carsStorage;
    private StringList countResultStorage;
    private Scanner scanner;
    private StrategyRegistry<ExportStrategy> strategyRegistry;
    private ExportCommand exportCommand;

    private static class TestExportStrategy implements ExportStrategy {
        private final String label;
        private Object exportedData;
        private String exportedFileName;
        private boolean exportCalled;

        public TestExportStrategy(String label) {
            this.label = label;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public void export(String fileName, Collection<?> data) {
            this.exportedFileName = fileName;
            this.exportedData = data;
            this.exportCalled = true;
        }

        public boolean isExportCalled() {
            return exportCalled;
        }

        public Object getExportedData() {
            return exportedData;
        }

        public String getExportedFileName() {
            return exportedFileName;
        }

        public void reset() {
            exportCalled = false;
            exportedData = null;
            exportedFileName = null;
        }
    }

    // Исправленная стратегия, которая выбрасывает исключение
    private static class FailingExportStrategy implements ExportStrategy {
        private final String label;

        public FailingExportStrategy(String label) {
            this.label = label;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public void export(String fileName, Collection<?> data) {
            throw new RuntimeException("Test exception");
        }
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));

        carsStorage = new CarList();
        countResultStorage = new StringList();
        strategyRegistry = new StrategyRegistry<>();
        scanner = new Scanner(System.in);

        carsStorage.add(createCar("Toyota Camry", 2020, 200));
        carsStorage.add(createCar("BMW 3 Series", 2021, 300));
        carsStorage.add(createCar("Audi A4", 2022, 250));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
        if (inContent != null) {
            try {
                inContent.reset();
            } catch (Exception e) {
                // ignore
            }
        }
        if (scanner != null) {
            scanner.close();
        }
    }

    private Car createCar(String model, int year, int power) {
        return new Car.Builder()
                .setModel(model)
                .setYear(year)
                .setPower(power)
                .build();
    }

    private void provideInput(String data) {
        inContent = new ByteArrayInputStream(data.getBytes());
        System.setIn(inContent);
        scanner = new Scanner(System.in);
    }

    private void initCommand() {
        exportCommand = new ExportCommand(
                strategyRegistry,
                carsStorage,
                countResultStorage,
                scanner,
                System.out
        );
    }

    private void createUnsortedList() {
        carsStorage.clear();
        carsStorage.add(createCar("Toyota Camry", 2021, 150));
        carsStorage.add(createCar("BMW 3 Series", 2020, 300));
        carsStorage.add(createCar("Audi A4", 2022, 200));
    }

    @Test
    void getCommandText_ShouldReturnExport() {
        initCommand();
        assertEquals("export", exportCommand.getCommandText());
    }

    @Test
    void getUserGuide_ShouldReturnGuide() {
        initCommand();
        assertTrue(exportCommand.getUserGuide().contains("Вывод списка автомобилей в файл"));
    }

    @Test
    void execute_ShouldShowMessage_WhenCarsStorageIsEmpty() {
        provideInput("test.txt\n1\n");
        carsStorage.clear();
        initCommand();

        exportCommand.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("Массив пуст."));
    }

    @Test
    void execute_ShouldShowMessage_WhenNoStrategies() {
        provideInput("test.txt\n");
        initCommand();

        exportCommand.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("Нет зарегистрированных видов экспорта."));
    }

    @Test
    void execute_ShouldCancel_WhenFileNameIsNull() {
        provideInput("отмена\n");
        initCommand();

        exportCommand.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("Выбор отменён."));
    }

    @Test
    void execute_ShouldSelectExportStrategyAndExport_WhenValidInput() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertTrue(testStrategy.isExportCalled());
        assertEquals("test.txt", testStrategy.getExportedFileName());
        assertEquals(carsStorage, testStrategy.getExportedData());

        String output = outContent.toString();
        assertTrue(output.contains("Экспорт завершён успешно!"));
    }

    @Test
    void execute_ShouldCancelExport_WhenInvalidStrategyNumber() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n0\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Недопустимый номер."));
    }

    @Test
    void execute_ShouldCancelExport_WhenInvalidStrategyInput() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\nabc\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Некорректный ввод. Введите число."));
    }

    @Test
    void execute_ShouldHandleExportException() {
        FailingExportStrategy failingStrategy = new FailingExportStrategy("Список машин");
        strategyRegistry.register(failingStrategy);
        provideInput("test.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("Ошибка экспорта: Test exception"));
    }

    @Test
    void execute_ShouldExportCountResultStorage_WhenSelected() {
        countResultStorage.add("Result 1");
        countResultStorage.add("Result 2");

        TestExportStrategy testStrategy = new TestExportStrategy("Результаты поиска");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertTrue(testStrategy.isExportCalled());
        assertEquals("test.txt", testStrategy.getExportedFileName());
        assertEquals(countResultStorage, testStrategy.getExportedData());
    }

    @Test
    void execute_ShouldShowMessage_WhenCountResultStorageIsEmpty() {
        TestExportStrategy testStrategy = new TestExportStrategy("Результаты поиска");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Нет результатов поиска для сохранения."));
    }

    @Test
    void execute_ShouldConfirmExport_WhenArrayNotSortedAndUserConfirms() {
        createUnsortedList();

        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertTrue(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Массив еще не был отсортирован."));
        assertTrue(output.contains("Вы точно хотите сохранить его в файл?"));
        assertTrue(output.contains("Экспорт завершён успешно!"));
    }

    @Test
    void execute_ShouldCancelExport_WhenArrayNotSortedAndUserCancels() {
        createUnsortedList();

        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n2\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Массив еще не был отсортирован."));
        assertTrue(output.contains("Вы точно хотите сохранить его в файл?"));
        assertTrue(output.contains("Экспорт отменён."));
    }

    @Test
    void execute_ShouldExportSortedArray_WithoutConfirmation() {
        carsStorage.clear();
        carsStorage.add(createCar("Audi A4", 2022, 250));
        carsStorage.add(createCar("BMW 3 Series", 2021, 300));
        carsStorage.add(createCar("Toyota Camry", 2020, 200));

        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertTrue(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Массив отсортирован."));
        assertFalse(output.contains("Вы точно хотите сохранить его в файл?"));
        assertTrue(output.contains("Экспорт завершён успешно!"));
    }

    @Test
    void execute_ShouldHandleBlankFileName() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("отмена\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Выбор отменён."));
    }

    @Test
    void execute_ShouldHandleInvalidChoiceInConfirmation() {
        createUnsortedList();

        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\nabc\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Некорректный ввод. Введите 1 или 2."));
    }

    @Test
    void execute_ShouldHandleUnknownExportLabel() {
        TestExportStrategy testStrategy = new TestExportStrategy("Неизвестный тип");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Неизвестный тип данных для экспорта."));
    }

    @Test
    void execute_ShouldHandleBlankLineInStrategySelection() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Выбор отменён."));
    }

    @Test
    void execute_ShouldHandleMultipleStrategies() {
        TestExportStrategy strategy1 = new TestExportStrategy("Список машин");
        TestExportStrategy strategy2 = new TestExportStrategy("Результаты поиска");
        strategyRegistry.register(strategy1);
        strategyRegistry.register(strategy2);

        countResultStorage.add("Result 1");
        provideInput("test.txt\n2\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(strategy1.isExportCalled());
        assertTrue(strategy2.isExportCalled());
        assertEquals(countResultStorage, strategy2.getExportedData());

        String output = outContent.toString();
        assertTrue(output.contains("1. Список машин"));
        assertTrue(output.contains("2. Результаты поиска"));
    }

    @Test
    void execute_ShouldHandleBlankResponseInConfirmation() {
        createUnsortedList();

        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Выбор отменён."));
    }

    @Test
    void execute_ShouldHandleFileNameWithSpaces() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("my test file.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertTrue(testStrategy.isExportCalled());
        assertEquals("my test file.txt", testStrategy.getExportedFileName());
    }

    @Test
    void execute_ShouldHandleCancelInConfirmationWithBlankResponse() {
        createUnsortedList();

        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n   \n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Выбор отменён."));
    }

    @Test
    void execute_ShouldHandleMultipleInvalidFileNameAttempts() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("invalid?.txt\ninvalid|.txt\ntest.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertTrue(testStrategy.isExportCalled());
        assertEquals("test.txt", testStrategy.getExportedFileName());
        String output = outContent.toString();
        assertTrue(output.contains("Ошибка:"));
        assertTrue(output.contains("Имя файла принято: test.txt"));
    }

    @Test
    void execute_ShouldHandleCaseInsensitiveCancel() {
        provideInput("ОТМЕНА\n");
        initCommand();

        exportCommand.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("Выбор отменён."));
    }

    @Test
    void execute_ShouldHandleStrategySelectionWithExtraSpaces() {
        TestExportStrategy testStrategy = new TestExportStrategy("Список машин");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n 1 \n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertTrue(testStrategy.isExportCalled());
        assertEquals("test.txt", testStrategy.getExportedFileName());
    }

    @Test
    void execute_ShouldNotCallExport_WhenDataIsNull() {
        TestExportStrategy testStrategy = new TestExportStrategy("Неизвестный тип");
        strategyRegistry.register(testStrategy);
        provideInput("test.txt\n1\n");
        initCommand();

        exportCommand.execute(new String[0]);

        assertFalse(testStrategy.isExportCalled());
        String output = outContent.toString();
        assertTrue(output.contains("Неизвестный тип данных для экспорта."));
    }
}