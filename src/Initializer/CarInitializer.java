package Initializer;

import car.Car;

import java.util.Scanner;

public class CarInitializer {
    public static Car initCar() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Напишите мощность двигателя");
        int power = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Напишите год производства");
        int year = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Напишите год производства");
        String model = scanner.nextLine().trim();
        try {
            scanner.close();
        }
        catch (Exception e)
        {
            System.out.println("Ошибка при закрытии сканера");
            throw new Exception(e.getMessage());
        }
        return new Car.Builder()
                .setYear(year)
                .setPower(power)
                .setModel(model)
                .build();
    }
}
