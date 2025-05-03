package ihm;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import logic.Controller;

public class LevelSelection {
    private static File path_selected;

    public LevelSelection() throws IOException {

        path_selected = new File(new File(".").getCanonicalPath() + "/levels/level1.txt");

        JFrame levelSelection = new JFrame("Sokoban v1.0 par Gabriel FARAGO");
        levelSelection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel path = new JLabel("level1.txt", SwingConstants.CENTER);
        path.setBounds(50, 150, 300, 50);

        JButton browse = createBrowseButton(path);

        JLabel title = createTitle();
        JButton quit = createQuitButton();
        JButton back = createBackButton(levelSelection);
        JButton play = createPlayButton(levelSelection);

        JComboBox<String> levelList = createLevelList(path);

        levelSelection.add(play);
        levelSelection.add(back);
        levelSelection.add(quit);
        levelSelection.add(title);
        levelSelection.add(levelList);
        levelSelection.add(browse);
        levelSelection.add(path);
        levelSelection.setSize(400, 400);
        levelSelection.setLayout(null);
        levelSelection.setLocationRelativeTo(null);
        levelSelection.setVisible(true);

    }

    private static JButton createBrowseButton(JLabel path) {
        Icon browse_icon = new ImageIcon("img/browser.png");

        JButton browse = new JButton(browse_icon);
        browse.setOpaque(false);
        browse.setContentAreaFilled(false);
        browse.setBounds(250, 100, 100, 50);

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileBrowser = new JFileChooser();
                File f = null;
                try {
                    f = new File(new File(".").getCanonicalPath() + "/levels");
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                fileBrowser.setCurrentDirectory(f);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileBrowser.setFileFilter(filter);
                int returnVal = fileBrowser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String browser_result = fileBrowser.getSelectedFile().getName();
                    path.setText(browser_result);
                    path_selected = fileBrowser.getSelectedFile();
                }

            }
        });
        return browse;
    }

    private static JComboBox<String> createLevelList(JLabel path) {
        String[] existingLevels =
            IntStream.range(1, 10 + 1)
            .mapToObj("Level %s"::formatted)
            .toArray(String[]::new);

        JComboBox<String> levelList = new JComboBox<>(existingLevels);
        levelList.setSelectedIndex(0);
        levelList.setBounds(50, 100, 100, 50);

        levelList.addActionListener(_ -> {
            var fileName = "level%d.txt".formatted(levelList.getSelectedIndex() + 1);
            path.setText(fileName);
            try {
                path_selected = new File(new File(".").getCanonicalPath() + "/levels/%s".formatted(fileName));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return levelList;
    }

    private static JButton createPlayButton(JFrame levelSelection) {
        JButton play = new JButton("Play");

        play.setBounds(75, 225, 250, 50);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelSelection.dispose();
                try {
                    new SokobanWindow(new Controller(path_selected.getPath()));
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        return play;
    }

    private static JButton createBackButton(JFrame levelSelection) {
        JButton back = new JButton("Back");
        back.setBounds(25, 300, 150, 50);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelSelection.dispose();
                new HomeWindow();
            }
        });
        return back;
    }

    private static JButton createQuitButton() {
        JButton quit = new JButton("Quit");
        quit.setBounds(225, 300, 150, 50);

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return quit;
    }

    private static JLabel createTitle() {
        JLabel title = new JLabel("CHOOSE A LEVEL", SwingConstants.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 40));
        title.setBounds(0, 20, 400, 50);
        return title;
    }

}
