package strategy.input;

import car.Car;
import car.CarList;

import java.util.Scanner;
import java.util.stream.IntStream;

public class ConsoleInputStrategy implements InputStrategy {

    private final Scanner scanner;

    public ConsoleInputStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public CarList setCars() {
        try {
            System.out.print("Введите количество автомобилей которые хотите добавить: ");
            int size = Integer.parseInt(scanner.nextLine().trim());
            if (size <= 0) {
                System.err.println("Размер должен быть больше 0.");
                return new CarList();
            }
            return IntStream.range(0, size)
                    .mapToObj(i -> readSingleCar(scanner, i + 1))
                    .collect(CarList::new, CarList::add, CarList::addAll);

        } catch (NumberFormatException e) {
            System.err.println("Ошибка: введите корректное число!");
            return new CarList();
        } catch (Exception e) {
            System.err.println("Ошибка при вводе данных: " + e.getMessage());
            return new CarList();
        }
    }
    private static Car readSingleCar(Scanner scanner, int index) {
        System.out.println("\n--- Ввод автомобиля №" + index + " ---");
        System.out.print("Введите модель автомобиля: ");
        String model = scanner.nextLine().trim();
        if (model.isEmpty()) {
            throw new IllegalArgumentException("Модель не может быть пустой.");
        }

        System.out.print("Введите год производства: ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Введите мощность двигателя: ");
        int power = Integer.parseInt(scanner.nextLine().trim());

        return new Car.Builder()
                .setModel(model)
                .setYear(year)
                .setPower(power)
                .build();
    }
    @Override
    public String getLabel() {
        return "Ручной ввод автомобиля с консоли";
    }
}