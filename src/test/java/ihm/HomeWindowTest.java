package ihm;

import org.junit.jupiter.api.Test;
import swing.ComponentFinder;

import javax.swing.*;

import java.awt.*;

import static org.assertj.core.api.BDDAssertions.then;

class HomeWindowTest {
    @Test void a_HomeWindow_has_a_frame() {
        HomeWindow homeWindow = new HomeWindow();
        JFrame frame = homeWindow.getFrame();

        then(frame).isNotNull();
        then(frame.getTitle()).isEqualTo("Sokoban v1.0 par Gabriel FARAGO");
        then(frame.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);

        then(frame.getSize()).isEqualTo(new Dimension(400, 400));
        then(frame.getLayout()).isNotNull();
        then(frame.isVisible()).isTrue();
    }

    @Test void a_HomeWindow_has_a_title() {
        HomeWindow homeWindow = new HomeWindow();
        JFrame frame = homeWindow.getFrame();

        JLabel title = ComponentFinder.findComponentByNameAsType(frame, "HomeWindow.title", JLabel.class);
        then(title).isNotNull();
        then(title.getText()).isEqualTo("SOKOBAN");
    }

    @Test void a_HomeWindow_has_a_play_button() {
        HomeWindow homeWindow = new HomeWindow();
        JFrame frame = homeWindow.getFrame();

        JButton play = ComponentFinder.findComponentByNameAsType(frame, "HomeWindow.play", JButton.class);
        then(play).isNotNull();
        then(play.getText()).isEqualTo("Play");
    }

    @Test void a_HomeWindow_has_a_quit_button() {
        HomeWindow homeWindow = new HomeWindow();
        JFrame frame = homeWindow.getFrame();

        JButton quit = ComponentFinder.findComponentByNameAsType(frame, "HomeWindow.quit", JButton.class);
        then(quit).isNotNull();
        then(quit.getText()).isEqualTo("Quit");
    }

}