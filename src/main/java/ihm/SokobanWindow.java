package ihm;

import java.awt.Dimension;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import logic.Controller;
import logic.Direction;
import logic.GameAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SokobanWindow extends JFrame implements KeyListener{

    public static final int IMAGE_SIZE = 32;

    public Controller getController() {
        return controller;
    }

    private final Controller controller;
    private final List<Direction> previousActions = new ArrayList<>();

    public SokobanWindow(Controller controller) {
        this.controller = controller;

        int IMAGE_WIDTH = controller.getWarehouse().getColumns() * IMAGE_SIZE;
        int IMAGE_HEIGHT = controller.getWarehouse().getLines() * IMAGE_SIZE;

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setPreferredSize( new Dimension( IMAGE_WIDTH + 16, IMAGE_HEIGHT + 39 ));
        if (controller.isOnCustomLevel()) {
            this.setTitle( "Custom level");
        }
        else {
            this.setTitle( "Level %s".formatted(controller.getLevel()));
        }
        this.setResizable(false);

        this.add(new SokobanPanel(controller.getWarehouse()));
        this.addKeyListener(this);
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
            case KeyEvent.VK_UP,    KeyEvent.VK_Z -> Direction.UP;
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S -> Direction.DOWN;
            case KeyEvent.VK_LEFT,  KeyEvent.VK_Q -> Direction.LEFT;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.RIGHT;
            case KeyEvent.VK_BACK_SPACE -> GameAction.RESTART;
            case KeyEvent.VK_SPACE      -> GameAction.STEP_BACK;
            case KeyEvent.VK_ESCAPE     -> GameAction.STOP;
            default -> null;
        };
        if( input == null ) {
            return;
        }
        else if (input instanceof Direction direction) {
            previousActions.add(direction);
            controller.action(direction);
        }
        else if (input == GameAction.STEP_BACK && !previousActions.isEmpty()) {
            controller.restart();

            // replay all but the last move
            previousActions.removeLast();
            for (var direction : previousActions) {
                controller.action(direction);
            }
        }
        else if (input == GameAction.RESTART) {
            previousActions.clear();
            controller.restart();
        }
        else if (input == GameAction.STOP) {
            exitGame();
        }

        repaint();
        if (controller.levelEnd()) {
            handleEndOfLevel();
        }
    }

    private void handleEndOfLevel() {
        if (controller.isOnCustomLevel()) {
            JOptionPane.showMessageDialog(this, "Congratulations, you've completed the level!");
            exitGame();
        }
        else {
            handleNextLevel();
        }
    }

    private void handleNextLevel() {
        Object[] options = {"Next level", "Quit"};
        Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        int result = JOptionPane.showOptionDialog(this,
                "You won!",
                "End of level",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(image),
                options,
                options[0]);
        if (result == JOptionPane.YES_OPTION) {
            this.dispose();
            new SokobanWindow(this.controller.nextLevel());
        }
        else if (result == JOptionPane.NO_OPTION) {
            exitGame();
        }
    }

    @Override
    public void keyReleased( KeyEvent e ) {
        // nothing
    }

    private static void exitGame() {
        System.exit( 0 );
    }
}
