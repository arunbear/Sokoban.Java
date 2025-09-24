package ihm;

import logic.Controller;
import logic.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;

import static org.assertj.core.api.BDDAssertions.then;

class SokobanWindowTest {

    private SokobanWindow window;
    private Controller controller;

    @BeforeEach
    void setUp() {
        // Use a test level file
        String testLevelPath = Paths.get("src/test/resources/levels/test_level.txt").toAbsolutePath().toString();
        controller = new Controller(testLevelPath);
        window = new SokobanWindow(controller);
    }

    @Test
    void window_has_correct_initial_state() {
        // then
        then(window.isVisible()).isTrue();
        then(window.isResizable()).isFalse();
        then(window.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);

        // Verify the window size is set based on the warehouse
        Warehouse warehouse = controller.warehouse;
        int columns = warehouse.getColumns();
        int lines = warehouse.getLines();
        int imageWidth = columns * SokobanWindow.IMAGE_SIZE;
        int imageHeight = lines * SokobanWindow.IMAGE_SIZE;

        // Verify window is at least as large as the game content
        then(window.getSize().width).isGreaterThanOrEqualTo(imageWidth);
        then(window.getSize().height).isGreaterThanOrEqualTo(imageHeight);
    }

    @Test
    void handles_right_direction_key_press() {
        // given - initial positions
        int initialLine = controller.worker.getLine();
        int initialColumn = controller.worker.getColumn();

        // Move right - should push the box
        KeyEvent rightKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED);

        // when
        window.keyPressed(rightKey);

        // then - verify worker moved right (pushing the box)
        then(controller.worker.getLine()).isEqualTo(initialLine);
        then(controller.worker.getColumn()).isEqualTo(initialColumn + 1);
    }

    @Test
    void handles_left_direction_key_press() {
        // given - initial positions
        int initialLine = controller.worker.getLine();
        int initialColumn = controller.worker.getColumn();

        // Move left
        KeyEvent leftKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED);

        // when
        window.keyPressed(leftKey);

        // then - verify worker moved left
        then(controller.worker.getLine()).isEqualTo(initialLine);
        then(controller.worker.getColumn()).isEqualTo(initialColumn - 1);
    }

    @Test
    void handles_up_direction_key_press() {
        // given - initial positions
        int initialLine = controller.worker.getLine();
        int initialColumn = controller.worker.getColumn();

        // Move up
        KeyEvent upKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_UP,
                KeyEvent.CHAR_UNDEFINED);

        // when
        window.keyPressed(upKey);

        // then - verify worker moved up
        then(controller.worker.getLine()).isEqualTo(initialLine - 1);
        then(controller.worker.getColumn()).isEqualTo(initialColumn);
    }

    @Test
    void handles_down_direction_key_press() {
        // given - initial positions
        int initialLine = controller.worker.getLine();
        int initialColumn = controller.worker.getColumn();

        // Move down
        KeyEvent downKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_DOWN,
                KeyEvent.CHAR_UNDEFINED);

        // when
        window.keyPressed(downKey);

        // then - verify worker moved down
        then(controller.worker.getLine()).isEqualTo(initialLine + 1);
        then(controller.worker.getColumn()).isEqualTo(initialColumn);
    }

    @Test
    void handles_restart_action() {
        // given
        int initialColumn = controller.worker.getColumn();
        KeyEvent rightKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(rightKey);
        int columnAfterMove = controller.worker.getColumn();

        // when - press backspace to restart
        KeyEvent restartKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_BACK_SPACE,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(restartKey);

        // then - worker should be back at initial position
        then(controller.worker.getColumn()).isNotEqualTo(columnAfterMove);
        then(controller.worker.getColumn()).isEqualTo(initialColumn);
    }
}
