package strategy;

import car.Car;
import java.util.Scanner;
import java.util.function.Predicate;

public class CountByYearStrategy extends BaseCountStrategy {
    private int targetYear;

    public CountByYearStrategy(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected String promptUser() {
        System.out.print("Введите год выпуска для поиска: ");
        return scanner.nextLine().trim();
    }

    @Override
    protected boolean parseInput(String input) {
        try {
            targetYear = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число!");
            return false;
        }
    }

    @Override
    protected Predicate<Car> getCondition() {
        return car -> car.getYear() == targetYear;
    }

    @Override
    public String getLabel() {
        return "Подсчет по году выпуска";
    }
}