package strategy.count;

import Utils.LinkedList;
import car.Car;
import utils.MultithreadedCounter;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public abstract class BaseCountStrategy implements CountStrategy {
    protected final Scanner scanner;
    protected String searchValue;

    public BaseCountStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public int count(LinkedList<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            System.out.println("Список автомобилей пуст.");
            return 0;
        }

        String input = promptUser();
        if (input == null || input.isEmpty()) {
            System.out.println("Ввод отменен.");
            return 0;
        }

        if (!parseInput(input)) {
            return 0;
        }

        Predicate<Car> condition = getCondition();
        return MultithreadedCounter.countCars(cars, condition);
    }

    protected abstract String promptUser();
    protected abstract boolean parseInput(String input);
    protected abstract Predicate<Car> getCondition();
}