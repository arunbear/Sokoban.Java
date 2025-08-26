package ihm;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HomeWindow {
    private final JFrame frame;

    public HomeWindow() {
        this.frame = createFrame();
        JLabel title = createTitle();

        // Create buttons
        JButton quit = createQuitButton();
        JButton play = createPlayButton(frame);
        JButton edit = createEditButton(frame);

        List.of(play, edit, quit, title).forEach(frame::add);

        configureFrame(frame);
    }

    public JFrame getFrame() {
        return frame;
    }

    private static void configureFrame(JFrame frame) {
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); // center the window
        frame.setVisible(true);
    }

    private static JButton createEditButton(JFrame frame) {
        JButton edit = new JButton("Edit levels");
        edit.setBounds(25, 300, 150, 50);
        edit.addActionListener(_ -> {
            frame.dispose();
            try {
                new LevelEditorSetup();
            } catch (FontFormatException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        return edit;
    }

    private static JButton createPlayButton(JFrame frame) {
        JButton play = new JButton("Play");
        play.setName("HomeWindow.play");
        play.setBounds(75, 225, 250, 50);
        play.addActionListener(_ -> {
            frame.dispose();
            try {
                new LevelSelection();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        return play;
    }

    private static JButton createQuitButton() {
        JButton quit = new JButton("Quit");
        quit.setName("HomeWindow.quit");
        quit.setBounds(225, 300, 150, 50);
        quit.addActionListener(_ -> System.exit(0));
        return quit;
    }

    private static JLabel createTitle() {
        JLabel title = new JLabel("SOKOBAN", SwingConstants.CENTER);
        title.setName("HomeWindow.title");
        title.setFont(new Font(Font.SERIF, Font.BOLD, 70));
        title.setBounds(0, 50, 400, 100);
        return title;
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Sokoban v1.0 par Gabriel FARAGO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        return frame;
    }
}

