package ihm;

import java.awt.Dimension;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import logic.Controller;
import logic.Direction;
import logic.Game_Managers;


public class SokobanWindow extends JFrame implements KeyListener{

    static final int TAILLE_IMAGE = 32;
    
    private int LARGEUR_FENETRE = 25 * TAILLE_IMAGE;
	private int HAUTEUR_FENETRE = 15 * TAILLE_IMAGE;
    private Controller controleur;
    private List<Direction> previousActions = new ArrayList<Direction> ();

    public SokobanWindow(Controller controleur ) {
        this.controleur = controleur;
        
        LARGEUR_FENETRE = controleur.entrepot.getNbColonnes() * TAILLE_IMAGE;
        HAUTEUR_FENETRE = controleur.entrepot.getNbLignes() * TAILLE_IMAGE;
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setPreferredSize( new Dimension( LARGEUR_FENETRE + 16, HAUTEUR_FENETRE + 39 ));
        if (controleur.isOnCustomLevel()) {
            this.setTitle( "Custom level");
        }
        else {
            this.setTitle( "Level %s".formatted(controleur.getLevel()));
        }
        this.setResizable(false);

        this.add( new SokobanPanel( controleur ));
        this.addKeyListener( this );
        this.pack();
        this.setLocationRelativeTo( null );
        this.setVisible( true );
    }

    @Override
    public void keyTyped( KeyEvent e ) {
        // nothing
    }

    @Override
    public void keyPressed( KeyEvent e ) {
        Object input = switch( e.getKeyCode() ) {
            case KeyEvent.VK_UP    -> Direction.UP;
            case KeyEvent.VK_Z    -> Direction.UP;
            case KeyEvent.VK_DOWN  -> Direction.DOWN;
            case KeyEvent.VK_S  -> Direction.DOWN;
            case KeyEvent.VK_LEFT  -> Direction.LEFT;
            case KeyEvent.VK_Q -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            case KeyEvent.VK_D -> Direction.RIGHT;
            case KeyEvent.VK_BACK_SPACE -> Game_Managers.RESTART;
            case KeyEvent.VK_SPACE -> Game_Managers.STEP_BACK;
            case KeyEvent.VK_ESCAPE -> Game_Managers.STOP;
            default                -> null;
        };
        if( input == null ) return;
        else if (input == Game_Managers.STEP_BACK) {
        	try {
				controleur.restart();
				List<Direction> New_previous_actions = new ArrayList<Direction> ();
				for (int i = 0; i < previousActions.size() - 1; i++) {
				    Direction element = previousActions.get(i);
				    New_previous_actions.add(element);
				    controleur.action(element);
				}
				previousActions = New_previous_actions;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        else if (input == Game_Managers.RESTART) {
        	try {
        		previousActions = new ArrayList<Direction> (); 
				controleur.restart();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        else if (input == Game_Managers.STOP) {
        	System.exit( 0 );
        }
        else {
        	previousActions.add((Direction) input);
        	controleur.action( (Direction) input );
        }
        repaint();
        if( controleur.levelEnd() && (this.controleur.getLevel() + 1) < 10 && this.controleur.getLevel() > 0 && controleur.getPathToLevel().contains("levels"))  {
        	Object[] options = {"Niveau suivant",
            "Quitter le jeu"};        	
        	 Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        	 int result = JOptionPane.showOptionDialog(this, "Vous avez gagné !", "Fin du niveau", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(image), options, options[0]);
        	 if (result == JOptionPane.YES_OPTION) {
        		 try {
        			this.dispose();
        			String oldPath = this.controleur.getPathToLevel();
        			new SokobanWindow(new Controller(oldPath.substring(0, oldPath.length() - 5) + Integer.toString(this.controleur.getLevel() + 1 ) + ".txt"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        		return;
        	 }
        	 else if (result == JOptionPane.NO_OPTION) {
        		 System.exit( 0 );
        	 }
        	 System.exit( 0 );
        } 
        else if (controleur.levelEnd()){
        	JOptionPane.showMessageDialog( this, "Bravo, vous avez fini le niveau !" );
            System.exit( 0 );
        }
    }
   

    @Override
    public void keyReleased( KeyEvent e ) {
        // nothing
    }
}
