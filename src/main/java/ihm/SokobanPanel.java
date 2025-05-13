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

import static ihm.SokobanWindow.IMAGE_SIZE;


@SuppressWarnings("serial")
public class SokobanPanel extends JPanel {

    private static EnumMap<TileType, Image > images;

    private final Controller controller;

    public SokobanPanel(Controller controller) {
        this.controller = controller;
        try {
            images = new EnumMap<TileType, Image >(
                Map.of(
                    TileType.FLOOR, ImageIO.read( new File( "img/Vide.jpg" )),
                    TileType.WALL, ImageIO.read( new File( "img/Mur.jpg" )),
                    TileType.UNSTORED_BOX, ImageIO.read( new File( "img/Caisse.jpg" )),
                    TileType.STORED_BOX, ImageIO.read( new File( "img/CaisseRangee.jpg" )),
                    TileType.STORAGE_AREA, ImageIO.read( new File( "img/Rangement.jpg" )),
                    TileType.WORKER_ON_FLOOR, ImageIO.read( new File( "img/Joueur.jpg" )),
                    TileType.OUTSIDE, ImageIO.read( new File( "img/Background.jpg" )),
                    TileType.WORKER_IN_STORAGE_AREA, ImageIO.read( new File( "img/JoueurRangement.jpg" ))
                )
            );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }
    @Override
    public void paint( Graphics g ) {
    	super.paint( g );
        // Le côté métier raisonne en [ligne, colonne]
        // Le côté IHM raisonne en [x, y]
        // Donc x <=> colonne et y <=> ligne
        for(int l = 0; l < controller.warehouse.getLines(); l++ ) {
            for(int c = 0; c < controller.warehouse.getColumns(); c++ ) {
                g.drawImage( images.get( controller.warehouse.getCase( l, c).getContent()), c * IMAGE_SIZE, l * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, null );
            }
        }
    }

}
