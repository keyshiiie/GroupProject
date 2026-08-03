package strategy.input;

import car.Car;
import car.CarList;

import java.util.Objects;
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
                    .takeWhile(Objects::nonNull)
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
        while (true) {
            try {
                System.out.println("\n--- Ввод автомобиля №" + index + " ---");
                System.out.print("Введите модель автомобиля (или Enter для отмены): ");
                String model = scanner.nextLine().trim();

                if (model.isEmpty()) {
                    System.out.println("Ввод отменен.");
                    return null;
                }

                System.out.print("Введите год производства: ");
                String yearInput = scanner.nextLine().trim();
                if (yearInput.isEmpty()) {
                    System.out.println("Ввод отменен.");
                    return null;
                }
                int year = Integer.parseInt(yearInput);

                System.out.print("Введите мощность двигателя: ");
                String powerInput = scanner.nextLine().trim();
                if (powerInput.isEmpty()) {
                    System.out.println("Ввод отменен.");
                    return null;
                }
                int power = Integer.parseInt(powerInput);

                return new Car.Builder()
                        .setModel(model)
                        .setYear(year)
                        .setPower(power)
                        .build();

            } catch (NumberFormatException e) {
                System.err.println("Ошибка: введите корректное число!");
                System.out.println("Попробуйте снова или нажмите Enter для отмены.");
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте снова или нажмите Enter для отмены.");
            }
        }
    }
    @Override
    public String getLabel() {
        return "Ручной ввод автомобиля с консоли";
    }
}