package ihm;

import logic.TileType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jspecify.annotations.NullMarked;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static java.nio.file.Files.deleteIfExists;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static swing.ComponentFinder.findComponentByNameAsType;

@NullMarked
class EditorTest {

    private static final String TEST_LEVEL_NAME = "testEditorLevel";
    private static final Path TEST_LEVEL_PATH = Path.of("levels", TEST_LEVEL_NAME + ".txt");
    private static final int TEST_ROWS = 10;
    private static final int TEST_COLUMNS = 10;
    
    private Editor editor;

    @BeforeEach
    void setUp() throws Exception {
        editor = new Editor(TEST_ROWS, TEST_COLUMNS, TEST_LEVEL_NAME);
        deleteIfExists(TEST_LEVEL_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up windows
        Arrays.stream(java.awt.Window.getWindows())
                .filter(java.awt.Window::isDisplayable)
                .forEach(java.awt.Window::dispose);

        deleteIfExists(TEST_LEVEL_PATH);
    }

    @Test
    void editor_is_a_JFrame() {
        // Then
        then(editor).isInstanceOf(JFrame.class);
        then(editor.getTitle()).isEqualTo("Sokoban v1.0 par Gabriel FARAGO");
        then(editor.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);
        then(editor.isResizable()).isFalse();
    }

    @Test
    void editor_has_required_components() {
        // Buttons
        Arrays.asList(Editor.Component.PLAYER_BUTTON,
                Editor.Component.BACKGROUND_BUTTON,
                Editor.Component.BOX_BUTTON,
                Editor.Component.BOX_ON_TARGET_BUTTON,
                Editor.Component.PLAYER_ON_TARGET_BUTTON,
                Editor.Component.WALL_BUTTON,
                Editor.Component.TARGET_BUTTON,
                Editor.Component.EMPTY_BUTTON,
                Editor.Component.SAVE_BUTTON,
                Editor.Component.BACK_BUTTON,
                Editor.Component.QUIT_BUTTON
        ).forEach(component -> {
            then(findComponentByNameAsType(editor, component.name(), JButton.class)).isNotNull();
        });

        // Other components
        then(findComponentByNameAsType(editor, Editor.Component.ERROR_LABEL.name(), JLabel.class)).isNotNull();
        then(findComponentByNameAsType(editor, Editor.Component.SOKOBAN_PANEL.name(), JPanel.class)).isNotNull();
    }

    @Test
    void editor_has_correct_mouse_listeners() {
        MouseListener[] mouseListeners = editor.getMouseListeners();
        then(mouseListeners.length).isPositive();
        
        MouseMotionListener[] mouseMotionListeners = editor.getMouseMotionListeners();
        then(mouseMotionListeners.length).isPositive();
    }
    
    @Test
    void player_button_sets_content_to_worker_on_floor() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.PLAYER_BUTTON.name(), JButton.class);
        
        // When - Simulate button click
        button.doClick();
        
        then(editor.getContent()).isEqualTo(TileType.WORKER_ON_FLOOR);
    }

    @Test
    void background_button_sets_content_to_outside() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.BACKGROUND_BUTTON.name(), JButton.class);
        
        // When - Simulate button click
        button.doClick();
        
        then(editor.getContent()).isEqualTo(TileType.OUTSIDE);
    }

    @Test
    void box_button_sets_content_to_unstored_box() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.BOX_BUTTON.name(), JButton.class);
        
        // When - Simulate button click
        button.doClick();
        
        then(editor.getContent()).isEqualTo(TileType.UNSTORED_BOX);
    }

    @Test
    void box_on_target_button_sets_content_to_stored_box() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.BOX_ON_TARGET_BUTTON.name(), JButton.class);
        
        // When - Simulate button click
        button.doClick();
        
        then(editor.getContent()).isEqualTo(TileType.STORED_BOX);
    }

    @Test
    void player_on_target_button_sets_content_to_worker_in_storage_area() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.PLAYER_ON_TARGET_BUTTON.name(), JButton.class);
        
        // When - Simulate button click
        button.doClick();
        
        then(editor.getContent()).isEqualTo(TileType.WORKER_IN_STORAGE_AREA);
    }

    @Test
    void wall_button_sets_content_to_wall() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.WALL_BUTTON.name(), JButton.class);
        
        // When - Simulate button click
        button.doClick();
        
        then(editor.getContent()).isEqualTo(TileType.WALL);
    }

    @Test
    void target_button_sets_content_to_storage_area() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.TARGET_BUTTON.name(), JButton.class);
        
        // When - Simulate button click
        button.doClick();
        
        then(editor.getContent()).isEqualTo(TileType.STORAGE_AREA);
    }

    @Test
    void empty_button_sets_content_to_floor() {
        // Given
        JButton button = findComponentByNameAsType(editor, Editor.Component.EMPTY_BUTTON.name(), JButton.class);

        // When
        button.doClick();

        // Then
        then(editor.getContent()).isEqualTo(TileType.FLOOR);
    }

    @Test
    void back_button_deletes_level_file() throws IOException {
        // Given
        Files.createDirectories(TEST_LEVEL_PATH.getParent());
        Files.createFile(TEST_LEVEL_PATH);
        then(Files.exists(TEST_LEVEL_PATH)).isTrue();

        JButton backButton = findComponentByNameAsType(editor, Editor.Component.BACK_BUTTON.name(), JButton.class);

        // When
        backButton.doClick();

        // Then
        then(Files.exists(TEST_LEVEL_PATH))
            .as("The level file no longer exists")
            .isFalse();
    }

    @Test
    void back_button_closes_editor_and_shows_home_window() {
        // Given
        JButton backButton = findComponentByNameAsType(editor, Editor.Component.BACK_BUTTON.name(), JButton.class);

        // When
        backButton.doClick();

        // Then
        then(editor.isDisplayable())
            .as("The editor window is no longer displayed")
            .isFalse();

        then(Arrays.stream(Window.getWindows())
            .filter(Window::isVisible)
            .toList())
            .as("Exactly one HomeWindow is visible")
            .hasExactlyElementsOfTypes(HomeWindow.class);
    }

    @Test
    void quit_button_triggers_exit_handler_and_deletes_file() throws IOException {
        // Given
        ExitHandler mockExitHandler = mock(ExitHandler.class);

        // Create a test level file that should be deleted
        Files.createDirectories(TEST_LEVEL_PATH.getParent());
        Files.createFile(TEST_LEVEL_PATH);
        then(Files.exists(TEST_LEVEL_PATH)).isTrue();

        editor = new Editor(TEST_ROWS, TEST_COLUMNS, TEST_LEVEL_NAME) {
            @Override
            ExitHandler defaultExitHandler() {
                return mockExitHandler;
            }
        };

        JButton quitButton = findComponentByNameAsType(editor, Editor.Component.QUIT_BUTTON.name(), JButton.class);

        // When
        quitButton.doClick();

        // Then
        then(Files.exists(TEST_LEVEL_PATH)).isFalse();
        verify(mockExitHandler).exit(ExitHandler.SUCCESS);
    }
}
