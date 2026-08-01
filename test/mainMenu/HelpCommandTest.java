package mainMenu;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void execute_printsAvailableCommands() {
        var registry = new CommandRegistry();
        registry.register(new ExitCommand());
        registry.register(new HelpCommand(registry));

        var helpCommand = new HelpCommand(registry);
        helpCommand.execute(new String[0]);

        String output = outContent.toString();
        assertTrue(output.contains("Available commands:"));
        assertTrue(output.contains("- exit: команда для выхода из программы"));
        assertTrue(output.contains("- help: вывод списка доступных команд"));
    }
}
