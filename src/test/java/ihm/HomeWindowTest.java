package ihm;

import org.junit.jupiter.api.Test;
import swing.ComponentFinder;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.BDDAssertions.then;

class HomeWindowTest {
    @Test void a_HomeWindow_is_a_frame() {
        HomeWindow homeWindow = new HomeWindow();

        then(homeWindow).isInstanceOf(JFrame.class);
        then(homeWindow.getTitle()).isEqualTo("Sokoban v1.0 par Gabriel FARAGO");
        then(homeWindow.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);

        then(homeWindow.getSize()).isEqualTo(new Dimension(400, 400));
        then(homeWindow.getLayout()).isNotNull();
        then(homeWindow.isVisible()).isTrue();
        then(homeWindow.isResizable()).isFalse();
    }

    @Test void a_HomeWindow_has_a_title() {
        HomeWindow homeWindow = new HomeWindow();

        JLabel title = ComponentFinder.findComponentByNameAsType(homeWindow, "HomeWindow.title", JLabel.class);
        then(title).isNotNull();
        then(title.getText()).isEqualTo("SOKOBAN");
    }

    @Test void a_HomeWindow_has_a_play_button() {
        HomeWindow homeWindow = new HomeWindow();

        JButton play = ComponentFinder.findComponentByNameAsType(homeWindow, "HomeWindow.play", JButton.class);
        then(play).isNotNull();
        then(play.getText()).isEqualTo("Play");
    }

    @Test void a_HomeWindow_has_a_quit_button() {
        HomeWindow homeWindow = new HomeWindow();

        JButton quit = ComponentFinder.findComponentByNameAsType(homeWindow, "HomeWindow.quit", JButton.class);
        then(quit).isNotNull();
        then(quit.getText()).isEqualTo("Quit");
    }

    @Test void a_HomeWindow_has_an_edit_button() {
        HomeWindow homeWindow = new HomeWindow();

        JButton edit = ComponentFinder.findComponentByNameAsType(homeWindow, "HomeWindow.edit", JButton.class);
        then(edit).isNotNull();
        then(edit.getText()).isEqualTo("Edit levels");
    }

    @Test
    void quitButton_calls_ExitHandler() {
        // Arrange
        TestExitHandler testExitHandler = new TestExitHandler();
        HomeWindow homeWindow = new HomeWindow(testExitHandler);
        JButton quit = ComponentFinder.findComponentByNameAsType(homeWindow, "HomeWindow.quit", JButton.class);
        
        // Get the action listeners
        var listeners = quit.getActionListeners();
        then(listeners).hasSize(1);
        
        // Act - Execute the action listener
        listeners[0].actionPerformed(null);
        
        // Assert - Verify exit handler was called with SUCCESS status
        then(testExitHandler.exitCalled).isTrue();
        then(testExitHandler.lastExitStatus).isEqualTo(ExitHandler.SUCCESS);
    }
    
    // Test implementation of ExitHandler
    private static class TestExitHandler implements ExitHandler {
        boolean exitCalled = false;
        int lastExitStatus = -1;
        
        @Override
        public void exit(int status) {
            exitCalled = true;
            lastExitStatus = status;
        }
    }
}