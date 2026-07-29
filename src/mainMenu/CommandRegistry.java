package mainMenu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandRegistry {
    private final Map<String, ConsoleCommand> commands = new HashMap<>();

    public void register(ConsoleCommand command) {
        String key = command.getCommandText().toLowerCase();

        if (commands.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate command: " + key);
        }

        commands.put(key, command);
    }

    public ConsoleCommand getCommand(String text) {
        return commands.get(text.toLowerCase());
    }

    public Set<String> getAvailableCommands() {
        return Collections.unmodifiableSet(commands.keySet());
    }
}
