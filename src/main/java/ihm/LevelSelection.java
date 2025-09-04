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
import org.jspecify.annotations.NullMarked;

@NullMarked
public class LevelSelection extends JFrame {

    public static LevelSelection create() throws IOException {
        return new Builder().build();
    }

    public static class Builder {
        private ExitHandler exitHandler = System::exit;
        private PlayLevelHandler playLevelHandler = (parent, levelPath) -> {
            parent.dispose();
            new SokobanWindow(new Controller(levelPath));
        };

        public Builder() {}

        public Builder withExitHandler(ExitHandler exitHandler) {
            this.exitHandler = exitHandler;
            return this;
        }

        public Builder withPlayLevelHandler(PlayLevelHandler playLevelHandler) {
            this.playLevelHandler = playLevelHandler;
            return this;
        }

        public LevelSelection build() throws IOException {
            return new LevelSelection(exitHandler, playLevelHandler);
        }
    }

    private static final String FRAME_TITLE = "Sokoban v1.0 par Gabriel FARAGO";
    private static final String defaultFileName = "level1.txt";

    private final JLabel levelFileLabel = new JLabel(defaultFileName, SwingConstants.CENTER);
    private final ExitHandler exitHandler;
    private final PlayLevelHandler playLevelHandler;
    private File selectedLevelFile;

    private LevelSelection(ExitHandler exitHandler, PlayLevelHandler playLevelHandler) throws IOException {
        this.exitHandler = exitHandler;
        this.playLevelHandler = playLevelHandler;

        selectedLevelFile = new File("%s/levels/%s".formatted(
            new File(".").getCanonicalPath(),
            defaultFileName)
        );

        initializeUI();
    }

    private void initializeUI() {
        add(aPlayButton());
        add(aBackButton());
        add(aQuitButton());
        add(aTitle());
        add(aListOfLevels());
        add(aBrowseButton());

        levelFileLabel.setBounds(50, 150, 300, 50);
        add(levelFileLabel);

        configureFrame();
    }

    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private JButton aBrowseButton() {
        Icon browse_icon = new ImageIcon("img/browser.png");

        JButton browse = new JButton(browse_icon);
        browse.setName("LevelSelection.browse");
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
        levelList.setName("LevelSelection.levelList");
        levelList.setSelectedIndex(0);
        levelList.setBounds(50, 100, 100, 50);

        levelList.addActionListener(e -> {
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
        play.setName("LevelSelection.play");

        play.setBounds(75, 225, 250, 50);
        play.addActionListener(e -> playLevelHandler.playLevel(LevelSelection.this, selectedLevelFile.getPath()));
        return play;
    }

    private JButton aBackButton() {
        JButton back = new JButton("Back");
        back.setName("LevelSelection.back");
        back.setBounds(25, 300, 150, 50);
        back.addActionListener(e -> {
            dispose();
            new HomeWindow();
        });
        return back;
    }

    private JButton aQuitButton() {
        JButton quit = new JButton("Quit");
        quit.setName("LevelSelection.quit");
        quit.setBounds(225, 300, 150, 50);

        quit.addActionListener(e -> exitHandler.exit(ExitHandler.SUCCESS));
        return quit;
    }

    private JLabel aTitle() {
        JLabel title = new JLabel("Select Level", SwingConstants.CENTER);
        title.setName("LevelSelection.title");
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
