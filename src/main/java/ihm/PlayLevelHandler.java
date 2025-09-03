package ihm;

import javax.swing.JFrame;

/**
 * Handles starting a new game level with the specified level file.
 */
@FunctionalInterface
public interface PlayLevelHandler {
    /**
     * Starts a new game level with the specified level file.
     * @param parent The parent JFrame that should be disposed after starting the game
     * @param levelPath The path to the level file to load
     */
    void playLevel(JFrame parent, String levelPath);
}
