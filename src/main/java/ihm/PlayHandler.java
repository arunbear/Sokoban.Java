package ihm;

import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(DefaultPlayHandler.class.getName());
    
    @Override
    public void handlePlay() {
        LOGGER.info("Handling play action - opening level selection");
        try {
            LevelSelection.create();
            LOGGER.info("Level selection window opened successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to open level selection window", e);
        }
    }
}
