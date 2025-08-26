package ihm;

/**
 * Handles application exit with standard status codes.
 */
public interface ExitHandler {
    /** Indicates successful termination */
    int SUCCESS = 0;
    
    /** Indicates abnormal termination */
    int FAILURE = 1;
    
    /**
     * Terminates the currently running Java Virtual Machine.
     * @param status exit status - should be 0 for normal termination
     */
    void exit(int status);
}

/**
 * Default implementation that delegates to System.exit()
 */
class SystemExitHandler implements ExitHandler {
    @Override
    public void exit(int status) {
        System.exit(status);
    }
}
