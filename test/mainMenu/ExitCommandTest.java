package mainMenu;

import utils.AppTerminator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExitCommandTest {

    @Test
    void getCommandText_returnsExit() {
        var cmd = new ExitCommand();
        assertEquals("exit", cmd.getCommandText());
    }

    @Test
    void getUserGuide_returnsDescription() {
        var cmd = new ExitCommand();
        assertTrue(cmd.getUserGuide().contains("выход"));
    }

    @Test
    void execute_callsTerminator() {
        var terminated = new boolean[]{false};

        AppTerminator mockTerminator = status -> {
            terminated[0] = true;
        };

        var cmd = new ExitCommand(mockTerminator);
        cmd.execute(new String[0]);

        assertTrue(terminated[0], "AppTerminator должен быть вызван при выполнении команды exit");
    }
}