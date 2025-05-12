package logic;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Warehouse {
    private final int lines;

    private final int columns;

    private final List<Case> case_tableau = new ArrayList<Case> ();

    public Controller controller;


    public Warehouse(String path_to_level, int level, Controller controller) throws IOException {
    	this.controller = controller;

    	Path file = Paths.get(path_to_level);
		List<String> lines = Collections.emptyList();
		lines = Files.readAllLines(file, StandardCharsets.UTF_8);

		this.lines = lines.size();
		this.columns = lines.get(0).length();
		for (int i = 0; i<this.lines; i++) {
			for(int j = 0; j<this.columns; j++) {

				switch (Character.toString(lines.get(i).charAt(j))) {
					case "_":
						// Case arrière-plan
						case_tableau.add(new Case(i, j, ContenuCase.ARRIERE_PLAN, this));
						break;
					case "M":
						// Case mu
						case_tableau.add(new Case(i, j, ContenuCase.WALL, this));
						break;
					case "#":
						// Case vide
						case_tableau.add(new Case(i, j, ContenuCase.CASE_VIDE, this));
						break;
					case "T":
						// Case cible
						case_tableau.add(new Case(i, j, ContenuCase.RANGEMENT, this));
						break;
					case "G":
						// Case joueur
						case_tableau.add(new Case(i, j, ContenuCase.JOUEUR, this));
						controller.worker.moveTo(i, j);
						break;
					case "C":
						// Case Caisse
						case_tableau.add(new Case(i, j, ContenuCase.CAISSE, this));
						break;
					case "B":
						// Case joueur sur une cible
						case_tableau.add(new Case(i, j, ContenuCase.JOUEUR_RANGEMENT, this));
						controller.worker.moveTo(i, j);
						break;
					case "V":
						// Case caisse déjà validée
						case_tableau.add(new Case(i, j, ContenuCase.CAISSE_RANGEE, this));
						break;
				}

			}
		}
	}


    public Case getCase(int l, int c) {
    	return case_tableau.get(l*this.columns + c);
    }


    public boolean checkVictory() {
    	for (int i=0; i < case_tableau.size(); i++) {
    		if (case_tableau.get(i).getContent() == ContenuCase.CAISSE) {
    			return false;
    		}
    	};
    	return true;
    }

    public int getLines() {
    	return lines;
    }

    public int getColumns() {
    	return columns;
    }

}
