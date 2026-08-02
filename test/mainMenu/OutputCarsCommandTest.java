package mainMenu;

import car.Car;
import car.CarList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OutputCarsCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void execute_withEmptyList_printsMessage() {
        var command = new OutputCarsCommand(new CarList());
        command.execute(new String[0]);

        assertTrue(outContent.toString().contains("Список автомобилей пуст."));
    }

    @Test
    void execute_printsCars() {
        CarList cars = new CarList();
        cars.add(new Car.Builder().setModel("ModelA").setYear(2020).setPower(150).build());
        cars.add(new Car.Builder().setModel("ModelB").setYear(2018).setPower(200).build());

        var command = new OutputCarsCommand(cars);
        command.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("--- Список автомобилей ---"));
        // Исправлено: проверяем фактический формат toString()
        assertTrue(output.contains("Мощность: 150"));
        assertTrue(output.contains("Модель: 'ModelA'"));
        assertTrue(output.contains("Год выпуска: 2020"));
        assertTrue(output.contains("Мощность: 200"));
        assertTrue(output.contains("Модель: 'ModelB'"));
        assertTrue(output.contains("Год выпуска: 2018"));
        assertTrue(output.contains("--------------------------"));
    }

    @Test
    void execute_printsCarsWithIndexes() {
        CarList cars = new CarList();
        cars.add(new Car.Builder().setModel("ModelA").setYear(2020).setPower(150).build());
        cars.add(new Car.Builder().setModel("ModelB").setYear(2018).setPower(200).build());

        var command = new OutputCarsCommand(cars);
        command.execute(new String[0]);

        String output = outContent.toString();
        // Проверяем, что индексы отображаются
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
    }

    @Test
    void execute_withSingleCar() {
        CarList cars = new CarList();
        cars.add(new Car.Builder().setModel("Test Model").setYear(2022).setPower(100).build());

        var command = new OutputCarsCommand(cars);
        command.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("--- Список автомобилей ---"));
        assertTrue(output.contains("1."));
        assertTrue(output.contains("Test Model"));
        assertTrue(output.contains("2022"));
        assertTrue(output.contains("100"));
        assertTrue(output.contains("--------------------------"));
    }

    @Test
    void execute_withMultipleCars() {
        CarList cars = new CarList();
        for (int i = 1; i <= 5; i++) {
            cars.add(new Car.Builder()
                    .setModel("Car" + i)
                    .setYear(2020 + i)
                    .setPower(100 + i * 10)
                    .build());
        }

        var command = new OutputCarsCommand(cars);
        command.execute(new String[0]);

        String output = outContent.toString();
        for (int i = 1; i <= 5; i++) {
            assertTrue(output.contains(i + "."));
            assertTrue(output.contains("Car" + i));
        }
    }

    @Test
    void getCommandText_shouldReturnShow() {
        var command = new OutputCarsCommand(new CarList());
        assertEquals("show", command.getCommandText());
    }

    @Test
    void getUserGuide_shouldReturnGuide() {
        var command = new OutputCarsCommand(new CarList());
        assertTrue(command.getUserGuide().contains("вывод списка автомобилей"));
    }
}