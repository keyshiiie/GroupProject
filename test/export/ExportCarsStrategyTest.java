package export;

import car.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import strategy.export.ExportCarsStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportCarsStrategyTest {

    private ExportCarsStrategy strategy;
    private List<Car> cars;
    private String testFileName;
    private Path testFilePath;
    private String testFileNameWithPath;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        strategy = new ExportCarsStrategy();
        cars = new ArrayList<>();

        testFileName = "test_cars";
        testFilePath = tempDir.resolve(testFileName + ".txt");
        testFileNameWithPath = testFilePath.toString();

    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(testFilePath)) {
            Files.delete(testFilePath);
        }
    }

    private Car createCar(String model, int year, int power) {
        return new Car.Builder()
                .setModel(model)
                .setYear(year)
                .setPower(power)
                .build();
    }

    @Test
    void getLabel_ShouldReturnCorrectLabel() {
        assertEquals("Список машин", strategy.getLabel());
    }

    @Test
    void export_ShouldWriteCarsToFile_WhenDataIsValid() throws IOException {

        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("BMW 3 Series", 2021, 300));
        cars.add(createCar("Audi A4", 2022, 250));


        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан: " + testFilePath);
        List<String> lines = Files.readAllLines(testFilePath);
        assertFalse(lines.isEmpty(), "Файл не должен быть пустым");
        assertEquals(3, lines.size(), "Должно быть 3 строки");

        String content = String.join(" ", lines);
        assertTrue(content.contains("Toyota Camry"));
        assertTrue(content.contains("BMW 3 Series"));
        assertTrue(content.contains("Audi A4"));
    }

    @Test
    void export_ShouldThrowException_WhenDataIsEmpty() {
        List<Car> emptyList = new ArrayList<>();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> strategy.export(testFilePath.toString(), emptyList)
        );

        assertEquals("Ожидается список машин", exception.getMessage());
    }

    @Test
    void export_ShouldThrowException_WhenDataDoesNotContainCars() {
        List<String> invalidData = Arrays.asList("String", "not car");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> strategy.export(testFilePath.toString(), invalidData)
        );

        assertEquals("Ожидается список машин", exception.getMessage());
    }

    @Test
    void export_ShouldThrowNullPointerException_WhenDataIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> strategy.export(testFilePath.toString(), null)
        );
    }

    @Test
    void export_ShouldHandleSingleCar() throws IOException {
        cars.add(createCar("Honda Civic", 2019, 180));

        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан");
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(1, lines.size(), "Должна быть 1 строка");
        assertTrue(lines.get(0).contains("Honda Civic"));
    }

    @Test
    void export_ShouldHandleManyCars() throws IOException {
        for (int i = 0; i < 100; i++) {
            cars.add(createCar("Car" + i, 2000 + i % 20, 100 + i % 50 + 1));
        }

        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан");
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(100, lines.size(), "Должно быть 100 строк");
    }

    @Test
    void export_ShouldThrowRuntimeException_WhenFileNameIsInvalid() {
        String invalidFileName = "/invalid/path/to/file";
        cars.add(createCar("Toyota", 2020, 200));

        assertThrows(
                RuntimeException.class,
                () -> strategy.export(invalidFileName, cars)
        );
    }

    @Test
    void export_ShouldNotModifyOriginalList() throws IOException {
        cars.add(createCar("Toyota", 2020, 200));
        List<Car> originalCopy = new ArrayList<>(cars);

        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertEquals(originalCopy, cars);
    }

    @Test
    void export_ShouldHandleCarsWithSameModel() throws IOException {
        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("Toyota Camry", 2021, 250));
        cars.add(createCar("Toyota Camry", 2022, 300));

        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан");
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(3, lines.size(), "Должно быть 3 строки");

        String content = String.join(" ", lines);
        assertTrue(content.contains("2020"));
        assertTrue(content.contains("2021"));
        assertTrue(content.contains("2022"));
    }

    @Test
    void export_ShouldHandleCarsWithMinimalPower() throws IOException {
        cars.add(createCar("Test", 2000, 1));

        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан");
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(1, lines.size(), "Должна быть 1 строка");
        assertTrue(lines.get(0).contains("1"));
    }

    @Test
    void export_ShouldHandleCarsWithMaxValues() throws IOException {
        cars.add(createCar("Test", 2022, 1000));

        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан");
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(1, lines.size(), "Должна быть 1 строка");
        assertTrue(lines.get(0).contains("1000"));
    }

    @Test
    void export_ShouldHandleCarsWithSpecialCharactersInModel() throws IOException {
        cars.add(createCar("Toyota Camry 2.0", 2020, 200));
        cars.add(createCar("BMW 3-Series", 2021, 300));
        cars.add(createCar("Audi A4 Quattro", 2022, 250));

        String pathWithoutExt = testFilePath.toString().replace(".txt", "");
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан");
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(3, lines.size(), "Должно быть 3 строки");

        String content = String.join(" ", lines);
        assertTrue(content.contains("Toyota Camry 2.0"));
        assertTrue(content.contains("BMW 3-Series"));
        assertTrue(content.contains("Audi A4 Quattro"));
    }

    @Test
    void export_ShouldAppendToExistingFile() throws IOException {
        String pathWithoutExt = testFilePath.toString().replace(".txt", "");

        cars.add(createCar("First Car", 2020, 200));
        strategy.export(pathWithoutExt, cars);

        cars.clear();
        cars.add(createCar("Second Car", 2021, 300));
        strategy.export(pathWithoutExt, cars);

        assertTrue(Files.exists(testFilePath), "Файл должен быть создан");
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(2, lines.size(), "Должно быть 2 строки");
        assertTrue(lines.get(0).contains("First Car"));
        assertTrue(lines.get(1).contains("Second Car"));
    }

    @Test
    void export_ShouldWorkWithRelativePath() throws IOException {
        String relativePath = "test_relative";
        Path relativeFilePath = Paths.get(relativePath + ".txt");

        try {
            cars.add(createCar("Test Relative", 2020, 200));
            strategy.export(relativePath, cars);

            assertTrue(Files.exists(relativeFilePath), "Файл должен быть создан по относительному пути");
        } finally {
            Files.deleteIfExists(relativeFilePath);
        }
    }

    @Test
    void export_ShouldWorkWithDifferentFileNames() throws IOException {
        String[] fileNames = {"test1", "test2", "test3"};

        for (String name : fileNames) {
            Path filePath = tempDir.resolve(name + ".txt");
            String pathWithoutExt = filePath.toString().replace(".txt", "");

            cars.clear();
            cars.add(createCar("Car " + name, 2020, 200));
            strategy.export(pathWithoutExt, cars);

            assertTrue(Files.exists(filePath), "Файл должен быть создан: " + name);
            Files.deleteIfExists(filePath);
        }
    }
}