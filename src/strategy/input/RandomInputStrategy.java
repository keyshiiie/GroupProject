package strategy.input;

import car.Car;
import car.CarList;


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
    public CarList setCars() {
        try{
            System.out.print("Введите количество случайных автомобилей:");
            int size = Integer.parseInt(scanner.nextLine().trim());
            if(size <=0) throw new RuntimeException("размер должен быть больше 0");
            List<Car> randomCars = new ArrayList<>(FileInputStrategy.readCarsFromFile("randomCarsFile.txt", 100));
            Collections.shuffle(randomCars);
            int actualSize = Math.min(size, randomCars.size());
            return new CarList(randomCars.subList(0, actualSize));
        } catch(Exception e){
            System.err.println("Ошибка при чтении списка автомобилей: " + e.getMessage());
            return new CarList(new ArrayList<>());
        }
    }

    @Override
    public String getLabel() {
        return "Случайное заполнение списка автомобилей";
    }
}
