package mainMenu;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.input.ConsoleInputStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputNewCarsCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;

    private ConsoleInputStrategy handler;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
        outContent.reset();
        errContent.reset();
    }

    private void prepareHandler(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        handler = new ConsoleInputStrategy(new Scanner(System.in));
    }


    @Test
    void setCarsValidInputReturnsCarList() {
        String input = "2\n" +
                "Toyota\n" +
                "2020\n" +
                "150\n" +
                "Honda\n" +
                "2018\n" +
                "200\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertEquals(2, result.size());
        Car car1 = result.get(0);
        assertEquals("Toyota", car1.getModel());
        assertEquals(2020, car1.getYear());
        assertEquals(150, car1.getPower());

        Car car2 = result.get(1);
        assertEquals("Honda", car2.getModel());
        assertEquals(2018, car2.getYear());
        assertEquals(200, car2.getPower());

        String output = outContent.toString();
        assertTrue(output.contains("Введите количество автомобилей которые хотите добавить:"));
        assertTrue(output.contains("--- Ввод автомобиля №1 ---"));
        assertTrue(output.contains("--- Ввод автомобиля №2 ---"));
    }

    @Test
    void setCarsZeroSizeReturnsEmptyAndPrintsError() {
        String input = "0\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(errContent.toString().contains("Размер должен быть больше 0."));
    }

    @Test
    void setCarsNegativeSizeReturnsEmptyAndPrintsError() {
        String input = "-5\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(errContent.toString().contains("Размер должен быть больше 0."));
    }

    @Test
    void setCarsInvalidNumberFormatReturnsEmptyAndPrintsError() {
        String input = "abc\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(errContent.toString().contains("Ошибка: введите корректное число!"));
    }

    @Test
    void setCarsCancelFirstCarReturnsEmpty() {
        String input = "1\n" +
                "\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(outContent.toString().contains("Ввод отменен."));
    }

    @Test
    void setCarsCancelAfterFirstCarReturnsFirstCar() {
        String input = "2\n" +
                "Toyota\n" +
                "2020\n" +
                "150\n" +
                "\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getModel());
        assertTrue(outContent.toString().contains("Ввод отменен."));
    }

    @Test
    void setCarsInvalidYear() {
        String input = "1\n" +
                "BMW\n" +
                "abc\n" +
                "180\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void setCarsInvalidPower() {
        String input = "1\n" +
                "Audi\n" +
                "2019\n" +
                "xbr\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertEquals(0, result.size());
    }


    @Test
    void setCarsCancelAtYearInputReturnsPreviousCars() {
        String input = "2\n" +
                "Toyota\n" +
                "2020\n" +
                "150\n" +
                "Honda\n" +
                "\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getModel());
        assertTrue(outContent.toString().contains("Ввод отменен."));
    }

    @Test
    void setCarsCancelAtPowerInputReturnsPreviousCars() {
        String input = "2\n" +
                "Toyota\n" +
                "2020\n" +
                "150\n" +
                "Honda\n" +
                "2019\n" +
                "\n";
        prepareHandler(input);

        CarList result = handler.setCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getModel());
        assertTrue(outContent.toString().contains("Ввод отменен."));
    }
}
