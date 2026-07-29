package mainMenu;

public class HelpCommand implements ConsoleCommand {
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String getCommandText() {
        return "help";
    }

    @Override
    public String getUserGuide() {
        return "вывод списка доступных команд";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Available commands:");

        for (String commandKey : registry.getAvailableCommands()) {
            ConsoleCommand cmd = registry.getCommand(commandKey);
            System.out.println("- " + cmd.getCommandText() + ": " + cmd.getUserGuide());
        }
    }
}

