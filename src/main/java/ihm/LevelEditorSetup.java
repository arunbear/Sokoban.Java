package ihm;

import com.google.common.annotations.VisibleForTesting;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * LevelEditorSetup is the first window that appears when the user wants to create a new level.
 * It asks for the level's name, number of rows and columns.
 * If the name is already in use, it will display an error message.
 * If the number of rows and columns are not integers, it will display an error message.
 * If the name is correct and the number of rows and columns are integers,
 * it will create a new level and open the level editor.
 */

public class LevelEditorSetup extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(LevelEditorSetup.class.getName());
    
    private int lineCount;
    private int columnCount;
    private String levelName;

    public LevelEditorSetup() throws FontFormatException, IOException {
        super("Sokoban v1.0 par Gabriel FARAGO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Font font_title = Font.createFont(Font.TRUETYPE_FONT, new File("font/Lostar.ttf"));
        font_title = font_title.deriveFont(Font.BOLD, 35);

        JLabel titleLabel = new JLabel("Level Editor", SwingConstants.CENTER);
        titleLabel.setName("LevelEditorSetup.title");
        titleLabel.setFont(font_title);
        titleLabel.setBounds(0, 0, 400, 50);

        JButton edit = new JButton("Edit");
        edit.setName("LevelEditorSetup.edit");

        JButton back = new JButton("Back");
        back.setName("LevelEditorSetup.back");

        JButton quit = new JButton("Quit");
        quit.setName("LevelEditorSetup.quit");

        JLabel name_display = new JLabel("Level name:", SwingConstants.CENTER);
        name_display.setName("LevelEditorSetup.nameLabel");
        name_display.setBounds(50, 90, 150, 30);

        JTextField nameInput = new JTextField(15);
        nameInput.setName("LevelEditorSetup.nameInput");
        nameInput.setBounds(220, 90, 150, 30);

        JLabel rowsLabel = new JLabel("Number of rows:", SwingConstants.CENTER);
        rowsLabel.setName("LevelEditorSetup.rowsLabel");
        rowsLabel.setBounds(50, 130, 200, 30);

        JLabel columnsLabel = new JLabel("Number of columns:", SwingConstants.CENTER);
        columnsLabel.setName("LevelEditorSetup.columnsLabel");
        columnsLabel.setBounds(50, 160, 200, 30);

        JTextField rowsInput = new JTextField(5);
        rowsInput.setName("LevelEditorSetup.rowsInput");
        rowsInput.setBounds(250, 130, 50, 30);

        JTextField columnsInput = new JTextField(5);
        columnsInput.setName("LevelEditorSetup.columnsInput");
        columnsInput.setBounds(250, 160, 50, 30);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setName("LevelEditorSetup.errorLabel");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(100, 175, 200, 50);

        edit.setBounds(75, 225, 250, 50);
        back.setBounds(25, 300, 150, 50);
        quit.setBounds(225, 300, 150, 50);

        quit.addActionListener(_ -> defaultExitHandler().exit(0));

        edit.addActionListener(_ -> {
            boolean isValidRowInput    = rowsInput.getText().matches(    "\\d+");
            boolean isValidColumnInput = columnsInput.getText().matches( "\\d+");
            boolean correctName = !nameInput.getText().isEmpty()
                               && !nameInput.getText().contains("level")
                               && !nameInput.getText().contains("/")
                               && !nameInput.getText().contains("\\");

            if (isValidRowInput && isValidColumnInput && correctName) {

                try {
                    File level = Paths.get(".").toRealPath()
                            .resolve("levels")
                            .resolve(nameInput.getText() + ".txt")
                            .toFile();
                    if (level.createNewFile()) {
                        lineCount = Integer.parseInt(rowsInput.getText());
                        columnCount = Integer.parseInt(columnsInput.getText());
                        levelName = nameInput.getText();
                        dispose();
                        new Editor(lineCount, columnCount, levelName);
                    } else {
                        errorLabel.setText("This name is already in use!");
                    }
                }
                catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to create level file", e);
                }
            } else if (!correctName) {
                errorLabel.setText("Incorrect name!");
            }
            else {
                errorLabel.setText("Please enter integers!");
            }
        });
        back.addActionListener(_ -> {
            dispose();
            new HomeWindow();
        });

        add(edit);
        add(back);
        add(quit);
        add(titleLabel);
        add(rowsInput);
        add(columnsInput);
        add(rowsLabel);
        add(columnsLabel);
        add(errorLabel);
        add(name_display);
        add(nameInput);
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @VisibleForTesting
    ExitHandler defaultExitHandler() {
        return System::exit;
    }
}