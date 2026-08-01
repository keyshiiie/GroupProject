package mainMenu;

import util.AppTerminator;
import util.DefaultAppTerminator;

public class ExitCommand implements ConsoleCommand {
    private final AppTerminator terminator;

    public ExitCommand() {
        this(new DefaultAppTerminator());
    }

    public ExitCommand(AppTerminator terminator) {
        this.terminator = terminator;
    }

    @Override
    public String getCommandText() {
        return "exit";
    }

    @Override
    public String getUserGuide() {
        return "команда для выхода из программы";
    }

    @Override
    public void execute(String[] args) {
        terminator.terminate(0);
    }
}
