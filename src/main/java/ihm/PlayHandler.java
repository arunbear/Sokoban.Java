package ihm;

/**
 * Handles the play action in the application.
 */
public interface PlayHandler {
    /**
     * Handles the play action when the play button is clicked.
     */
    void handlePlay();
}

/**
 * Default implementation that creates a new LevelSelection window.
 */
class DefaultPlayHandler implements PlayHandler {
    @Override
    public void handlePlay() {
        try {
            LevelSelection.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
