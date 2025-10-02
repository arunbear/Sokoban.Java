package ihm;

import logic.Controller;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.stream.Stream;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

@NullMarked
class SokobanWindowTest {

    private static final String DEFAULT_TEST_LEVEL = "src/test/resources/levels/test_level.txt";
    private SokobanWindow window;
    private Controller controller;

    @BeforeEach
    void setUp() {
        // Use the default test level file for all tests
        window = createTestWindow();
        controller = window.getController();
    }

    @AfterEach
    void tearDown() {

        // Clean up windows
        Arrays.stream(java.awt.Window.getWindows())
                .filter(java.awt.Window::isDisplayable)
                .forEach(java.awt.Window::dispose);
    }

    /**
     * Creates and returns a test window with the default test level.
     * This is the happy path setup used by most tests.
     *
     * @return A new SokobanWindow instance with the default test level
     */
    private SokobanWindow createTestWindow() {
        return createTestWindow(DEFAULT_TEST_LEVEL);
    }

    /**
     * Creates and returns a test window with the specified level file.
     *
     * @param levelPath Path to the level file to load
     * @return A new SokobanWindow instance with the specified level
     */
    private SokobanWindow createTestWindow(String levelPath) {
        String testLevelPath = Paths.get(levelPath).toAbsolutePath().toString();
        controller = new Controller(testLevelPath);
        window = new SokobanWindow(controller);
        return window;
    }

    @Test
    void window_has_correct_initial_state() {
        // then
        then(window).isNotNull();
        then(window.isVisible()).isTrue();

        // The test level should be treated as a custom level
        then(window.getController().isOnCustomLevel()).isTrue();
        then(window.getTitle()).isEqualTo("Custom level");
        then(window.isResizable()).isFalse();

        // Verify window size is at least as large as the game content
        int columns = controller.getWarehouse().getColumns();
        int lines = controller.getWarehouse().getLines();
        int imageWidth = columns * SokobanWindow.IMAGE_SIZE;
        int imageHeight = lines * SokobanWindow.IMAGE_SIZE;

        // Verify window is at least as large as the game content
        then(window.getSize().width).isGreaterThanOrEqualTo(imageWidth);
        then(window.getSize().height).isGreaterThanOrEqualTo(imageHeight);
    }

    @Test
    void window_title_reflects_standard_level() {
        // given - a standard level (level 1)
        String levelPath = "levels/level1.txt";

        // when - creating window with standard level using helper method
        window = createTestWindow(levelPath);

        // then - should be a standard level with level number 1
        then(window.getController().isOnCustomLevel()).isFalse();
        then(window.getController().getLevel()).isEqualTo(1);
        then(window.getTitle()).isEqualTo("Level 1");
    }

    @Test
    void handles_right_direction_key_press() {
        // given - initial positions
        int initialLine = controller.getWorker().getLine();
        int initialColumn = controller.getWorker().getColumn();

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
        then(controller.getWorker().getLine()).isEqualTo(initialLine);
        then(controller.getWorker().getColumn()).isEqualTo(initialColumn + 1);
    }

    @Test
    void handles_left_direction_key_press() {
        // given - initial positions
        int initialLine = controller.getWorker().getLine();
        int initialColumn = controller.getWorker().getColumn();

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
        then(controller.getWorker().getLine()).isEqualTo(initialLine);
        then(controller.getWorker().getColumn()).isEqualTo(initialColumn - 1);
    }

    @Test
    void handles_up_direction_key_press() {
        // given - initial positions
        int initialLine = controller.getWorker().getLine();
        int initialColumn = controller.getWorker().getColumn();

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
        then(controller.getWorker().getLine()).isEqualTo(initialLine - 1);
        then(controller.getWorker().getColumn()).isEqualTo(initialColumn);
    }

    @Test
    void handles_down_direction_key_press() {
        // given - initial positions
        int initialLine = controller.getWorker().getLine();
        int initialColumn = controller.getWorker().getColumn();

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
        then(controller.getWorker().getLine()).isEqualTo(initialLine + 1);
        then(controller.getWorker().getColumn()).isEqualTo(initialColumn);
    }

    private static Stream<Arguments> directionKeyCodes() {
        return Stream.of(
            Arguments.of(KeyEvent.VK_UP, "UP"),
            Arguments.of(KeyEvent.VK_RIGHT, "RIGHT"),
            Arguments.of(KeyEvent.VK_DOWN, "DOWN"),
            Arguments.of(KeyEvent.VK_LEFT, "LEFT")
        );
    }

