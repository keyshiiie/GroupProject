package mainMenu;

import car.Car;
import registry.StrategyRegistry;
import strategy.count.CountStrategy;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class CountingCommand implements ConsoleCommand {
    private final StrategyRegistry<CountStrategy> countRegistry;
    private final Consumer<List<Car>> onCounted;
    private final Scanner scanner;
    private final PrintStream out;
    private final List<Car> carsStorage;
    private final List<String> countResultStorage;

    public CountingCommand(
            StrategyRegistry<CountStrategy> countRegistry,
            Consumer<List<Car>> onCounted,
            PrintStream out,
            List<Car> carsStorage,
            Scanner scanner,
            List<String> countResultStorage
    ) {
        this.countRegistry = countRegistry;
        this.onCounted = onCounted;
        this.out = out;
        this.scanner = scanner;
        this.carsStorage = carsStorage;
        this.countResultStorage = countResultStorage;
    }

    @Override
    public String getCommandText() {
        return "count";
    }

    @Override
    public String getUserGuide() {
        return "Подсчет количества вхождений элемента в коллекцию (многопоточный)";
    }

    @Override
    public void execute(String[] args) {
        if (carsStorage.isEmpty()) {
            out.println("Нет автомобилей для подсчета. Сначала добавьте автомобили.");
            return;
        }

        var strategies = countRegistry.getAll();
        if (strategies.isEmpty()) {
            out.println("Нет зарегистрированных стратегий подсчета.");
            return;
        }

        out.println("\n--- Подсчет автомобилей ---");
        out.println("Всего автомобилей: " + carsStorage.size());

        out.println("\nВыберите вид подсчета:");
        for (int i = 0; i < strategies.size(); i++) {
            var s = strategies.get(i);
            out.printf("%d. %s%n", i + 1, s.getLabel());
        }
        out.print("Ваш выбор: ");

        String line = scanner.nextLine().strip();
        if (line.isEmpty()) {
            out.println("Выбор отменён.");
            return;
        }

        int idx;
        try {
            idx = Integer.parseInt(line) - 1;
        } catch (NumberFormatException e) {
            out.println("Некорректный ввод. Введите число.");
            return;
        }

        if (idx < 0 || idx >= strategies.size()) {
            out.println("Недопустимый номер. Выберите от 1 до " + strategies.size());
            return;
        }

        var selected = strategies.get(idx);
        out.printf("Выбран вид: %s%n", selected.getLabel());

        try {
            int countElements = selected.count(carsStorage);

            String result = selected.getLabel() + ": " + selected.getSearchValue() +
                    " - Найдено: " + countElements;
            countResultStorage.add(result);


            onCounted.accept(carsStorage);
            out.println("Найдено элементов: " + countElements);
            out.println("\n-------------");
        } catch (Exception e) {
            out.println("Ошибка при подсчете: " + e.getMessage());
        }
    }
}