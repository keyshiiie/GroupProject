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
        assertTrue(output.contains("Модель: ModelA, Год: 2020, Мощность: 150 л.с."));
        assertTrue(output.contains("Модель: ModelB, Год: 2018, Мощность: 200 л.с."));
    }
}