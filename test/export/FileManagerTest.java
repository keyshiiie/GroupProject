package export;

import car.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    @TempDir
    Path tempDir;

    private List<Car> testCars;
    private List<String> testResults;

    @BeforeEach
    void setUp() {
        testCars = new ArrayList<>();
        testCars.add(new Car.Builder()
                .setModel("Toyota Camry")
                .setYear(2020)
                .setPower(200)
                .build());
        testCars.add(new Car.Builder()
                .setModel("BMW X5")
                .setYear(2022)
                .setPower(300)
                .build());
        testCars.add(new Car.Builder()
                .setModel("Mercedes E-Class")
                .setYear(2021)
                .setPower(250)
                .build());

        testResults = new ArrayList<>();
        testResults.add("Результат 1: найдено 3 автомобиля");
        testResults.add("Результат 2: средняя мощность 250 л.с.");
    }

    @Test
    void testValidateFileName_ValidName() {
        assertNull(FileManager.validateFileName("validFileName"));
        assertNull(FileManager.validateFileName("file123"));
        assertNull(FileManager.validateFileName("my-file.txt"));
        assertNull(FileManager.validateFileName("my_file"));
        assertNull(FileManager.validateFileName("file with spaces"));
        assertNull(FileManager.validateFileName("ИмяФайла"));
    }

    @Test
    void testValidateFileName_NullName() {
        String error = FileManager.validateFileName(null);
        assertEquals("Имя файла не может быть пустым.", error);
    }

    @Test
    void testValidateFileName_EmptyName() {
        String error = FileManager.validateFileName("");
        assertEquals("Имя файла не может быть пустым.", error);
    }

    @Test
    void testValidateFileName_WhitespaceOnly() {
        String error = FileManager.validateFileName("   ");
        assertEquals("Имя файла не может заканчиваться на точку или пробел.", error);
    }

    @Test
    void testValidateFileName_TooLong() {
        String longName = "a".repeat(256);
        String error = FileManager.validateFileName(longName);
        assertEquals("Имя файла слишком длинное (максимум 255 символов).", error);
    }

    @Test
    void testValidateFileName_ExactlyMaxLength() {
        String name = "a".repeat(255);
        assertNull(FileManager.validateFileName(name));
    }

    @Test
    void testValidateFileName_InvalidCharacters() {
        String[] invalidChars = {"<>", ":", "\"", "/", "\\", "|", "?", "*"};
        for (String ch : invalidChars) {
            String fileName = "file" + ch + "name";
            String error = FileManager.validateFileName(fileName);
            assertNotNull(error);
            assertTrue(error.contains("недопустимые символы"));
        }
    }

    @Test
    void testValidateFileName_ReservedNames() {
        String[] reservedNames = {
                "CON", "PRN", "AUX", "NUL",
                "COM1", "COM2", "LPT1", "LPT2"
        };
        for (String name : reservedNames) {
            String error = FileManager.validateFileName(name);
            assertEquals("Имя '" + name + "' является зарезервированным системным именем.", error);
        }
    }

    @Test
    void testValidateFileName_ReservedNameWithExtension() {
        String error = FileManager.validateFileName("CON.txt");
        assertEquals("Имя 'CON.txt' является зарезервированным системным именем.", error);
    }

    @Test
    void testValidateFileName_EndsWithDot() {
        String error = FileManager.validateFileName("file.");
        assertEquals("Имя файла не может заканчиваться на точку или пробел.", error);
    }

    @Test
    void testValidateFileName_EndsWithSpace() {
        String error = FileManager.validateFileName("file ");
        assertEquals("Имя файла не может заканчиваться на точку или пробел.", error);
    }

    @Test
    void testValidateFileName_ContainsDotDot() {
        String error = FileManager.validateFileName("file..name");
        assertEquals("Имя файла не может содержать '..'.", error);
    }

    @Test
    void testGetFileName_ValidInput() {
        String input = "testfile\n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        String result = FileManager.getFileName(scanner, out);

        assertEquals("testfile", result);
        assertTrue(outputStream.toString().contains("Введите имя файла"));
    }

    @Test
    void testGetFileName_WithSpaces() {
        String input = "  my file name  \n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        String result = FileManager.getFileName(scanner, out);

        assertEquals("my file name", result);
    }

    @Test
    void testGetFileName_Cancel() {
        String input = "отмена\n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        String result = FileManager.getFileName(scanner, out);

        assertNull(result);
        assertTrue(outputStream.toString().contains("Введите имя файла"));
    }

    @Test
    void testGetFileName_CancelWithSpaces() {
        String input = "  отмена  \n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        String result = FileManager.getFileName(scanner, out);

        assertNull(result);
    }

    @Test
    void testGetFileName_CancelUpperCase() {
        String input = "ОТМЕНА\n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        String result = FileManager.getFileName(scanner, out);

        assertNull(result);
    }

    @Test
    void testGetFileName_InvalidThenValid() {
        String input = "invalid?.txt\nvalidfile\n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        String result = FileManager.getFileName(scanner, out);

        assertEquals("validfile", result);
        String output = outputStream.toString();
        assertTrue(output.contains("Ошибка"));
        assertTrue(output.contains("Введите имя файла"));
    }

    @Test
    void testGetFileName_EmptyThenValid() {
        String input = "\nvalidfile\n";
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        String result = FileManager.getFileName(scanner, out);

        assertEquals("validfile", result);
        assertTrue(outputStream.toString().contains("Ошибка"));
    }
    
    @Test
    void testWriteCarToFile_Success() throws IOException {
        String fileName = tempDir.resolve("cars").toString();
        FileManager.writeCarToFile(fileName, testCars);

        File file = new File(fileName + ".txt");
        assertTrue(file.exists());

        List<String> lines = readFileLines(file);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("Toyota Camry"));
        assertTrue(lines.get(0).contains("2020"));
        assertTrue(lines.get(0).contains("200"));
        assertTrue(lines.get(1).contains("BMW X5"));
        assertTrue(lines.get(2).contains("Mercedes E-Class"));
    }

    @Test
    void testWriteCarToFile_EmptyList() throws IOException {
        String fileName = tempDir.resolve("empty").toString();
        List<Car> emptyCars = new ArrayList<>();

        FileManager.writeCarToFile(fileName, emptyCars);

        File file = new File(fileName + ".txt");
        assertTrue(file.exists());
        List<String> lines = readFileLines(file);
        assertEquals(0, lines.size());
    }

    @Test
    void testWriteCarToFile_AppendMode() throws IOException {
        String fileName = tempDir.resolve("append_test").toString();
        FileManager.writeCarToFile(fileName, testCars);

        List<Car> additionalCars = new ArrayList<>();
        additionalCars.add(new Car.Builder()
                .setModel("Audi A4")
                .setYear(2023)
                .setPower(190)
                .build());
        FileManager.writeCarToFile(fileName, additionalCars);

        File file = new File(fileName + ".txt");
        List<String> lines = readFileLines(file);
        assertEquals(4, lines.size());
        assertTrue(lines.get(3).contains("Audi A4"));
    }

    @Test
    void testWriteCarToFile_ExceptionHandling() {
        String invalidPath = "/invalid/path/that/does/not/exist/cars";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FileManager.writeCarToFile(invalidPath, testCars);
        });
        assertEquals("Ошибка добавления полей в файл", exception.getMessage());
    }

    @Test
    void testWriteCountResultToFile_Success() throws IOException {
        String fileName = tempDir.resolve("results").toString();
        FileManager.writeCountResultToFile(fileName, testResults);

        File file = new File(fileName + ".txt");
        assertTrue(file.exists());

        List<String> lines = readFileLines(file);
        assertEquals(2, lines.size());
        assertEquals("Результат 1: найдено 3 автомобиля", lines.get(0));
        assertEquals("Результат 2: средняя мощность 250 л.с.", lines.get(1));
    }

    @Test
    void testWriteCountResultToFile_EmptyList() throws IOException {
        String fileName = tempDir.resolve("empty_results").toString();
        List<String> emptyResults = new ArrayList<>();

        FileManager.writeCountResultToFile(fileName, emptyResults);

        File file = new File(fileName + ".txt");
        assertTrue(file.exists());
        List<String> lines = readFileLines(file);
        assertEquals(0, lines.size());
    }

    @Test
    void testWriteCountResultToFile_AppendMode() throws IOException {
        String fileName = tempDir.resolve("append_results").toString();
        FileManager.writeCountResultToFile(fileName, testResults);

        List<String> additionalResults = new ArrayList<>();
        additionalResults.add("Результат 3: максимальная мощность 300 л.с.");
        FileManager.writeCountResultToFile(fileName, additionalResults);

        File file = new File(fileName + ".txt");
        List<String> lines = readFileLines(file);
        assertEquals(3, lines.size());
        assertEquals("Результат 3: максимальная мощность 300 л.с.", lines.get(2));
    }

    @Test
    void testWriteCountResultToFile_ExceptionHandling() {
        String invalidPath = "/invalid/path/results";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FileManager.writeCountResultToFile(invalidPath, testResults);
        });
        assertEquals("Ошибка добавления полей в файл", exception.getMessage());
    }

    private List<String> readFileLines(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}