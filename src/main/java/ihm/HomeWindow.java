package ihm;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HomeWindow {
	public HomeWindow() throws FontFormatException, IOException {
	 	JFrame frame = new JFrame("Sokoban v1.0 par Gabriel FARAGO");
	 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 	frame.setResizable(false);
	 
	 	Font font_title = Font.createFont(Font.TRUETYPE_FONT, new File("font/Lostar.ttf"));
	 	font_title = font_title.deriveFont(Font.BOLD, 70);
	 	
	 	JLabel titre = new JLabel("SOKOBAN", SwingConstants.CENTER);
	 	titre.setFont(font_title);
	 	titre.setBounds(0, 50, 400, 100);
	 	
	    JButton play = new JButton("Jouer");  
	    JButton edit = new JButton("Éditeur de niveau");
	    JButton quit = new JButton("Quitter");
	    
	    play.setBounds(75,225,250,50);  
	    edit.setBounds(25,300,150,50);
	    quit.setBounds(225, 300, 150, 50);
	    
	    quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit( 0 );
			}
	    });
	    play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				try {
					new LevelSelection();
				} catch (FontFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    });
	    edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				try {
					new LevelEditorSetup();
				} catch (FontFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    });
	    
	    frame.add(play);
	    frame.add(edit);
	    frame.add(quit);
	    frame.add(titre);
	    frame.setSize(400,400);
	    frame.setLayout(null);
	    frame.setVisible(true);
			
	}
}

