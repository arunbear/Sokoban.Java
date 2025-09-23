package ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.IntStream;

import com.google.common.annotations.VisibleForTesting;
import javax.swing.*;

import logic.TileType;
import logic.Controller;
import logic.LevelFile;

public class Editor extends JFrame implements MouseListener, MouseMotionListener {

    private final LevelFile levelFile;

    @VisibleForTesting
    enum Component {
        // Tool buttons
        PLAYER_BUTTON,
        BACKGROUND_BUTTON,
        BOX_BUTTON,
        BOX_ON_TARGET_BUTTON,
        PLAYER_ON_TARGET_BUTTON,
        WALL_BUTTON,
        TARGET_BUTTON,
        EMPTY_BUTTON,

        // Action buttons
        SAVE_BUTTON,
        BACK_BUTTON,
        QUIT_BUTTON,

        // Other components
        ERROR_LABEL,
        SOKOBAN_PANEL
    }

    @VisibleForTesting
    static final int TILE_SIZE = 32;

    @VisibleForTesting
    static final int X_OFFSET = 10;  // Horizontal offset from window edge to grid start

    private int windowWidth = 0;
    private int windowHeight = 0;
    private final Controller controller;
    private TileType content = TileType.OUTSIDE;

    @VisibleForTesting
    TileType getContent() {
        return content;
    }

