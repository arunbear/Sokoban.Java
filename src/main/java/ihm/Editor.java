package ihm;

import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

import com.google.common.annotations.VisibleForTesting;
import javax.swing.*;

import logic.TileType;
import logic.Controller;
import logic.LevelFile;

public class Editor extends JFrame implements MouseListener, MouseMotionListener {
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
    private static final Logger LOGGER = Logger.getLogger(Editor.class.getName());

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

        LevelFile levelFile = LevelFile.of(name);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < rowCount; i++) {
            content.append("_".repeat(columnCount))
                   .append(System.lineSeparator());
        }
        levelFile.write(content.toString());

		controller = new Controller(new File(new File(".").getCanonicalPath() + "/levels/" + name + ".txt").getPath());
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

        // Status label
        JLabel invalid_level = new JLabel("", SwingConstants.CENTER);
        invalid_level.setName(Component.ERROR_LABEL.name());
        invalid_level.setForeground(Color.RED);
        invalid_level.setBounds(windowWidth, 145, 150, 20);

        // Action buttons
        JButton save = new JButton("Save");
        save.setName(Component.SAVE_BUTTON.name());
        save.setBounds(windowWidth + 20, 170, 110, 30);

        JButton back = new JButton("Back");
        back.setName(Component.BACK_BUTTON.name());
        back.setBounds(windowWidth + 20, 210, 110, 30);

        createQuitButton(name);

        back.addActionListener(_ -> {
            dispose();
            LevelFile.of(name).delete();
            new HomeWindow();
        });

        save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int workerCount = 0;
				int boxCount = 0;
				int targetCount = 0;
				for (int i = 0; i < rowCount * columnCount; i++) {
					int c = i / columnCount;
					int l = i % columnCount;
					TileType end_content = controller.warehouse.getCase(c, l).getContent();
					if (end_content == TileType.WORKER_ON_FLOOR || end_content == TileType.WORKER_IN_STORAGE_AREA) {
						workerCount++;
					}
					if (end_content == TileType.UNSTORED_BOX || end_content == TileType.STORED_BOX) {
						boxCount++;
					}
					if (end_content == TileType.WORKER_IN_STORAGE_AREA || end_content == TileType.STORAGE_AREA || end_content == TileType.STORED_BOX) {
						targetCount++;
					}

				}
				if (workerCount == 1 && targetCount >= boxCount && boxCount > 0) {
					String line = "";
					for (int i = 0; i < rowCount * columnCount; i++) {

						int c = i / columnCount;
						int l = i % columnCount;
						TileType end_content = controller.warehouse.getCase(c, l).getContent();
						switch (end_content) {
							case WORKER_ON_FLOOR:
								line += "G";
								break;
							case WORKER_IN_STORAGE_AREA:
								line += "B";
								break;
							case UNSTORED_BOX:
								line += "C";
								break;
							case STORED_BOX:
								line += "V";
								break;
							case FLOOR:
								line += "#";
								break;
							case STORAGE_AREA:
								line += "T";
								break;
							case OUTSIDE:
								line += "_";
								break;
							case WALL:
								line += "M";
								break;
						}

						if (line.length() == columnCount && i+1 == columnCount) {
							LevelFile.of(name).write(line);
							line = "";
						}
						else if (line.length() == columnCount) {
							LevelFile.of(name).write(System.lineSeparator() + line, StandardOpenOption.APPEND);
							line = "";
						}
					}
					dispose();
                    new HomeWindow();
                }
				else {
					invalid_level.setText("Invalid level!");
				}
			}
	    });


	 	this.add(save);
	 	this.add(back);
	 	this.add(invalid_level);

        JPanel sokobanPanel = new SokobanPanel(controller);
        sokobanPanel.setName(Component.SOKOBAN_PANEL.name());
        this.add(sokobanPanel);

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.pack();
        this.setVisible( true );
	}

    private void createQuitButton(String name) {
        JButton quit = new JButton("Quit");
        quit.setName(Component.QUIT_BUTTON.name());
        quit.setBounds(windowWidth + 20, 250, 110, 30);
        quit.addActionListener(_ -> {
            LevelFile.of(name).delete();
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
}