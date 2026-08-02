package utils;

public class DefaultAppTerminator implements AppTerminator {
    @Override
    public void terminate(int status) {
        System.exit(status);
    }
}
