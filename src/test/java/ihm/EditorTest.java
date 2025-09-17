package ihm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jspecify.annotations.NullMarked;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.deleteIfExists;
import static org.assertj.core.api.BDDAssertions.then;
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
        // Create a new Editor instance for testing
        editor = new Editor(TEST_ROWS, TEST_COLUMNS, TEST_LEVEL_NAME);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the editor window after each test
        editor.dispose();
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
    void editor_has_required_buttons() {
        // When/Then - Verify all the editor buttons exist
        // Tool buttons
        then(findComponentByNameAsType(editor, "playerButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "backgroundButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "boxButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "boxOnTargetButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "playerOnTargetButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "wallButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "targetButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "emptyButton", JButton.class)).isNotNull();
        
        // Action buttons
        then(findComponentByNameAsType(editor, "saveButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "backButton", JButton.class)).isNotNull();
        then(findComponentByNameAsType(editor, "quitButton", JButton.class)).isNotNull();
    }

    @Test
    void editor_has_correct_panel() {
        // When/Then - Verify the SokobanPanel is present
        then(findComponentByNameAsType(editor, "sokobanPanel", JPanel.class)).isNotNull();
    }

    @Test
    void editor_has_correct_mouse_listeners() {
        MouseListener[] mouseListeners = editor.getMouseListeners();
        then(mouseListeners.length).isPositive();
        
        MouseMotionListener[] mouseMotionListeners = editor.getMouseMotionListeners();
        then(mouseMotionListeners.length).isPositive();
    }

}
