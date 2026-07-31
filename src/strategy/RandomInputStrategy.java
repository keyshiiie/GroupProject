package strategy;

import car.Car;
import reader.RandomCarReader;
import util.CarRandomList;


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
            List<Car> randomCars = new ArrayList<>();
            do {
                randomCars.addAll(CarRandomList.getCars());
            }while (randomCars.size()<size);
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
