package ihm;

import javax.swing.JFrame;

/**
 * Handles the back action in the application.
 */
@FunctionalInterface
public interface BackToHomeHandler {
    /**
     * Handles the back action when the back button is clicked.
     * @param currentWindow The current window that should be disposed when going back
     */
    void handleBack(JFrame currentWindow);
}
