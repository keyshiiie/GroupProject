import Utils.LinkedList;
import car.Car;
import mainMenu.*;
import registry.StrategyRegistry;
import strategy.count.CountByModelStrategy;
import strategy.count.CountByPowerStrategy;
import strategy.count.CountByYearStrategy;
import strategy.count.CountStrategy;
import strategy.export.ExportCarsStrategy;
import strategy.export.ExportSearchResultStrategy;
import strategy.export.ExportStrategy;
import strategy.input.ConsoleInputStrategy;
import strategy.input.FileInputStrategy;
import strategy.input.InputStrategy;
//import strategy.input.RandomInputStrategy;
import strategy.sort.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var sortRegistry = new StrategyRegistry<SortStrategy>();
        var inputRegistry = new StrategyRegistry<InputStrategy>();
        var countRegistry = new StrategyRegistry<CountStrategy>();

        var exportRegistry = new StrategyRegistry<ExportStrategy>();
        exportRegistry.register(new ExportCarsStrategy());
        exportRegistry.register(new ExportSearchResultStrategy());

        sortRegistry.register(new SortByPowerStrategy());
        sortRegistry.register(new SortByPowerEvenStrategy());
        sortRegistry.register(new SortByModelStrategy());
        sortRegistry.register(new SortByYearStrategy());
        sortRegistry.register(new SortByYearEvenStrategy());

        var carsStorage = new LinkedList<Car>();
        var countResultStorage = new LinkedList<String>();


        CommandRegistry commandRegistry = new CommandRegistry();

        Scanner scanner = new Scanner(System.in);

        try {
            //inputRegistry.register(new RandomInputStrategy(scanner));
            inputRegistry.register(new ConsoleInputStrategy(scanner));
            inputRegistry.register(new FileInputStrategy(scanner));

            countRegistry.register(new CountByPowerStrategy(scanner));
            countRegistry.register(new CountByYearStrategy(scanner));
            countRegistry.register(new CountByModelStrategy(scanner));

            commandRegistry.register(
                    new CountingCommand(
                            countRegistry,
                            carsStorage::addAll,
                            System.out,
                            carsStorage,
                            scanner,
                            countResultStorage
                    ));
            commandRegistry.register(new HelpCommand(commandRegistry));
            commandRegistry.register(new ExitCommand());

            commandRegistry.register(
                    new SortingCommand(
                            sortRegistry,
                            sorted -> {
                                carsStorage.clear();
                                carsStorage.addAll(sorted);
                            },
                            () -> carsStorage,
                            System.out,
                            scanner
                    )
            );

            commandRegistry.register(
                    new InputNewCarsCommand(
                            inputRegistry,
                            carsStorage::addAll,
                            System.out,
                            scanner
                    )
            );

            commandRegistry.register(new OutputCarsCommand(carsStorage));
            commandRegistry.register(new ExportCommand(exportRegistry, carsStorage, countResultStorage, scanner, System.out));

            System.out.println("Добро пожаловать! Для выполнения действия введите команду из списка ниже.");

            System.out.println("Доступные команды:");

            for (String commandKey : commandRegistry.getAvailableCommands()) {
                ConsoleCommand cmd = commandRegistry.getCommand(commandKey);
                System.out.println("- " + cmd.getCommandText() + ": " + cmd.getUserGuide());
            }

            System.out.println();

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;

                String[] parts = input.split("\\s+", 2);
                String commandText = parts[0];
                String rawArgs = parts.length > 1 ? parts[1] : "";
                String[] argArray = rawArgs.isBlank() ? new String[0] : rawArgs.split("\\s+");

                ConsoleCommand cmd = commandRegistry.getCommand(commandText);

                if (cmd == null) {
                    System.out.println("Неизвестная команда: " + commandText);
                    continue;
                }

                cmd.execute(argArray);
            }
        } catch (Exception e) {
            System.err.println("Произошла критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}