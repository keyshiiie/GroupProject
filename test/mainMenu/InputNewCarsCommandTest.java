package mainMenu;
import car.Car;
import car.CarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import registry.StrategyRegistry;
import strategy.input.InputStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class InputNewCarsCommandTest {

    private ByteArrayOutputStream baos;
    private PrintStream out;

    private StrategyRegistry<InputStrategy> registry;

    private CapturingConsumer consumer;

    private InputNewCarsCommand command;

    static class TestInputStrategy implements InputStrategy {
        private final String label;
        private final CarList result;
        private final RuntimeException exception;

        public TestInputStrategy(String label, CarList result) {
            this(label, result, null);
        }

        public TestInputStrategy(String label, RuntimeException exception) {
            this(label, null, exception);
        }

        private TestInputStrategy(String label, CarList result, RuntimeException exception) {
            this.label = label;
            this.result = result;
            this.exception = exception;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public CarList setCars() {
            if (exception != null) {
                throw exception;
            }
            return result != null ? result : new CarList();
        }
    }


    static class CapturingConsumer implements Consumer<CarList> {
        private CarList captured;

        @Override
        public void accept(CarList carList) {
            this.captured = carList;
        }

        public CarList getCaptured() {
            return captured;
        }

        public void reset() {
            captured = null;
        }
    }



    @BeforeEach
    void setUp() {
        baos = new ByteArrayOutputStream();
        out = new PrintStream(baos);
        registry = new StrategyRegistry<>();
        consumer = new CapturingConsumer();
    }


    private void createCommand(String input) {
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(bais);
        command = new InputNewCarsCommand(registry, consumer, out, scanner);
    }

    @Test
    void executeNoStrategiesPrintsNoStrategies() {
        createCommand("");
        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Нет зарегистрированных стратегий ввода."));
        assertNull(consumer.getCaptured());
    }

    @Test
    void executeUserCancelsWithEmptyLinePrintsCancel() {
        CarList dummy = new CarList();
        registry.register(new TestInputStrategy("Console", dummy));
        registry.register(new TestInputStrategy("File", dummy));
        createCommand("\n"); // пустая строка

        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Выберите источник данных:"));
        assertTrue(output.contains("1. Console"));
        assertTrue(output.contains("2. File"));
        assertTrue(output.contains("Выбор отменён."));
        assertNull(consumer.getCaptured());
    }

    @Test
    void executeUserCancelsWithExitPrintsCancel() {
        registry.register(new TestInputStrategy("Console", new CarList()));
        createCommand("exit\n");

        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Выбор отменён."));
        assertNull(consumer.getCaptured());
    }

    @Test
    void executeInvalidNumberFormatPrintsInvalid() {
        registry.register(new TestInputStrategy("Console", new CarList()));
        registry.register(new TestInputStrategy("File", new CarList()));
        createCommand("abc\n");

        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Некорректный ввод."));
        assertNull(consumer.getCaptured());
    }

    @Test
    void executeNumberOutOfRangeLowPrintsInvalidNumber() {
        registry.register(new TestInputStrategy("Console", new CarList()));
        registry.register(new TestInputStrategy("File", new CarList()));
        createCommand("0\n");

        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Недопустимый номер."));
        assertNull(consumer.getCaptured());
    }

    @Test
    void executeNumberOutOfRangeHighPrintsInvalidNumber() {
        registry.register(new TestInputStrategy("Console", new CarList()));
        registry.register(new TestInputStrategy("File", new CarList()));
        createCommand("3\n");

        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Недопустимый номер."));
        assertNull(consumer.getCaptured());
    }

    @Test
    void executeStrategyThrowsExceptionPrintsError() {
        registry.register(new TestInputStrategy("Console", new RuntimeException("Test exception")));
        registry.register(new TestInputStrategy("File", new CarList()));
        createCommand("1\n");

        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Выбран источник: Console"));
        assertTrue(output.contains("Ошибка при получении данных: Test exception"));
        assertNull(consumer.getCaptured());
    }

    @Test
    void executeSuccessfulSelectionAcceptsCars() {
        CarList cars = new CarList();
        cars.add(new Car.Builder().setModel("Test").setYear(2020).setPower(100).build());

        registry.register(new TestInputStrategy("Console", new CarList()));
        registry.register(new TestInputStrategy("File", cars));
        createCommand("2\n");

        command.execute(new String[0]);

        String output = baos.toString();
        assertTrue(output.contains("Выберите источник данных:"));
        assertTrue(output.contains("1. Console"));
        assertTrue(output.contains("2. File"));
        assertTrue(output.contains("Выбран источник: File"));
        assertTrue(output.contains("Загружено автомобилей: 1"));

        CarList captured = consumer.getCaptured();
        assertNotNull(captured);
        assertEquals(1, captured.size());
        Car car = captured.get(0);
        assertEquals("Test", car.getModel());
        assertEquals(2020, car.getYear());
        assertEquals(100, car.getPower());
    }

    @Test
    void getCommandTextReturnsInput() {
        createCommand("");
        assertEquals("input", command.getCommandText());
    }
}