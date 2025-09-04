package ihm;

import java.awt.Font;
import java.util.List;

import javax.swing.JButton;

import org.jspecify.annotations.NullMarked;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


@NullMarked
public class HomeWindow extends JFrame {
    private final ExitHandler exitHandler;
    private final PlayHandler playHandler;
    private final EditHandler editHandler;
    
    public HomeWindow() {
        this(ExitHandler.SYSTEM_EXIT_HANDLER, new DefaultPlayHandler(), new EditHandler.Default());
    }
    
    public HomeWindow(ExitHandler exitHandler) {
        this(exitHandler, new DefaultPlayHandler(), new EditHandler.Default());
    }
    
    public HomeWindow(PlayHandler playHandler) {
        this(ExitHandler.SYSTEM_EXIT_HANDLER, playHandler, new EditHandler.Default());
    }

    public HomeWindow(EditHandler editHandler) {
        this(ExitHandler.SYSTEM_EXIT_HANDLER, new DefaultPlayHandler(), editHandler);
    }

    public HomeWindow(ExitHandler exitHandler, PlayHandler playHandler, EditHandler editHandler) {
        super("Sokoban v1.0 par Gabriel FARAGO");
        this.exitHandler = exitHandler;
        this.playHandler = playHandler;
        this.editHandler = editHandler;
        
        initializeUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
    
    private void initializeUI() {
        JLabel title = createTitle();
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

    private JButton createEditButton(JFrame frame) {
        JButton edit = new JButton("Edit levels");
        edit.setName("HomeWindow.edit");
        edit.setBounds(25, 300, 150, 50);

        edit.addActionListener(e -> {
            frame.dispose();
            editHandler.handleEdit();
        });
        return edit;
    }

    private JButton createPlayButton(JFrame frame) {
        JButton play = new JButton("Play");
        play.setName("HomeWindow.play");
        play.setBounds(75, 225, 250, 50);
        play.addActionListener(e -> {
            frame.dispose();
            playHandler.handlePlay();
        });
        return play;
    }

    private JButton createQuitButton() {
        JButton quit = new JButton("Quit");
        quit.setName("HomeWindow.quit");
        quit.setBounds(225, 300, 150, 50);
        quit.addActionListener(e -> exitHandler.exit(ExitHandler.SUCCESS));
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

