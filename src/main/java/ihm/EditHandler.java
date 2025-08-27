package ihm;

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
        @Override
        public void handleEdit() {
            try {
                new LevelEditorSetup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
