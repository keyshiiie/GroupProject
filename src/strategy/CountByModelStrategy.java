package strategy;

import car.Car;
import java.util.Scanner;
import java.util.function.Predicate;

public class CountByModelStrategy extends BaseCountStrategy {
    private String targetModel;

    public CountByModelStrategy(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected String promptUser() {
        System.out.print("Введите модель для поиска: ");
        return scanner.nextLine().trim();
    }

    @Override
    protected boolean parseInput(String input) {
        targetModel = input.trim();
        return true;
    }

    @Override
    protected Predicate<Car> getCondition() {
        return car -> {
            String carModel = car.getModel().trim();
            return carModel.equalsIgnoreCase(targetModel);
        };
    }

    @Override
    public String getLabel() {
        return "Подсчет по модели автомобиля";
    }

    @Override
    public String getSearchValue() {
        return String.valueOf(targetModel);
    }
}