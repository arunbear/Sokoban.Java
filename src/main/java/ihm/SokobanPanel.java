package ihm;

import java.awt.Graphics;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.ContenuCase;
import logic.Controller;

import static ihm.SokobanWindow.IMAGE_SIZE;


@SuppressWarnings("serial")
public class SokobanPanel extends JPanel {

    private static EnumMap< ContenuCase, Image > images;

    private final Controller controller;

    public SokobanPanel(Controller controller) {
        this.controller = controller;
        try {
            images = new EnumMap< ContenuCase, Image >(
                Map.of(
                    ContenuCase.CASE_VIDE    , ImageIO.read( new File( "img/Vide.jpg" )),
                    ContenuCase.MUR          , ImageIO.read( new File( "img/Mur.jpg" )),
                    ContenuCase.CAISSE       , ImageIO.read( new File( "img/Caisse.jpg" )),
                    ContenuCase.CAISSE_RANGEE, ImageIO.read( new File( "img/CaisseRangee.jpg" )),
                    ContenuCase.RANGEMENT    , ImageIO.read( new File( "img/Rangement.jpg" )),
                    ContenuCase.JOUEUR       , ImageIO.read( new File( "img/Joueur.jpg" )),
                    ContenuCase.ARRIERE_PLAN , ImageIO.read( new File( "img/Background.jpg" )),
                    ContenuCase.JOUEUR_RANGEMENT , ImageIO.read( new File( "img/JoueurRangement.jpg" ))
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
        for(int l = 0; l < controller.entrepot.getLines(); l++ ) {
            for(int c = 0; c < controller.entrepot.getColumns(); c++ ) {
                g.drawImage( images.get( controller.entrepot.getCase( l, c).getContent()), c * IMAGE_SIZE, l * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, null );
            }
        }
    }

}
