package ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class LevelEditorSetup extends JFrame {
    private int lineCount;
    private int columnCount;
    private String levelName;

    public LevelEditorSetup() throws FontFormatException, IOException {
        super("Sokoban v1.0 par Gabriel FARAGO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Font font_title = Font.createFont(Font.TRUETYPE_FONT, new File("font/Lostar.ttf"));
        font_title = font_title.deriveFont(Font.BOLD, 35);

        JLabel titre = new JLabel("Level Editor", SwingConstants.CENTER);
        titre.setName("LevelEditorSetup.title");
        titre.setFont(font_title);
        titre.setBounds(0, 0, 400, 50);

        JButton edit = new JButton("Edit");
        edit.setName("LevelEditorSetup.edit");

        JButton back = new JButton("Back");
        back.setName("LevelEditorSetup.back");

        JButton quit = new JButton("Quit");


        JLabel name_display = new JLabel("Level name:", SwingConstants.CENTER);
        name_display.setBounds(50, 90, 150, 30);
        JTextField nameInput = new JTextField(15);
        nameInput.setBounds(220, 90, 150, 30);


        JLabel ligne_display = new JLabel("Number of rows:", SwingConstants.CENTER);
        ligne_display.setBounds(50, 130, 200, 30);
        JLabel colonne_display = new JLabel("Number of columns:", SwingConstants.CENTER);
        colonne_display.setBounds(50, 160, 200, 30);
        JTextField nbLignesInput = new JTextField(5);
        nbLignesInput.setBounds(250, 130, 50, 30);
        JTextField nbColonnesInput = new JTextField(5);
        nbColonnesInput.setBounds(250, 160, 50, 30);

        JLabel input_error = new JLabel("", SwingConstants.CENTER);
        input_error.setForeground(Color.RED);
        input_error.setBounds(100, 175, 200, 50);


        edit.setBounds(75, 225, 250, 50);
        back.setBounds(25, 300, 150, 50);
        quit.setBounds(225, 300, 150, 50);

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean inLigneCorrect = true;
                Boolean inColonneCorrect = true;
                Boolean correctName = true;
                if (nameInput.getText().contains("level") || nameInput.getText().contains("/") || nameInput.getText().contains("\\")) {
                    correctName = false;
                }
                for (int i = 0; i < nbLignesInput.getText().length(); i++) {
                    inLigneCorrect = (inLigneCorrect && Character.isDigit(nbLignesInput.getText().charAt(i)));
                }
                for (int i = 0; i < nbColonnesInput.getText().length(); i++) {
                    inColonneCorrect = (inColonneCorrect && Character.isDigit(nbColonnesInput.getText().charAt(i)));
                }

                if (inLigneCorrect && inColonneCorrect && correctName && nbLignesInput.getText().length() > 0 && nbColonnesInput.getText().length() > 0 && nameInput.getText().length() > 0) {

                    File level = null;
                    try {
                        level = new File(new File(".").getCanonicalPath() + "/levels/" + nameInput.getText() + ".txt");
                    } catch (IOException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                    try {
                        if (level.createNewFile()) {
                            lineCount = Integer.parseInt(nbLignesInput.getText());
                            columnCount = Integer.parseInt(nbColonnesInput.getText());
                            levelName = nameInput.getText();
                            dispose();
                            new Editor(lineCount, columnCount, levelName);
                        } else {
                            input_error.setText("This name is already in use!");
                        }
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else if (!correctName) {
                    input_error.setText("Incorrect name!");
                } else {

                    input_error.setText("Please enter integers!");
                }
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomeWindow();
            }
        });

        add(edit);
        add(back);
        add(quit);
        add(titre);
        add(nbLignesInput);
        add(nbColonnesInput);
        add(ligne_display);
        add(colonne_display);
        add(input_error);
        add(name_display);
        add(nameInput);
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}