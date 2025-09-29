package ihm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.jspecify.annotations.NullMarked;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Arrays;

import static java.nio.file.Files.deleteIfExists;
import static org.assertj.core.api.BDDAssertions.then;
import static swing.ComponentFinder.findComponentByNameAsType;

@NullMarked
class LevelEditorSetupTest {

    private static final String TEST_LEVEL_NAME = "testLevel";
    private static final Path TEST_LEVEL_PATH = Path.of("levels", TEST_LEVEL_NAME + ".txt");

    private static final int MIN_ROWS = 7;  // Minimum rows required by Warehouse
    private static final int MIN_COLUMNS = 5;  // Minimum columns required by Warehouse

    @BeforeEach
    void setUp() throws Exception {
        // Clean up test file if it exists
        deleteIfExists(TEST_LEVEL_PATH);

        // Clean up windows
        Arrays.stream(java.awt.Window.getWindows())
            .filter(java.awt.Window::isDisplayable)
            .forEach(java.awt.Window::dispose);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up test file after test
        deleteIfExists(TEST_LEVEL_PATH);
    }

    @Test
    void a_LevelEditorSetup_is_a_frame() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // Then
        then(editorSetup).isInstanceOf(JFrame.class);
        then(editorSetup.getTitle()).isEqualTo("Sokoban v1.0 par Gabriel FARAGO");
        then(editorSetup.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);
        then(editorSetup.getSize()).isEqualTo(new Dimension(400, 400));
        then(editorSetup.isVisible()).isTrue();
        then(editorSetup.isResizable()).isFalse();
    }

    @Test
    void a_LevelEditorSetup_has_a_title() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JLabel title = findComponentByNameAsType(editorSetup, "LevelEditorSetup.title", JLabel.class);

        // Then
        then(title).isNotNull();
        then(title.getText()).isEqualTo("Level Editor");
        then(title.getHorizontalAlignment()).isEqualTo(SwingConstants.CENTER);
    }

    @Test
    void a_LevelEditorSetup_has_an_edit_button() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JButton editButton = findComponentByNameAsType(editorSetup, "LevelEditorSetup.edit", JButton.class);

        // Then
        then(editButton).isNotNull();
        then(editButton.getText()).isEqualTo("Edit");
    }

    @Test
    void a_LevelEditorSetup_has_a_back_button() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JButton backButton = findComponentByNameAsType(editorSetup, "LevelEditorSetup.back", JButton.class);

        // Then
        then(backButton).isNotNull();
        then(backButton.getText()).isEqualTo("Back");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "testlevel1",    // contains 'level'
        "my/level",      // contains '/'
        "my\\level",     // contains '\'
    })
    void invalid_level_names_are_rejected_when_edit_button_is_clicked(String invalidName) throws Exception {
        // Given
        var editorSetup = new LevelEditorSetup();
        var editButton = findComponentByNameAsType(editorSetup, "LevelEditorSetup.edit", JButton.class);
        var nameInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.nameInput", JTextField.class);
        var rowsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.rowsInput", JTextField.class);
        var columnsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.columnsInput", JTextField.class);
        var errorLabel = findComponentByNameAsType(editorSetup, "LevelEditorSetup.errorLabel", JLabel.class);

        // Set invalid name and valid dimensions
        nameInput.setText(invalidName);
        rowsInput.setText(String.valueOf(MIN_ROWS));
        columnsInput.setText(String.valueOf(MIN_COLUMNS));

        // When
        editButton.doClick();

        then(errorLabel.getText())
            .as("Error message is shown for invalid level name: " + invalidName)
            .isEqualTo("Incorrect name!");

        then(editorSetup.isVisible())
            .as("Window remains open when validation fails for name: " + invalidName)
            .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "abc",    // letters
        "1.5",    // decimal
        "-5",     // negative
        "",       // empty
        " 5 "     // with spaces
    })
    void invalid_row_dimensions_are_rejected_when_edit_button_is_clicked(String invalidDimension) throws Exception {
        // Given
        var editorSetup = new LevelEditorSetup();
        var editButton = findComponentByNameAsType(editorSetup, "LevelEditorSetup.edit", JButton.class);
        var nameInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.nameInput", JTextField.class);
        var rowsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.rowsInput", JTextField.class);
        var columnsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.columnsInput", JTextField.class);
        var errorLabel = findComponentByNameAsType(editorSetup, "LevelEditorSetup.errorLabel", JLabel.class);

        // Set valid name but invalid dimensions
        nameInput.setText("validName");
        rowsInput.setText(invalidDimension);
        columnsInput.setText(String.valueOf(MIN_COLUMNS));

        // When
        editButton.doClick();

        then(errorLabel.getText())
            .as("Error message is shown for invalid dimension: " + invalidDimension)
            .isEqualTo("Please enter integers!");

        then(editorSetup.isVisible())
            .as("Window remains open when validation fails for dimension: " + invalidDimension)
            .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "abc",   // non-numeric
        "1.5",    // decimal
        "-5",     // negative
        "",       // empty
        " 5 "     // with spaces
    })
    void invalid_column_dimensions_are_rejected_when_edit_button_is_clicked(String invalidDimension) throws Exception {
        // Given
        var editorSetup = new LevelEditorSetup();
        var editButton   = findComponentByNameAsType(editorSetup, "LevelEditorSetup.edit",         JButton.class);
        var nameInput    = findComponentByNameAsType(editorSetup, "LevelEditorSetup.nameInput",    JTextField.class);
        var rowsInput    = findComponentByNameAsType(editorSetup, "LevelEditorSetup.rowsInput",    JTextField.class);
        var columnsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.columnsInput", JTextField.class);
        var errorLabel   = findComponentByNameAsType(editorSetup, "LevelEditorSetup.errorLabel",   JLabel.class);

        // Set valid name and valid rows but invalid columns
        nameInput.setText("validName");
        rowsInput.setText(String.valueOf(MIN_ROWS));
        columnsInput.setText(invalidDimension);

        // When
        editButton.doClick();

        then(errorLabel.getText())
            .as("Error message is shown for invalid column dimension: " + invalidDimension)
            .isEqualTo("Please enter integers!");

        then(editorSetup.isVisible())
            .as("Window remains open when validation fails for column dimension: " + invalidDimension)
            .isTrue();
    }

    @Test
    void edit_button_when_clicked_with_valid_input_disposes_window_and_shows_editor() throws Exception {
        // Given
        var editorSetup = new LevelEditorSetup();
        var editButton   = findComponentByNameAsType(editorSetup, "LevelEditorSetup.edit",         JButton.class);
        var nameInput    = findComponentByNameAsType(editorSetup, "LevelEditorSetup.nameInput",    JTextField.class);
        var rowsInput    = findComponentByNameAsType(editorSetup, "LevelEditorSetup.rowsInput",    JTextField.class);
        var columnsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.columnsInput", JTextField.class);

        // Set valid inputs
        nameInput.setText(TEST_LEVEL_NAME);
        rowsInput.setText(String.valueOf(MIN_ROWS));
        columnsInput.setText(String.valueOf(MIN_COLUMNS));

        // When
        editButton.doClick();

        then(editorSetup.isDisplayable())
            .as("Window is disposed when edit button is clicked with valid input")
            .isFalse();

        var visibleWindows = Arrays.stream(java.awt.Window.getWindows())
            .filter(java.awt.Window::isVisible)
            .toList();

        then(visibleWindows)
            .as("Exactly one window is visible (the Editor)")
            .hasSize(1);

        then(visibleWindows.getFirst())
            .isInstanceOf(ihm.Editor.class);
    }

    @Test
    void back_button_when_clicked_disposes_current_window_and_shows_home_window() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();
        JButton backButton = findComponentByNameAsType(editorSetup, "LevelEditorSetup.back", JButton.class);

        // When
        backButton.doClick();

        then(editorSetup.isDisplayable())
            .as("Window is disposed when back button is clicked")
            .isFalse();

        // Get all visible windows after clicking back
        var visibleWindows = Arrays.stream(java.awt.Window.getWindows())
            .filter(java.awt.Window::isVisible)
            .toList();

        then(visibleWindows)
            .as("Exactly one window is visible (the HomeWindow)")
            .hasSize(1);

        then(visibleWindows.getFirst())
            .as("The visible window is a HomeWindow")
            .isInstanceOf(HomeWindow.class);
    }

    @Test
    void existing_level_names_are_rejected_when_edit_button_is_clicked() throws Exception {
        // Given
        // Create a test level file first
        Files.createDirectories(TEST_LEVEL_PATH.getParent());
        Files.createFile(TEST_LEVEL_PATH);

        var editorSetup = new LevelEditorSetup();
        var editButton   = findComponentByNameAsType(editorSetup, "LevelEditorSetup.edit",         JButton.class);
        var nameInput    = findComponentByNameAsType(editorSetup, "LevelEditorSetup.nameInput",    JTextField.class);
        var rowsInput    = findComponentByNameAsType(editorSetup, "LevelEditorSetup.rowsInput",    JTextField.class);
        var columnsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.columnsInput", JTextField.class);
        var errorLabel   = findComponentByNameAsType(editorSetup, "LevelEditorSetup.errorLabel",   JLabel.class);

        // Set valid inputs but with existing level name
        nameInput.setText(TEST_LEVEL_NAME);
        rowsInput.setText(String.valueOf(MIN_ROWS));
        columnsInput.setText(String.valueOf(MIN_COLUMNS));

        // When
        editButton.doClick();

        then(errorLabel.getText())
            .as("Error message is shown for existing level name")
            .isEqualTo("This name is already in use!");

        then(editorSetup.isVisible())
            .as("Window remains open when level name exists")
            .isTrue();
    }

    @Test
    void a_LevelEditorSetup_has_a_quit_button() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JButton quitButton = findComponentByNameAsType(editorSetup, "LevelEditorSetup.quit", JButton.class);

        then(quitButton).isNotNull();
        then(quitButton.getText()).isEqualTo("Quit");
    }

    @Test
    void quit_button_triggers_quit_handler() throws Exception {
        // Given

        // We need to track if defaultExitHandler() was called from the test
        // Using AtomicInteger because:
        // 1. It allows us to modify the value inside the callback while keeping the reference final
        // 2. It's a clean way to work with the effectively final requirement for variables used in lambdas
        // 3. We use FAILURE as a sentinel value to detect if the handler was called
        final var exitStatus = new AtomicInteger(ExitHandler.FAILURE);

        LevelEditorSetup editorSetup = new LevelEditorSetup() {
            @Override
            ExitHandler defaultExitHandler() {
                return exitStatus::set;
            }
        };

        JButton quitButton = findComponentByNameAsType(editorSetup, "LevelEditorSetup.quit", JButton.class);

        // When
        quitButton.doClick();

        then(exitStatus.get())
            .isNotEqualTo(ExitHandler.FAILURE)
            .isEqualTo(ExitHandler.SUCCESS);
    }

    @Test
    void a_LevelEditorSetup_has_level_name_input() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JLabel nameLabel = findComponentByNameAsType(editorSetup, "LevelEditorSetup.nameLabel", JLabel.class);
        JTextField nameInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.nameInput", JTextField.class);

        then(nameLabel).isNotNull();
        then(nameLabel.getText()).isEqualTo("Level name:");
        then(nameLabel.getHorizontalAlignment()).isEqualTo(SwingConstants.CENTER);

        then(nameInput).isNotNull();
        then(nameInput.getColumns()).isEqualTo(15);
    }

    @Test
    void a_LevelEditorSetup_has_rows_input() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JLabel rowsLabel = findComponentByNameAsType(editorSetup, "LevelEditorSetup.rowsLabel", JLabel.class);
        JTextField rowsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.rowsInput", JTextField.class);

        then(rowsLabel).isNotNull();
        then(rowsLabel.getText()).isEqualTo("Number of rows:");
        then(rowsLabel.getHorizontalAlignment()).isEqualTo(SwingConstants.CENTER);

        then(rowsInput).isNotNull();
        then(rowsInput.getColumns()).isEqualTo(5);
    }

    @Test
    void a_LevelEditorSetup_has_columns_input() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JLabel columnsLabel = findComponentByNameAsType(editorSetup, "LevelEditorSetup.columnsLabel", JLabel.class);
        JTextField columnsInput = findComponentByNameAsType(editorSetup, "LevelEditorSetup.columnsInput", JTextField.class);

        then(columnsLabel).isNotNull();
        then(columnsLabel.getText()).isEqualTo("Number of columns:");
        then(columnsLabel.getHorizontalAlignment()).isEqualTo(SwingConstants.CENTER);

        then(columnsInput).isNotNull();
        then(columnsInput.getColumns()).isEqualTo(5);
    }

    @Test
    void a_LevelEditorSetup_has_an_error_label() throws Exception {
        // Given
        LevelEditorSetup editorSetup = new LevelEditorSetup();

        // When
        JLabel errorLabel = findComponentByNameAsType(editorSetup, "LevelEditorSetup.errorLabel", JLabel.class);

        then(errorLabel).isNotNull();
        then(errorLabel.getText()).isEmpty();
        then(errorLabel.getForeground()).isEqualTo(Color.RED);
        then(errorLabel.getHorizontalAlignment()).isEqualTo(SwingConstants.CENTER);
    }
}
