package mainMenu;

import car.Car;
import car.CarList;
import comparators.CarModelComparator;
import comparators.CarPowerComparator;
import comparators.CarYearComparator;
import registry.StrategyRegistry;
import strategy.export.ExportStrategy;
import utils.StringList;

import java.io.PrintStream;
import java.util.*;

import static export.FileManager.getFileName;
import static utils.SortedChecker.isSorted;

public class ExportCommand implements ConsoleCommand {
    private final StrategyRegistry<ExportStrategy> exportStrategyRegistry;
    private final CarList carsStorage;
    private final StringList countResultStorage;
    private final Scanner scanner;
    private final PrintStream out;

    private final CarPowerComparator powerComparator = new CarPowerComparator();
    private final CarModelComparator modelComparator = new CarModelComparator();
    private final CarYearComparator yearComparator = new CarYearComparator();

    public ExportCommand(StrategyRegistry<ExportStrategy> exportStrategyRegistry, CarList carsStorage,
                         StringList countResultStorage, Scanner scanner, PrintStream out) {
        this.exportStrategyRegistry = exportStrategyRegistry;
        this.carsStorage = carsStorage;
        this.countResultStorage = countResultStorage;
        this.scanner = scanner;
        this.out = out;
    }

    @Override
    public String getCommandText() {
        return "export";
    }

    @Override
    public String getUserGuide() {
        return "Вывод списка автомобилей в файл.";
    }

    @Override
    public void execute(String[] args) {
        if (carsStorage.isEmpty()) {
            out.println("Массив пуст.");
            return;
        }

        String fileName = getFileName(scanner, out);
        if (fileName == null) {
            out.println("Выбор отменён.");
            return;
        }

        out.println("Имя файла принято: " + fileName);

        var strategies = exportStrategyRegistry.getAll();
        if (strategies.isEmpty()) {
            out.println("Нет зарегистрированных видов экспорта.");
            return;
        }

        ExportStrategy selected = selectExportStrategy(strategies);
        if (selected == null) {
            return;
        }

        out.printf("Выбранный экспорт: %s%n", selected.getLabel());

        Collection<?> data = getDataForExport(selected);
        if (data == null) {
            return;
        }

        if (data == carsStorage) {
            if (!isArraySorted()) {
                if (!confirmExportUnsorted()) {
                    out.println("Экспорт отменён.");
                    return;
                }
            } else {
                out.println("Массив отсортирован.");
            }
        }

        out.println("Выгрузка началась!");
        try {
            selected.export(fileName, data);
            out.println("Экспорт завершён успешно!");
        } catch (Exception e) {
            out.println("Ошибка экспорта: " + e.getMessage());
        }
    }

    private ExportStrategy selectExportStrategy(List<ExportStrategy> strategies) {
        out.println("Выберите что экспортировать:");

        for (int i = 0; i < strategies.size(); i++) {
            var s = strategies.get(i);
            out.printf("%d. %s%n", i + 1, s.getLabel());
        }

        out.print("Ваш выбор (или 'exit' для выхода): ");
        String line = scanner.nextLine().trim();

        if (line.isEmpty() || line.equalsIgnoreCase("exit")) {
            out.println("Выбор отменён.");
            return null;
        }

        int idx;
        try {
            idx = Integer.parseInt(line) - 1;
        } catch (NumberFormatException e) {
            out.println("Некорректный ввод. Введите число.");
            return null;
        }

        if (idx < 0 || idx >= strategies.size()) {
            out.println("Недопустимый номер.");
            return null;
        }

        return strategies.get(idx);
    }

    private Collection<?> getDataForExport(ExportStrategy selected) {
        String label = selected.getLabel();

        if ("Список машин".equals(label)) {
            return carsStorage;
        } else if ("Результаты поиска".equals(label)) {
            if (countResultStorage.isEmpty()) {
                out.println("Нет результатов поиска для сохранения.");
                return null;
            }
            return countResultStorage;
        } else {
            out.println("Неизвестный тип данных для экспорта.");
            return null;
        }
    }

    private boolean isArraySorted() {
        List<Comparator<Car>> comparators = Arrays.asList(
                powerComparator,
                modelComparator,
                yearComparator
        );

        return comparators.stream()
                .anyMatch(comp -> isSorted(carsStorage, comp));
    }

    private boolean confirmExportUnsorted() {
        out.println("Массив еще не был отсортирован.");
        out.println("Вы точно хотите сохранить его в файл?");
        out.println("1 - Да");
        out.println("2 - Нет (или 'exit' для выхода)");

        String response = scanner.nextLine().trim();

        if (response.isEmpty() || response.equalsIgnoreCase("exit")) {
            out.println("Выбор отменён.");
            return false;
        }

        try {
            int choice = Integer.parseInt(response);
            if (choice == 1) {
                return true;
            } else if (choice == 2) {
                out.println("Экспорт отменён.");
                return false;
            } else {
                out.println("Некорректный ввод. Введите 1 или 2.");
                return false;
            }
        } catch (NumberFormatException e) {
            out.println("Некорректный ввод. Введите 1 или 2.");
            return false;
        }
    }
}