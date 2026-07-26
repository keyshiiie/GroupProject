package MainMenu;

public class ExitCommand implements ConsoleCommand {
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
        System.out.println("Выход из приложения...");
        System.exit(0);
    }
}
