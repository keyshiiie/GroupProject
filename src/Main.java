import car.Car;
import MainMenu.*;
import registry.StrategyRegistry;
import strategy.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var sortRegistry = new StrategyRegistry<SortStrategy>();
        var inputRegistry = new StrategyRegistry<InputStrategy>();
        var countRegistry = new StrategyRegistry<CountStrategy>();
        /*
         --- Стратегии сортировки---
        sortRegistry.register(new SortByPowerStrategy());
        sortRegistry.register(new SortByModelStrategy());
        sortRegistry.register(new SortByYearStrategy());
        */


        var carsStorage = new ArrayList<Car>();
        var countResultStorage = new ArrayList<String>();


        CommandRegistry commandRegistry = new CommandRegistry();

        Scanner scanner = new Scanner(System.in);

        try {
            inputRegistry.register(new RandomInputStrategy(scanner));
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
                            System.out
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