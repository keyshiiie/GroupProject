package mainMenu;

public interface ConsoleCommand {
    String getCommandText();
    String getUserGuide();
    void execute(String[] args);
}
