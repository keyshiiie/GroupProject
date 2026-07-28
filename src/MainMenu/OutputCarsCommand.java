package MainMenu;

import car.Car;
import java.util.List;

public class OutputCarsCommand implements ConsoleCommand {
    private final List<Car> carsStorage;

    public OutputCarsCommand(List<Car> carsStorage) {
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

        for (int i = 0; i < carsStorage.size(); i++) {
            Car car = carsStorage.get(i);

            System.out.printf("%d. Модель: %s, Год: %d, Мощность: %d л.с.%n",
                    i + 1,
                    car.getModel(),
                    car.getYear(),
                    car.getPower()
            );
        }

        System.out.println("--------------------------");
    }
}
