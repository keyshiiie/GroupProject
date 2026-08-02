package strategy.input;

import car.Car;
import car.CarList;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileInputStrategy implements InputStrategy {
    private final Scanner scanner;

    public FileInputStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String getLabel() {
        return "Загрузка автомобилей из файла";
    }

    @Override
    public CarList setCars() {
        try {
            System.out.print("Введите имя файла: ");
            String filename = scanner.nextLine().trim();

            System.out.print("Введите количество автомобилей для загрузки: ");
            int size = Integer.parseInt(scanner.nextLine().trim());
            if(size <=0) throw new RuntimeException("размер должен быть больше 0");
            List<Car> carsFromFile = readCarsFromFile(filename, size);
            CarList carList = carsFromFile.stream()
                    .collect(
                            CarList::new,
                            CarList::add,
                            CarList::addAll
                    );
            return carList;
        } catch (Exception e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return new CarList(new ArrayList<>());
        }
    }

    public static List<Car> readCarsFromFile(String filename,int size) throws Exception {
        Path path = findFile(filename);
        System.out.println("Файл найден: " + path.toAbsolutePath());

        if (!Files.exists(path)) {
            throw new Exception("Файл не найден");
        }
        if (Files.isRegularFile(path) && !path.getFileName().toString().endsWith(".txt")) {
            throw new Exception("Файл должен быть в формате txt");
        }
        List<Car> cars = new ArrayList<>();
        String firstLine = readFirstLine(path);
        if (firstLine.startsWith("Автомобиль: ")) {
            cars = readFirstLineCars(path,size);
        }
        if (!firstLine.startsWith("Автомобиль: ")) {
            throw new Exception("Файл не подходит под формат чтения \n" +
                    "(Автомобиль: мощность двигателя, полное название модели," +
                    " год производства\")");
        }
        return cars;
    }

    private static String readFirstLine(Path path) throws Exception {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.readLine();
        }
    }


    private static List<Car> readFirstLineCars(Path path, int size) throws Exception {
        List<Car> cars = new ArrayList<>();
        int i = 0;
        try {
            for (String line : Files.readAllLines(path)) {
                if (line.isEmpty()) continue;
                if (line.startsWith("Автомобиль: ")) {
                    cars.add(getCar(line));
                    i++;
                }
                if (i == size) break;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage() + "Файл не подходит под формат чтения\n");
        }
        return cars;
    }

    private static Path findFile(String filename) throws Exception {
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        List<Path> searchRoots = Arrays.asList(
                projectRoot,
                projectRoot.resolve("src"),
                projectRoot.getParent()
        );

        for (Path root : searchRoots) {
            Path candidate = root.resolve(filename).normalize().toAbsolutePath();
            if (Files.exists(candidate) && Files.isReadable(candidate)) {
                return candidate;
            }
        }

        throw new Exception("Файл " + filename + " не найден. Проверены: " + searchRoots);
    }

    private static Car getCar(String line) {
        String content = line.replaceFirst("^Автомобиль:", "");
        String[] parts = content.split(";");
        int power = Integer.parseInt(parts[0].replaceAll("\\s*", ""));
        String model = parts[1];
        int year = Integer.parseInt(parts[2].replaceAll("\\s*", ""));
        return new Car.Builder()
                .setPower(power)
                .setModel(model)
                .setYear(year)
                .build();
    }
}