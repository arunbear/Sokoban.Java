package ihm;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.TileType;
import logic.Controller;
import org.jspecify.annotations.NullMarked;

import static ihm.SokobanWindow.IMAGE_SIZE;

@NullMarked
public class SokobanPanel extends JPanel {
    private static final EnumMap<TileType, Image> images;

    static {
        try {
            images = new EnumMap<>(Map.of(
                TileType.FLOOR,                  loadImage("img/Vide.jpg"),
                TileType.WALL,                   loadImage("img/Mur.jpg"),
                TileType.UNSTORED_BOX,           loadImage("img/Caisse.jpg"),
                TileType.STORED_BOX,             loadImage("img/CaisseRangee.jpg"),
                TileType.STORAGE_AREA,           loadImage("img/Rangement.jpg"),
                TileType.WORKER_ON_FLOOR,        loadImage("img/Joueur.jpg"),
                TileType.OUTSIDE,                loadImage("img/Background.jpg"),
                TileType.WORKER_IN_STORAGE_AREA, loadImage("img/JoueurRangement.jpg")
            ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load game resources: " + e.getMessage(), e);
        }
    }

    private static Image loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    private final Controller controller;

    public SokobanPanel(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void paint( Graphics g ) {
    	super.paint( g );
        // Business logic uses [row, column] coordinates
        // UI uses [x, y] coordinates
        // So x <=> column and y <=> row
        for(int l = 0; l < controller.getWarehouse().getLines(); l++ ) {
            for(int c = 0; c < controller.getWarehouse().getColumns(); c++ ) {
                g.drawImage( images.get(controller.getWarehouse().getCell(l, c).getContent()), c * IMAGE_SIZE, l * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, null);
            }
        }
    }

}
