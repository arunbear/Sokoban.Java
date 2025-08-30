package ihm;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;
import static swing.ComponentFinder.findComponentByNameAsType;

class LevelSelectionTest {
    @Test 
    void a_LevelSelection_is_a_frame() throws IOException {
        LevelSelection levelSelection = new LevelSelection();

        then(levelSelection).isInstanceOf(JFrame.class);
        then(levelSelection.getTitle()).isEqualTo("Sokoban v1.0 par Gabriel FARAGO");
        then(levelSelection.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);

        then(levelSelection.getSize()).isEqualTo(new Dimension(400, 400));
        then(levelSelection.getLayout()).isNotNull();
        then(levelSelection.isVisible()).isTrue();
        then(levelSelection.isResizable()).isFalse();
    }

    @Test
    void a_LevelSelection_has_a_title() throws IOException {
        LevelSelection levelSelection = new LevelSelection();

        JLabel title = findComponentByNameAsType(levelSelection, "LevelSelection.title", JLabel.class);
        then(title).isNotNull();
        then(title.getText()).isEqualTo("Select Level");
    }

    @Test
    void a_LevelSelection_has_a_play_button() throws IOException {
        LevelSelection levelSelection = new LevelSelection();

        JButton play = findComponentByNameAsType(levelSelection, "LevelSelection.play", JButton.class);
        then(play).isNotNull();
        then(play.getText()).isEqualTo("Play");
    }

    @Test
    void a_LevelSelection_has_a_back_button() throws IOException {
        LevelSelection levelSelection = new LevelSelection();

        JButton back = findComponentByNameAsType(levelSelection, "LevelSelection.back", JButton.class);
        then(back).isNotNull();
        then(back.getText()).isEqualTo("Back");
    }

    @Test
    void a_LevelSelection_has_a_quit_button() throws IOException {
        LevelSelection levelSelection = new LevelSelection();

        JButton quit = findComponentByNameAsType(levelSelection, "LevelSelection.quit", JButton.class);
        then(quit).isNotNull();
        then(quit.getText()).isEqualTo("Quit");
    }

    @Test
    void a_LevelSelection_has_a_level_selector() throws IOException {
        LevelSelection levelSelection = new LevelSelection();

        JComboBox<?> levelList = findComponentByNameAsType(levelSelection, "LevelSelection.levelList", JComboBox.class);
        then(levelList).isNotNull();
        then(levelList.getItemCount()).isGreaterThan(0);
    }

    @Test
    void a_LevelSelection_has_a_browse_button() throws IOException {
        LevelSelection levelSelection = new LevelSelection();

        JButton browse = findComponentByNameAsType(levelSelection, "LevelSelection.browse", JButton.class);
        then(browse).isNotNull();
        then(browse.getToolTipText()).isEqualTo("Browse for a level file");
    }
}
