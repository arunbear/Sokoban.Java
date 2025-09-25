package ihm;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the edit action when the edit button is clicked.
 */
public interface EditHandler {
    /**
     * Handles the edit action.
     */
    void handleEdit();

    /**
     * Default implementation that creates a new LevelEditorSetup.
     */
    class Default implements EditHandler {
        private static final Logger LOGGER = Logger.getLogger(Default.class.getName());

        @Override
        public void handleEdit() {
            try {
                new LevelEditorSetup();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to open Level Editor Setup", e);
            }
        }
    }
}
