package ihm;

public interface ExitHandler {
    void exit(int status);
}

class SystemExitHandler implements ExitHandler {
    @Override
    public void exit(int status) {
        System.exit(status);
    }
}
