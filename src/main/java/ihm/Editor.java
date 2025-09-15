package ihm;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import logic.TileType;
import logic.Controller;

@SuppressWarnings("serial")
public class Editor extends JFrame implements MouseListener, MouseMotionListener {
    private static final Logger LOGGER = Logger.getLogger(Editor.class.getName());

    private static final int TILE_SIZE = 32;

    private int windowWidth = 0;
    private int windowHeight = 0;
    private final Controller controller;
    private TileType content = TileType.OUTSIDE;

	public Editor (int rowCount, int columnCount, String name) throws IOException  {

		FileWriter levelWriter = new FileWriter(new File(new File(".").getCanonicalPath() + "/levels/" + name + ".txt"));
		BufferedWriter lowerWriter = new BufferedWriter(levelWriter);
		for (int i = 0; i < rowCount; i++ ) {
			lowerWriter.write("_".repeat(columnCount));
			lowerWriter.newLine();
		}
		lowerWriter.close();
		levelWriter.close();

		controller = new Controller(new File(new File(".").getCanonicalPath() + "/levels/" + name + ".txt").getPath());
		windowWidth = controller.warehouse.getColumns() * TILE_SIZE;
        windowHeight = controller.warehouse.getLines() * TILE_SIZE;
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setTitle("Sokoban v1.0 par Gabriel FARAGO");
        this.setPreferredSize(new Dimension(windowWidth + 150, Math.max(windowHeight + 150, 330)));
        this.setResizable(false);

        JButton gardien           = new JButton(new ImageIcon( "img/Joueur.jpg"));
        JButton background        = new JButton(new ImageIcon( "img/Background.jpg"));
        JButton caisse            = new JButton(new ImageIcon( "img/Caisse.jpg"));
        JButton caisse_rangee     = new JButton(new ImageIcon( "img/CaisseRangee.jpg"));
        JButton gardien_rangement = new JButton(new ImageIcon( "img/JoueurRangement.jpg"));
        JButton mur               = new JButton(new ImageIcon( "img/Mur.jpg"));
        JButton rangement         = new JButton(new ImageIcon( "img/Rangement.jpg"));
        JButton vide              = new JButton(new ImageIcon( "img/Vide.jpg"));

        JLabel invalid_level = new JLabel("", SwingConstants.CENTER);
	 	invalid_level.setForeground(Color.RED);
	 	invalid_level.setBounds(windowWidth, 145, 150, 20);

        JButton validate = new JButton("Terminer");
	 	validate.setBounds(windowWidth + 20, 170 , 110, 30);

        JButton back = new JButton("Retour");
	 	back.setBounds(windowWidth + 20, 210 , 110, 30);

        JButton quit = new JButton("Quitter");
	 	quit.setBounds(windowWidth + 20, 250 , 110, 30);

        gardien.setOpaque(false);
	 	gardien.setContentAreaFilled(false);
	 	gardien.setBounds(windowWidth + 30, 0, 32, 32);

        background.setOpaque(false);
	 	background.setContentAreaFilled(false);
	 	background.setBounds(windowWidth + 30, 37, 32, 32);

        caisse.setOpaque(false);
	 	caisse.setContentAreaFilled(false);
	 	caisse.setBounds(windowWidth + 30, 74, 32, 32);

        caisse_rangee.setOpaque(false);
	 	caisse_rangee.setContentAreaFilled(false);
	 	caisse_rangee.setBounds(windowWidth + 30, 111, 32, 32);

        gardien_rangement.setOpaque(false);
	 	gardien_rangement.setContentAreaFilled(false);
	 	gardien_rangement.setBounds(windowWidth + 75, 0, 32, 32);

        mur.setOpaque(false);
	 	mur.setContentAreaFilled(false);
	 	mur.setBounds(windowWidth + 75, 37, 32, 32);

        rangement.setOpaque(false);
	 	rangement.setContentAreaFilled(false);
	 	rangement.setBounds(windowWidth + 75, 74, 32, 32);

        vide.setOpaque(false);
	 	vide.setContentAreaFilled(false);
	 	vide.setBounds(windowWidth + 75, 111, 32, 32);

	 	quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File level_drop = new File(new File(".").getCanonicalPath() + "/levels/" + name + ".txt");
					level_drop.delete();
				} catch (IOException e1) {
                    LOGGER.log(Level.SEVERE, "Error while deleting level file: " + name, e1);
				}
				System.exit( 0 );
			}
	    });
	 	back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				try {
					File level_drop = new File(new File(".").getCanonicalPath() + "/levels/" + name + ".txt");
					level_drop.delete();
				} catch (IOException e2) {
                    LOGGER.log(Level.SEVERE, "Error while deleting level file when going back: " + name, e2);
				}
                new HomeWindow();

            }
	    });
	 	validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int gardien = 0;
				int nb_caisse = 0;
				int nb_target = 0;
				for (int i = 0; i < rowCount * columnCount; i++) {
					int c = i / columnCount;
					int l = i % columnCount;
					TileType end_content = controller.warehouse.getCase(c, l).getContent();
					if (end_content == TileType.WORKER_ON_FLOOR || end_content == TileType.WORKER_IN_STORAGE_AREA) {
						gardien ++;
					}
					if (end_content == TileType.UNSTORED_BOX || end_content == TileType.STORED_BOX) {
						nb_caisse ++;
					}
					if (end_content == TileType.WORKER_IN_STORAGE_AREA || end_content == TileType.STORAGE_AREA || end_content == TileType.STORED_BOX) {
						nb_target ++;
					}

				}
				if (gardien == 1 && nb_target >= nb_caisse && nb_caisse > 0) {
					String ligne = "";
					for (int i = 0; i < rowCount * columnCount; i++) {

						int c = i / columnCount;
						int l = i % columnCount;
						TileType end_content = controller.warehouse.getCase(c, l).getContent();
						switch (end_content) {
							case WORKER_ON_FLOOR:
								ligne += "G";
								break;
							case WORKER_IN_STORAGE_AREA:
								ligne += "B";
								break;
							case UNSTORED_BOX:
								ligne += "C";
								break;
							case STORED_BOX:
								ligne += "V";
								break;
							case FLOOR:
								ligne += "#";
								break;
							case STORAGE_AREA:
								ligne += "T";
								break;
							case OUTSIDE:
								ligne += "_";
								break;
							case WALL:
								ligne += "M";
								break;
						}

						if (ligne.length() == columnCount && i+1 == columnCount) {
							try {
								FileWriter levelWriter = new FileWriter(new File(new File(".").getCanonicalPath() + "/levels/" + name + ".txt"));
								BufferedWriter lowerWriter = new BufferedWriter(levelWriter);
								lowerWriter.write(ligne);
								lowerWriter.close();
								levelWriter.close();
								ligne = "";
							} catch (IOException e1) {
								LOGGER.log(Level.SEVERE, "Error while writing level file: " + name, e1);
							}
						}
						else if (ligne.length() == columnCount) {
							try {
								FileWriter levelWriter = new FileWriter(new File(new File(".").getCanonicalPath() + "/levels/" + name + ".txt"), true);
								BufferedWriter lowerWriter = new BufferedWriter(levelWriter);
								lowerWriter.newLine();
								lowerWriter.write(ligne);
								lowerWriter.close();
								levelWriter.close();
								ligne = "";
							} catch (IOException e1) {
								LOGGER.log(Level.SEVERE, "Error while writing level file: " + name, e1);
							}
						}
					}
					dispose();
                    new HomeWindow();

                }
				else {
					invalid_level.setText("Niveau invalide !");
				}
			}
	    });

	 	gardien.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.WORKER_ON_FLOOR;
			}
	    });
	 	background.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.OUTSIDE;
			}
	    });
	 	caisse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.UNSTORED_BOX;
			}
	    });
	 	caisse_rangee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.STORED_BOX;
			}
	    });
	 	gardien_rangement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.WORKER_IN_STORAGE_AREA;
			}
	    });
	 	mur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.WALL;
			}
	    });
	 	rangement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.STORAGE_AREA;
			}
	    });
	 	vide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				content = TileType.FLOOR;
			}
	    });

	 	this.add(gardien);
	 	this.add(background);
	 	this.add(caisse);
	 	this.add(caisse_rangee);
	 	this.add(gardien_rangement);
	 	this.add(mur);
	 	this.add(rangement);
	 	this.add(vide);
	 	this.add(validate);
	 	this.add(quit);
	 	this.add(back);
	 	this.add(invalid_level);
        this.add( new SokobanPanel(controller));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.pack();
        this.setVisible( true );
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getX() < windowWidth && e.getY() < windowHeight) {

			//controleur.entrepot.getCase(c-1, l).setContent(content);
			//repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getX() < windowWidth + 31 && e.getY() < windowHeight + 31) {
			int l = Math.max((e.getX() - 10) / 32, 0);
			int c = Math.max((e.getY() - 32) / 32, 0);
			controller.warehouse.getCase(c, l).setContent(content);
			repaint();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}