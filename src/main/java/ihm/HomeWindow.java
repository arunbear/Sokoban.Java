package ihm;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HomeWindow {
    public HomeWindow() {
        JFrame frame = new JFrame("Sokoban v1.0 par Gabriel FARAGO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JLabel title = new JLabel("SOKOBAN", SwingConstants.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 70));
        title.setBounds(0, 50, 400, 100);

        JButton play = new JButton("Play");
        JButton edit = new JButton("Edit levels");
        JButton quit = new JButton("Quit");

        play.setBounds(75, 225, 250, 50);
        edit.setBounds(25, 300, 150, 50);
        quit.setBounds(225, 300, 150, 50);

        quit.addActionListener(_ -> System.exit(0));
        play.addActionListener(_ -> {
            frame.dispose();
            try {
                new LevelSelection();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        edit.addActionListener(_ -> {
            frame.dispose();
            try {
                new LevelEditorSetup();
            } catch (FontFormatException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        frame.add(play);
        frame.add(edit);
        frame.add(quit);
        frame.add(title);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); // center the window
        frame.setVisible(true);

    }
}

