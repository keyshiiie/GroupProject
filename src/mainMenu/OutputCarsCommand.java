package mainMenu;

import car.Car;
import car.CarList;

import java.util.List;
import java.util.stream.IntStream;

public class OutputCarsCommand implements ConsoleCommand {
    private final CarList carsStorage;

    public OutputCarsCommand(CarList carsStorage) {
        this.carsStorage = carsStorage;
    }

    @Override
    public String getCommandText() {
        return "show";
    }

    @Override
    public String getUserGuide() {
        return "вывод списка автомобилей";
    }

    @Override
    public void execute(String[] args) {
        if (carsStorage.isEmpty()) {
            System.out.println("Список автомобилей пуст.");
            return;
        }

        System.out.println("--- Список автомобилей ---");

        IntStream.range(0, carsStorage.size())
                .forEach(i -> {
                    Car car = carsStorage.get(i);
                    System.out.println((i + 1) + ". " + car.toString());
                });

        System.out.println("--------------------------");
    }
}
