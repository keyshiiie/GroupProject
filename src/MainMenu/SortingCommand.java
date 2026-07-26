package MainMenu;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import Car.Car;
import registry.StrategyRegistry;
import strategy.NamedStrategy;
import strategy.SortStrategy;

public class SortingCommand implements ConsoleCommand {

    private final StrategyRegistry<SortStrategy> sortRegistry;
    private final Consumer<List<Car>> onSorted;
    private final Supplier<List<Car>> currentCarsSupplier;
    private final PrintStream out;

    public SortingCommand(
            StrategyRegistry<SortStrategy> sortRegistry,
            Consumer<List<Car>> onSorted,
            Supplier<List<Car>> currentCarsSupplier,
            PrintStream out
    ) {
        this.sortRegistry = sortRegistry;
        this.onSorted = onSorted;
        this.currentCarsSupplier = currentCarsSupplier;
        this.out = out;
    }

    @Override
    public String getCommandText() {
        return "sort";
    }

    @Override
    public String getUserGuide() {
        return "Сортировка автомобилей: выбор стратегии сортировки.";
    }

    @Override
    public void execute(String[] args) {
        var cars = currentCarsSupplier.get();

        if (cars == null || cars.isEmpty()) {
            out.println("Список автомобилей пуст. Сначала загрузите данные.");

            return;
        }

        var strategies = sortRegistry.getAll();

        if (strategies.isEmpty()) {
            out.println("Нет зарегистрированных стратегий сортировки.");

            return;
        }

        out.println("Выберите стратегию сортировки:");

        for (int i = 0; i < strategies.size(); i++) {
            var s = strategies.get(i);
            out.printf("%d. %s%n", i + 1, ((NamedStrategy)s).getLabel());
        }

        out.print("Ваш выбор: ");

        String line = readLine();

        if (line == null || line.strip().isEmpty()) {
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
        out.printf("Выбрана стратегия: %s%n", selected.getLabel());

        List<Car> sorted;

        try {
            sorted = selected.sort(cars);
        } catch (Exception e) {
            out.println("Ошибка сортировки: " + e.getMessage());
            return;
        }

        onSorted.accept(sorted);
        out.println("Сортировка выполнена.");
    }

    private String readLine() {
        try (var scanner = new java.util.Scanner(System.in)) {
            scanner.useDelimiter("\\R");

            if (scanner.hasNext()) {
                return scanner.next();
            }

            return null;
        }
    }
}