	public Editor (int rowCount, int columnCount, String name) throws IOException  {

        levelFile = LevelFile.of(name);
        initializeEmptyLevel(rowCount, columnCount);

        controller = new Controller(levelFile.getFilePath().toString());
		windowWidth = controller.warehouse.getColumns() * TILE_SIZE;
        windowHeight = controller.warehouse.getLines() * TILE_SIZE;

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setTitle("Sokoban v1.0 par Gabriel FARAGO");
        this.setPreferredSize(new Dimension(windowWidth + 150, Math.max(windowHeight + 150, 330)));
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Tool buttons
        createPlayerButton();
        createBackgroundButton();
        createBoxButton();
        createBoxOnTargetButton();
        createPlayerOnTargetButton();
        createWallButton();
        createTargetButton();
        createEmptyButton();

        JLabel errorLabel = createErrorLabel();

        // Action buttons
        createBackButton();
        createQuitButton();
        JButton save = createSaveButton();

        save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

                if (isValidLevel(rowCount, columnCount)) {
					String line = "";
					for (int i = 0; i < rowCount * columnCount; i++) {

						int c = i / columnCount;
						int l = i % columnCount;
						TileType endContent = controller.warehouse.getCase(c, l).getContent();
                        line += endContent.getCode();

						if (line.length() == columnCount && i+1 == columnCount) {
							levelFile.write(line);
							line = "";
						}
						else if (line.length() == columnCount) {
							levelFile.write(System.lineSeparator() + line, StandardOpenOption.APPEND);
							line = "";
						}
					}
					dispose();
                    new HomeWindow();
                }
				else {
					errorLabel.setText("Invalid level!");
				}
			}
	    });

        JPanel sokobanPanel = new SokobanPanel(controller);
        sokobanPanel.setName(Component.SOKOBAN_PANEL.name());
        this.add(sokobanPanel);

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.pack();
        this.setVisible( true );
	}

    private void initializeEmptyLevel(int rowCount, int columnCount) {
        final var wall = TileType.WALL.codeAsString();
        String wallRow = wall.repeat(columnCount);
        String middleRow = wall
                + TileType.FLOOR.codeAsString().repeat(columnCount - 2)
                + wall;

        List<String> lines = new ArrayList<>();
        lines.add(wallRow);
        lines.addAll(Collections.nCopies(rowCount - 2, middleRow));
        lines.add(wallRow);

        levelFile.write(String.join(System.lineSeparator(), lines));
    }

    private void createBackButton() {
        JButton back = new JButton("Back");
        back.setName(Component.BACK_BUTTON.name());
        back.setBounds(windowWidth + 20, 210, 110, 30);
        back.addActionListener(_ -> {
            dispose();
            levelFile.delete();
            new HomeWindow();
        });
        this.add(back);
    }

    private void createQuitButton() {
        JButton quit = new JButton("Quit");
        quit.setName(Component.QUIT_BUTTON.name());
        quit.setBounds(windowWidth + 20, 250, 110, 30);
        quit.addActionListener(_ -> {
            levelFile.delete();
            defaultExitHandler().exit(ExitHandler.SUCCESS);
        });
        this.add(quit);
    }

    @VisibleForTesting
    ExitHandler defaultExitHandler() {
        return System::exit;
    }

    private void createPlayerButton() {
        JButton button = new JButton(new ImageIcon("img/Joueur.jpg"));
        button.setName(Component.PLAYER_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 30, 0, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.WORKER_ON_FLOOR);
        this.add(button);
    }

    private void createBackgroundButton() {
        JButton button = new JButton(new ImageIcon("img/Background.jpg"));
        button.setName(Component.BACKGROUND_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 30, 37, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.OUTSIDE);
        this.add(button);
    }

    private void createBoxButton() {
        JButton button = new JButton(new ImageIcon("img/Caisse.jpg"));
        button.setName(Component.BOX_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 30, 74, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.UNSTORED_BOX);
        this.add(button);
    }

    private void createBoxOnTargetButton() {
        JButton button = new JButton(new ImageIcon("img/CaisseRangee.jpg"));
        button.setName(Component.BOX_ON_TARGET_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 30, 111, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.STORED_BOX);
        this.add(button);
    }

    private void createPlayerOnTargetButton() {
        JButton button = new JButton(new ImageIcon("img/JoueurRangement.jpg"));
        button.setName(Component.PLAYER_ON_TARGET_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 75, 0, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.WORKER_IN_STORAGE_AREA);
        this.add(button);
    }

    private void createWallButton() {
        JButton button = new JButton(new ImageIcon("img/Mur.jpg"));
        button.setName(Component.WALL_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 75, 37, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.WALL);
        this.add(button);
    }

    private void createTargetButton() {
        JButton button = new JButton(new ImageIcon("img/Rangement.jpg"));
        button.setName(Component.TARGET_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 75, 74, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.STORAGE_AREA);
        this.add(button);
    }

    private JButton createSaveButton() {
        JButton save = new JButton("Save");
        save.setName(Component.SAVE_BUTTON.name());
        save.setBounds(windowWidth + 20, 170, 110, 30);
        this.add(save);
        return save;
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setName(Component.ERROR_LABEL.name());
        label.setForeground(Color.RED);
        label.setBounds(windowWidth, 145, 150, 20);
        this.add(label);
        return label;
    }

    private void createEmptyButton() {
        JButton button = new JButton(new ImageIcon("img/Vide.jpg"));
        button.setName(Component.EMPTY_BUTTON.name());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBounds(windowWidth + 75, 111, TILE_SIZE, TILE_SIZE);
        button.addActionListener(_ -> content = TileType.FLOOR);
        this.add(button);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() < windowWidth + 31 && e.getY() < windowHeight + 31) {
            int l = Math.max((e.getX() - X_OFFSET) / TILE_SIZE, 0);
            int c = Math.max((e.getY() - TILE_SIZE) / TILE_SIZE, 0);
            controller.warehouse.getCase(c, l).setContent(content);
            repaint();
        }
    }

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) {}

    @Override
	public void mouseReleased(MouseEvent e) {}

    @Override
	public void mouseEntered(MouseEvent e) {}

    @Override
	public void mouseExited(MouseEvent e) {}

    /**
     * Validates the level based on its dimensions and tile counts.
     * @param rowCount Number of rows in the level
     * @param columnCount Number of columns in the level
     * @return true if the level is valid, false otherwise
     */
    private boolean isValidLevel(int rowCount, int columnCount) {
        int[] tileCounts = new int[TileType.values().length];
        
        IntStream.range(0, rowCount * columnCount).forEach(i -> {
            int c = i / columnCount;
            int l = i % columnCount;
            TileType tileType = controller.warehouse.getCase(c, l).getContent();
            tileCounts[tileType.ordinal()]++;
        });
        
        return isValidLevel(tileCounts);
    }
    
    /**
     * Validates the level based on tile counts.
     * @param tileCounts Array containing counts of each tile type
     * @return true if the level is valid, false otherwise
     */
    private boolean isValidLevel(int[] tileCounts) {
        int workersFound = tileCounts[TileType.WORKER_ON_FLOOR.ordinal()]
                         + tileCounts[TileType.WORKER_IN_STORAGE_AREA.ordinal()];
        if (workersFound != 1) {
            return false;
        }

        int boxesFound = tileCounts[TileType.UNSTORED_BOX.ordinal()]
                       + tileCounts[TileType.STORED_BOX.ordinal()];
        if (boxesFound == 0) {
            return false;
        }

        int targetsFound = tileCounts[TileType.WORKER_IN_STORAGE_AREA.ordinal()]
                         + tileCounts[TileType.STORAGE_AREA.ordinal()]
                         + tileCounts[TileType.STORED_BOX.ordinal()];

        return targetsFound >= boxesFound;
    }
}