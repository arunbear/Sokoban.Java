package ihm;

import org.junit.jupiter.api.Test;
import org.jspecify.annotations.NullMarked;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.BDDAssertions.then;
import static swing.ComponentFinder.findComponentByNameAsType;

@NullMarked
class LevelEditorSetupTest {
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
