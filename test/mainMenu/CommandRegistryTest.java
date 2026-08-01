package mainMenu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    @Test
    void register_and_getCommand() {
        CommandRegistry registry = new CommandRegistry();
        ConsoleCommand cmd = new HelpCommand(registry);

        registry.register(cmd);
        assertEquals(cmd, registry.getCommand("help"));
        assertEquals(cmd, registry.getCommand("HELP")); // регистронезависимость
    }

    @Test
    void duplicateCommand_throws() {
        CommandRegistry registry = new CommandRegistry();
        ConsoleCommand cmd1 = new HelpCommand(registry);
        ConsoleCommand cmd2 = new ExitCommand();

        registry.register(cmd1);
        // меняем текст команды, чтобы сделать её «дубликатом» по ключу
        // для теста можно создать простую реализацию ConsoleCommand
        ConsoleCommand duplicate = new ConsoleCommand() {
            @Override public String getCommandText() { return "help"; }
            @Override public String getUserGuide() { return ""; }
            @Override public void execute(String[] args) {}
        };

        assertThrows(IllegalArgumentException.class, () -> registry.register(duplicate));
    }

    @Test
    void getAvailableCommands_returnsUnmodifiableSet() {
        CommandRegistry registry = new CommandRegistry();
        ConsoleCommand cmd = new ExitCommand();
        registry.register(cmd);

        var available = registry.getAvailableCommands();
        assertTrue(available.contains("exit"));

        // проверка, что сет неизменяемый
        assertThrows(UnsupportedOperationException.class, () -> available.add("test"));
    }
}
