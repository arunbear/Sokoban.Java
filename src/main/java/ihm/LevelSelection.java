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
    private static final String defaultFileName = "level1.txt";
    private static File selectedLevelFile;

    private final JFrame frame = new JFrame("Sokoban v1.0 par Gabriel FARAGO");
    private final JLabel levelFileLabel = new JLabel(defaultFileName, SwingConstants.CENTER);

    public LevelSelection() throws IOException {

        selectedLevelFile = new File("%s/levels/%s".formatted(
            new File(".").getCanonicalPath(),
            defaultFileName)
        );

        levelFileLabel.setBounds(50, 150, 300, 50);

        frame.add(aPlayButton());
        frame.add(aBackButton());
        frame.add(aQuitButton());
        frame.add(aTitle());
        frame.add(aListOfLevels());
        frame.add(aBrowseButton());
        frame.add(levelFileLabel);

        configureFrame();
    }

    private void configureFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton aBrowseButton() {
        Icon browse_icon = new ImageIcon("img/browser.png");

        JButton browse = new JButton(browse_icon);
        browse.setToolTipText("Browse for a level file");
        browse.setOpaque(false);
        browse.setContentAreaFilled(false);
        browse.setBounds(250, 100, 100, 50);

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileBrowser = makeFileChooser();

                int returnVal = fileBrowser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String browser_result = fileBrowser.getSelectedFile().getName();
                    levelFileLabel.setText(browser_result);
                    selectedLevelFile = fileBrowser.getSelectedFile();
                }
            }

        });
        return browse;
    }

    private JComboBox<String> aListOfLevels() {
        String[] existingLevels =
            IntStream.range(1, 10 + 1)
            .mapToObj("Level %s"::formatted)
            .toArray(String[]::new);

        JComboBox<String> levelList = new JComboBox<>(existingLevels);
        levelList.setSelectedIndex(0);
        levelList.setBounds(50, 100, 100, 50);

        levelList.addActionListener(_ -> {
            var fileName = "level%d.txt".formatted(levelList.getSelectedIndex() + 1);
            levelFileLabel.setText(fileName);
            try {
                selectedLevelFile = new File(new File(".").getCanonicalPath() + "/levels/%s".formatted(fileName));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return levelList;
    }

    private JButton aPlayButton() {
        JButton play = new JButton("Play");

        play.setBounds(75, 225, 250, 50);
        play.addActionListener(e -> {
            frame.dispose();
            try {
                new SokobanWindow(new Controller(selectedLevelFile.getPath()));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        return play;
    }

    private JButton aBackButton() {
        JButton back = new JButton("Back");
        back.setBounds(25, 300, 150, 50);
        back.addActionListener(_ -> {
            frame.dispose();
            new HomeWindow();
        });
        return back;
    }

    private static JButton aQuitButton() {
        JButton quit = new JButton("Quit");
        quit.setBounds(225, 300, 150, 50);

        quit.addActionListener(_ -> System.exit(0));
        return quit;
    }

    private static JLabel aTitle() {
        JLabel title = new JLabel("CHOOSE A LEVEL", SwingConstants.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 40));
        title.setBounds(0, 20, 400, 50);
        return title;
    }

    private static JFileChooser makeFileChooser() {
        JFileChooser fileBrowser = new JFileChooser();

        File browseDirectory;
        try {
            browseDirectory = new File("%s/levels".formatted(new File(".").getCanonicalPath()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        fileBrowser.setCurrentDirectory(browseDirectory);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileBrowser.setFileFilter(filter);
        return fileBrowser;
    }
}
