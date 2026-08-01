package export;

import car.Car;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    private static final int MAX_FILE_NAME_LENGTH = 255;
    private static final String INVALID_CHARS = "[<>:\"/\\\\|?*]";

    private FileManager() {
        throw new UnsupportedOperationException("Утилитарный класс");
    }

    public static String getFileName(Scanner scanner, PrintStream out) {
        String fileName = null;
        boolean valid = false;

        while (!valid) {
            out.print("Введите имя файла (или 'отмена' для выхода): ");
            String input = scanner.nextLine();

            if (input == null || input.trim().equalsIgnoreCase("отмена")) {
                return null;
            }

            fileName = input.strip();

            String validationError = validateFileName(fileName);
            if (validationError != null) {
                out.println("Ошибка: " + validationError);
                out.println("Попробуйте снова.");
            } else {
                valid = true;
            }
        }

        return fileName;
    }

    public static String validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "Имя файла не может быть пустым.";
        }

        if (fileName.length() > MAX_FILE_NAME_LENGTH) {
            return "Имя файла слишком длинное (максимум " + MAX_FILE_NAME_LENGTH + " символов).";
        }

        if (fileName.matches(".*" + INVALID_CHARS + ".*")) {
            return "Имя содержит недопустимые символы: < > : \" / \\ | ? *";
        }

        String[] reservedNames = {
                "CON", "PRN", "AUX", "NUL",
                "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
                "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
        };

        String upperName = fileName.toUpperCase();

        for (String reserved : reservedNames) {
            if (upperName.equals(reserved) || upperName.startsWith(reserved + ".")) {
                return "Имя '" + fileName + "' является зарезервированным системным именем.";
            }
        }

        if (fileName.endsWith(".") || fileName.endsWith(" ")) {
            return "Имя файла не может заканчиваться на точку или пробел.";
        }

        try {
            Paths.get(fileName);
        } catch (InvalidPathException e) {
            return "Некорректное имя файла: " + e.getMessage();
        }

        if (fileName.contains("..")) {
            return "Имя файла не может содержать '..'.";
        }

        return null;
    }

    public static void writeCarToFile(String fileName, List<Car> carsStorage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt", true))) {
            for (Car car : carsStorage) {
                String model = car.getModel();
                String year = String.valueOf(car.getYear());
                String power = String.valueOf(car.getPower());

                String text = "Модель: " + model + " Год: " + year + " Мощность: " + power;
                writer.write(text);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка добавления полей в файл");
        }
    }

    public static void writeCountResultToFile(String fileName, List<String> results) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt", true))) {
            for (String res : results) {
                writer.write(res);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка добавления полей в файл");
        }
    }
}