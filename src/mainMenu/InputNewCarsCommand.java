package mainMenu;

import car.Car;
import car.CarList;
import registry.StrategyRegistry;
import strategy.input.InputStrategy;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class InputNewCarsCommand implements ConsoleCommand {

    private final StrategyRegistry<InputStrategy> inputRegistry;
    private final Consumer<CarList> onCarsLoaded;
    private final PrintStream out;

    private final Scanner scanner;

    public InputNewCarsCommand(
            StrategyRegistry<InputStrategy> inputRegistry,
            Consumer<CarList> onCarsLoaded,
            PrintStream out,
            Scanner scanner
    ) {
        this.inputRegistry = inputRegistry;
        this.onCarsLoaded = onCarsLoaded;
        this.out = out;
        this.scanner = scanner;
    }

    @Override
    public String getCommandText() {
        return "input";
    }

    @Override
    public String getUserGuide() {
        return "Ввод новых автомобилей: выбор источника данных (файл, консоль, рандом).";
    }

    @Override
    public void execute(String[] args) {
        var strategies = inputRegistry.getAll();
        if (strategies.isEmpty()) {
            out.println("Нет зарегистрированных стратегий ввода.");
            return;
        }

        out.println("Выберите источник данных: ");
        IntStream.range(0, strategies.size())
                .forEach(i -> out.printf("%d. %s%n", i + 1, strategies.get(i).getLabel()));
        out.print("Ваш выбор: ");

        String line = scanner.nextLine().strip();
        if (line == null || line.strip().isEmpty() || line.equals("exit")) {
            out.println("Выбор отменён.");
            return;
        }

        int idx;
        try {
            idx = Integer.parseInt(line.strip()) - 1;
        } catch (NumberFormatException e) {
            out.println("Некорректный ввод.");
            return;
        }

        if (idx < 0 || idx >= strategies.size()) {
            out.println("Недопустимый номер.");
            return;
        }

        var selected = strategies.get(idx);
        out.printf("Выбран источник: %s%n", selected.getLabel());

        CarList cars;
        try {
            cars = selected.setCars();
        } catch (Exception e) {
            out.println("Ошибка при получении данных: " + e.getMessage());
            return;
        }

        onCarsLoaded.accept(cars);
        out.printf("Загружено автомобилей: %d%n", cars.size());
    }
}