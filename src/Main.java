import Car.Car;
import MainMenu.*;
import registry.StrategyRegistry;
import strategy.InputStrategy;
import strategy.SortStrategy;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var sortRegistry = new StrategyRegistry<SortStrategy>();
        var inputRegistry = new StrategyRegistry<InputStrategy>();

        /*
         --- Стратегии сортировки---
        sortRegistry.register(new SortByPowerStrategy());
        sortRegistry.register(new SortByModelStrategy());
        sortRegistry.register(new SortByYearStrategy());

        // --- Стратегии ввода ---
        inputRegistry.register(new FileInputStrategy());
        inputRegistry.register(new ConsoleInputStrategy());
        inputRegistry.register(new RandomInputStrategy());
        inputRegistry.register(new MyNewInputStrategy());
        */

        var carsStorage = new ArrayList<Car>();

        CommandRegistry commandRegistry = new CommandRegistry();

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
                        System.out
                )
        );

        commandRegistry.register(new OutputCarsCommand(carsStorage));

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Система управления автомобилями запущена.");

            ConsoleCommand helpCommand = commandRegistry.getCommand("help");

            if (helpCommand != null) {
                helpCommand.execute(new String[0]);
            } else {
                System.out.println("Команда 'help' не найдена — проверьте регистрацию команд.");
            }

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
