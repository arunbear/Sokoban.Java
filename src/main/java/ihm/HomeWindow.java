package ihm;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HomeWindow extends JFrame {
    public HomeWindow() {
        super("Sokoban v1.0 par Gabriel FARAGO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        JLabel title = createTitle();
        
        // Create buttons
        JButton quit = createQuitButton();
        JButton play = createPlayButton(this);
        JButton edit = createEditButton(this);
        
        List.of(play, edit, quit, title).forEach(this::add);
        
        configureFrame();
    }

    private void configureFrame() {
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null); // center the window
        setVisible(true);
    }

    private static JButton createEditButton(JFrame frame) {
        JButton edit = new JButton("Edit levels");
        edit.setName("HomeWindow.edit");
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

}

