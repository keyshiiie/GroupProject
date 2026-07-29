package strategy.input;

import car.Car;
import strategy.input.FileInputStrategy;
import strategy.input.InputStrategy;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class RandomInputStrategy implements InputStrategy {
    private final Scanner scanner;

    public RandomInputStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Car> getCars() {
        try{
            System.out.print("Введите количество случайных автомобилей:");
            int size = Integer.parseInt(scanner.nextLine().trim());
            List<Car> randomCars = new ArrayList<>(FileInputStrategy.readCarsFromFile("randomCarsFile.txt", 100));
            Collections.shuffle(randomCars);
            int actualSize = Math.min(size, randomCars.size());
            return randomCars.subList(0, actualSize);
        } catch(Exception e){
            System.err.println("Ошибка при чтении списка автомобилей: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String getLabel() {
        return "Случайное заполнение списка автомобилей";
    }
}