    @ParameterizedTest(name = "Player cannot move {1} when obstructed")
    @MethodSource("directionKeyCodes")
    void player_cannot_move_when_obstructed(int keyCode, String directionName) {
        // given - load the no-moves test level
        window = createTestWindow("src/test/resources/levels/test_level_no_moves.txt");

        // Record initial position
        int initialLine = controller.getWorker().getLine();
        int initialColumn = controller.getWorker().getColumn();

        // when - try to move in the specified direction
        KeyEvent keyEvent = new KeyEvent(
            window,
            KeyEvent.KEY_PRESSED,
            System.currentTimeMillis(),
            0,
            keyCode,
            KeyEvent.CHAR_UNDEFINED
        );
        window.keyPressed(keyEvent);

        // then - verify the player didn't move
        then(controller.getWorker().getLine())
            .as("Player did not move from line %d when moving %s".formatted(initialLine, directionName))
            .isEqualTo(initialLine);
        then(controller.getWorker().getColumn())
            .as("Player did not move from column %d when moving %s".formatted(initialColumn, directionName))
            .isEqualTo(initialColumn);
    }

    @Test
    void handles_restart_action() {
        // given
        int initialColumn = controller.getWorker().getColumn();
        KeyEvent rightKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(rightKey);
        int columnAfterMove = controller.getWorker().getColumn();

        // when - press backspace to restart
        KeyEvent restartKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_BACK_SPACE,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(restartKey);

        // then - worker should be back at initial position
        then(controller.getWorker().getColumn()).isNotEqualTo(columnAfterMove);
        then(controller.getWorker().getColumn()).isEqualTo(initialColumn);
    }

    @Test
    void can_undo_the_last_move() {
        // given - make a move
        int initialLine   = controller.getWorker().getLine();
        int initialColumn = controller.getWorker().getColumn();

        // Move right
        KeyEvent rightKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(rightKey);

        // when - press space to step back
        KeyEvent spaceKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_SPACE,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(spaceKey);

        // then - worker should be back at initial position
        then(controller.getWorker().getLine())
            .as("line position is restored when undoing last move")
            .isEqualTo(initialLine);
        then(controller.getWorker().getColumn())
            .as("column position is restored when undoing last move")
            .isEqualTo(initialColumn);
    }

    @Test
    void resets_level_when_backspace_key_is_pressed() {
        // given - make a move and change the level state
        int initialLine   = controller.getWorker().getLine();
        int initialColumn = controller.getWorker().getColumn();

        // Move right to change the state
        KeyEvent rightKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(rightKey);
        window.keyPressed(rightKey); // again

        // when - press backspace to reset the level
        KeyEvent resetKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_BACK_SPACE,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(resetKey);

        // then - worker should be back at initial position
        then(controller.getWorker().getLine())
            .as("line position is restored when resetting level")
            .isEqualTo(initialLine);
        then(controller.getWorker().getColumn())
            .as("column position is restored when resetting level")
            .isEqualTo(initialColumn);
    }

    @Test
    void completing_a_custom_level_disposes_window_and_shows_home_window() {
        // given - create a test window with the simplified level that only needs one push
        window = createTestWindow("src/test/resources/levels/test_level_end.txt");

        // Simulate the user clicking OK to close the level completion dialog.
        // This has to be done before the move to push the box to the target
        // because the dialog will wait for user input
        Timer timer = new Timer(10, _ -> window.clickOkButton());
        timer.setRepeats(false);
        SwingUtilities.invokeLater(timer::start);

        // When: Perform a single move to push the box onto the target and end the level
        KeyEvent rightKey = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(rightKey);

        then(window.isShowing())
                .as("Window is disposed after level completion and OK click")
                .isFalse();

        then(Window.getWindows())
                .as("Contains a visible HomeWindow")
                .anyMatch(w -> w instanceof HomeWindow && w.isShowing());
    }

    @Test
    void completing_level_1_loads_next_level() {
        // given - create a test window with the simplified test level
        window = createTestWindow("src/test/resources/levels/level1.txt");

        // Simulate the user clicking Next Level to proceed to the next level
        // This has to be done before the move to push the box to the target
        // because the dialog will wait for user input
        Timer timer = new Timer(10, _ -> window.clickNextLevelButton());
        timer.setRepeats(false);
        SwingUtilities.invokeLater(timer::start);

        // When: Perform a single move to push the box onto the target and end the level
        pressKey(KeyEvent.VK_RIGHT);

        then(window.isShowing())
                .as("Current window is disposed after level completion")
                .isFalse();

        then(Window.getWindows())
                .as("A new window with title 'Level 2' is created")
                .anyMatch(w -> w instanceof SokobanWindow sw && sw.getTitle().equals("Level 2"));
    }

    private void pressKey(int keyCode) {
        KeyEvent keyEvent = new KeyEvent(window,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                keyCode,
                KeyEvent.CHAR_UNDEFINED);
        window.keyPressed(keyEvent);
    }
}
