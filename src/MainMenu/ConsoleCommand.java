package MainMenu;

public interface ConsoleCommand {
    String getCommandText();
    String getUserGuide();
    void execute(String[] args);
}
