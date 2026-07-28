package strategy;

import car.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputStrategy implements InputStrategy {

    private final Scanner scanner;

    public ConsoleInputStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Car> getCars() {
        try {
            System.out.println("\n--- Ввод нового автомобиля ---");

            System.out.print("Введите модель автомобиля: ");
            String model = scanner.nextLine().trim();
            if (model.isEmpty()) {
                System.out.println("Модель не может быть пустой. Ввод отменён.");
                return new ArrayList<>();
            }

            System.out.print("Введите год производства: ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Введите мощность двигателя (л.с.): ");
            int power = Integer.parseInt(scanner.nextLine().trim());

            Car car = new Car.Builder()
                    .setModel(model)
                    .setYear(year)
                    .setPower(power)
                    .build();

            List<Car> cars = new ArrayList<>();
            cars.add(car);

            System.out.println("Автомобиль успешно добавлен: " + car);
            return cars;

        } catch (NumberFormatException e) {
            System.err.println("Ошибка: введите корректное число!");
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Ошибка при вводе данных: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String getLabel() {
        return "Ручной ввод автомобиля с консоли";
    }
}