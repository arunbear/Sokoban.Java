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
}
