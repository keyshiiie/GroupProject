package reader;

import car.Car;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCarReader {

    public static List<Car> readRandomCarsFromFile() throws Exception {
        Path path = findFile("randomCarsFile.txt");
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
            cars = readFirstLineRandomCars(path);
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


    private static List<Car> readFirstLineRandomCars(Path path) throws Exception {
        List<Car> cars = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(path)) {
                if (line.isEmpty()) continue;
                if (line.startsWith("Автомобиль: ")) {
                    cars.addAll(getCar(line));
                }
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

    private static List<Car> getCar(String line) {
        String content = line.replaceFirst("^Автомобиль:", "");
        String[] parts = content.split(";");
        String[] powers = parts[0].replaceAll("\\s*", "").split("–");
        String model = parts[1];
        String[] years = parts[2].replaceAll("\\s*", "").split("–");
        int minPower = Integer.parseInt(powers[0]);
        int maxPower = 0;
        int firstYear = Integer.parseInt(years[0]);
        int lastYear = 0;
        if (powers.length == 2)
        {
            maxPower = Integer.parseInt(powers[1]);
        }
        if (years.length == 2){
             lastYear = Integer.parseInt(years[1]);
        }
        List<Car> randomCars = new ArrayList<>();
        Random rand = new Random();
        for (int index = 0; index < 10000;index++){
            int randomPower = minPower;
            int randomYear = firstYear;
            if(maxPower != 0)
            {
                randomPower = rand.nextInt(maxPower - minPower + 1) + minPower;
            }
            if (lastYear != 0)
            {
                randomYear = rand.nextInt(lastYear - firstYear + 1) + firstYear;
            }
            randomCars.add(new Car.Builder()
                    .setPower(randomPower)
                    .setModel(model)
                    .setYear(randomYear)
                    .build());
        }

        return randomCars;
    }
}
