package mainMenu;

import car.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import registry.StrategyRegistry;
import strategy.sort.SortStrategy;
import strategy.NamedStrategy;

import java.io.StringReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class SortingCommandTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    private StrategyRegistry<SortStrategy> sortRegistry;

    private SortStrategy sortStrategy;

    private boolean onSortedCalled = false;
    private List<Car> lastSortedList = null;

    @BeforeEach
    void setup() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        sortRegistry = new StrategyRegistry<>();

        sortStrategy = new SortStrategy() {
            @Override
            public String getLabel() {
                return "By Year";
            }

            @Override
            public Comparator<Car> getComparator() {
                return null;
            }

            @Override
            public List<Car> sort(List<Car> input) {
                List<Car> result = new ArrayList<>(input);

                return List.of(
                        new Car.Builder().setModel("BBBBB").setYear(2019).setPower(120).build(),
                        new Car.Builder().setModel("AAAAA").setYear(2020).setPower(100).build()
                );
            }
        };

        sortRegistry.register(sortStrategy);

        Supplier<List<Car>> currentCarsSupplier = () -> {
            return List.of(
                    new Car.Builder().setModel("AAAAA").setYear(2020).setPower(100).build(),
                    new Car.Builder().setModel("BBBBB").setYear(2019).setPower(120).build()
            );
        };

        Consumer<List<Car>> onSorted = cars -> {
            onSortedCalled = true;
            lastSortedList = cars;
        };

        this.currentCarsSupplierFunc = currentCarsSupplier;
        this.onSortedFunc = onSorted;
    }

    private Supplier<List<Car>> currentCarsSupplierFunc;
    private Consumer<List<Car>> onSortedFunc;

    @AfterEach
    void teardown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("При выполнении команды сортировки: стратегия применяется и уведомление срабатывает")
    void execute_sortsAndNotifies() {
        // Создаем Scanner из строки "1" (выбор первой стратегии)
        Scanner scanner = new Scanner(new StringReader("1\n"));

        // Создаем команду, передавая наши реальные объекты и заглушки
        var command = new SortingCommand(
                sortRegistry,
                onSortedFunc,
                currentCarsSupplierFunc,
                System.out,
                scanner
        );

        command.execute(new String[0]);

        assertTrue(onSortedCalled, "Метод onSorted не был вызван");

        assertNotNull(lastSortedList);
        assertEquals(2, lastSortedList.size());
    }

    @Test
    @DisplayName("При вводе неверного номера стратегии: сортировка не выполняется, уведомление не приходит")
    void execute_invalidChoice_doesNothing() {
        Scanner scanner = new Scanner(new StringReader("99\n")); // неверный выбор

        var command = new SortingCommand(
                sortRegistry,
                onSortedFunc,
                currentCarsSupplierFunc,
                System.out,
                scanner
        );

        command.execute(new String[0]);

        assertFalse(onSortedCalled, "При неверном выборе уведомление не должно вызываться");
        assertNull(lastSortedList, "Список отсортированных авто не должен быть сохранён");
    }
}
