package strategy.count;

import car.Car;
import java.util.Scanner;
import java.util.function.Predicate;

public class CountByPowerStrategy extends BaseCountStrategy {
    private int targetPower;

    public CountByPowerStrategy(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected String promptUser() {
        System.out.print("Введите мощность для поиска (л.с.): ");
        return scanner.nextLine().trim();
    }

    @Override
    protected boolean parseInput(String input) {
        try {
            targetPower = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число!");
            return false;
        }
    }

    @Override
    protected Predicate<Car> getCondition() {
        return car -> car.getPower() == targetPower;
    }

    @Override
    public String getLabel() {
        return "Подсчет по мощности двигателя";
    }

    @Override
    public String getSearchValue() {
        return String.valueOf(targetPower);
    }
}