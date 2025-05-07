package logic;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;

@SuppressWarnings("unused")
public class Controller {
    private int level;
    private final String pathToLevel;
    public Entrepot entrepot;
    public Worker worker;

	private static final int customLevel = 99;

    public Controller(String path_to_level) throws IOException {
		var levelsPath = "levels%slevel".formatted(FileSystems.getDefault().getSeparator());
    	if (path_to_level.contains(levelsPath)) {
    		int pos = path_to_level.length() - 5;
    		this.level = Integer.parseInt(path_to_level.substring(pos, pos + 1));
    		if (level == 0) {
    			level = 10;
    		}
    	}
    	else {
    		this.level = customLevel;
    	}
    	this.pathToLevel = path_to_level;
    	this.worker = new Worker();

    	this.entrepot = new Entrepot(path_to_level ,this.level, this);
    }

    public void action(Direction direction) {
    	int l = worker.getLine();
    	int c = worker.getColumn();
    	switch (direction) {
    		case HAUT:
    			if (l != 0) {
    				if (entrepot.getCase((l - 1), c).acceptGardian(direction)) {
    					worker.set_pos(l-1, c);
    				}
                }
                break;
    		case BAS:
    			if (l != entrepot.getNbLignes() - 1) {
    				if (entrepot.getCase((l + 1), c).acceptGardian(direction)) {
    					worker.set_pos(l+1, c);
    				}
                }
                break;
    		case GAUCHE:
    			if (c != 0) {
    				if (entrepot.getCase(l, c - 1).acceptGardian(direction)) {
    					worker.set_pos(l, c-1);
    				}
                }
                break;
    		case DROITE:
    			if (c != entrepot.getNbColonnes() - 1) {
    				if (entrepot.getCase(l, c + 1).acceptGardian(direction)) {
    					worker.set_pos(l, c+1);
    				}
                }
                break;
    	}
    }

    public boolean levelEnd() {
    	return entrepot.checkVictory();
    }

    public int getLevel() {
    	return level;
    }

	public boolean isOnCustomLevel() {
		return level == customLevel;
	}

    public String getPathToLevel() {
    	return pathToLevel;
    }

	public void restart() throws IOException {
		this.worker = new Worker();
    	this.entrepot = new Entrepot(this.pathToLevel, this.level, this);
	}

}
