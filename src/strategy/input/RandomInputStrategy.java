package strategy.input;

import car.Car;
import car.CarList;
import utils.CarRandomList;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


public class RandomInputStrategy implements InputStrategy {
    private final Scanner scanner;

    public RandomInputStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public CarList setCars() {
        try{
            System.out.print("Введите количество случайных автомобилей: ");
            int size = Integer.parseInt(scanner.nextLine().trim());
            if(size <=0) throw new RuntimeException("размер должен быть больше 0");
            CarList randomCars = CarRandomList.getCars();
            if (randomCars == null || randomCars.isEmpty()) {
                throw new RuntimeException("Нет доступных случайных автомобилей");
            }
            CarList shuffledCars = new CarList(randomCars);
            shuffledCars.shuffle();
            return IntStream.range(0, Math.min(size, shuffledCars.size()))
                    .mapToObj(shuffledCars::get)
                    .collect(CarList::new, CarList::add, CarList::addAll);
        } catch(Exception e){
            System.err.println("Ошибка при чтении списка автомобилей: " + e.getMessage());
            return new CarList();
        }
    }

    @Override
    public String getLabel() {
        return "Случайное заполнение списка автомобилей";
    }
}
