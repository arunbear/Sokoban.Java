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
}
