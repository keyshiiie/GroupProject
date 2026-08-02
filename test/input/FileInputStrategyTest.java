package input;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.Test;
import strategy.input.FileInputStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FileInputStrategyTest {

    private Path createFileInProjectRoot(String content, String fileName) throws Exception {
        Path root = Path.of(System.getProperty("user.dir"));
        Path file = root.resolve(fileName);
        Files.writeString(file, content);
        return file;
    }

    @Test
    void shouldReadCarsFromFileCorrectly() throws Exception {
        String content = "Автомобиль: 200; Mercedes-Benz S-Class; 2021\n" +
                "Автомобиль: 180; BMW 7 Series; 2020\n" +
                "Автомобиль: 220; Audi A8; 2022";
        createFileInProjectRoot(content, "cars.txt");

        Scanner scanner = new Scanner("cars.txt\n2\n");
        FileInputStrategy strategy = new FileInputStrategy(scanner);

        CarList cars = strategy.setCars();

        assertEquals(2, cars.size());
        assertEquals("Mercedes-Benz S-Class", cars.get(0).getModel());
        assertEquals(200, cars.get(0).getPower());
        assertEquals(2021, cars.get(0).getYear());
        assertEquals("BMW 7 Series", cars.get(1).getModel());
        Files.deleteIfExists(Path.of(System.getProperty("user.dir")).resolve("cars.txt"));
    }

    @Test
    void shouldReturnEmptyListOnFileNotFound() {
        Scanner scanner = new Scanner("missing.txt\n5\n");
        FileInputStrategy strategy = new FileInputStrategy(scanner);
        CarList cars = strategy.setCars();
        assertTrue(cars.isEmpty());
    }

    @Test
    void shouldReturnEmptyListOnInvalidSizeInput() throws IOException {
        try {
            createFileInProjectRoot("Автомобиль: 100; Test; 2020", "valid.txt");
        } catch (Exception e) {
            fail("Не удалось создать файл");
        }
        Scanner scanner = new Scanner("valid.txt\nnotANumber\n");
        FileInputStrategy strategy = new FileInputStrategy(scanner);
        CarList cars = strategy.setCars();
        assertTrue(cars.isEmpty());
        Files.deleteIfExists(Path.of(System.getProperty("user.dir")).resolve("valid.txt"));
    }

    @Test
    void shouldThrowExceptionOnWrongFormat() throws Exception {
        createFileInProjectRoot("Wrong format", "bad.txt");
        Scanner scanner = new Scanner("bad.txt\n5\n");
        FileInputStrategy strategy = new FileInputStrategy(scanner);
        CarList cars = strategy.setCars();
        assertTrue(cars.isEmpty());
        Files.deleteIfExists(Path.of(System.getProperty("user.dir")).resolve("bad.txt"));
    }

    @Test
    void getLabelShouldReturnCorrectString() {
        Scanner scanner = new Scanner("");
        FileInputStrategy strategy = new FileInputStrategy(scanner);
        assertEquals("Загрузка автомобилей из файла", strategy.getLabel());
    }
}