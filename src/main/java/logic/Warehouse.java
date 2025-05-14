package logic;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final int lines;
    private final int columns;

    private final List<Case> case_tableau = new ArrayList<>();

	public Warehouse(String path_to_level, Worker worker) throws IOException {

    	Path file = Paths.get(path_to_level);
        List<String> linesFromFile = Files.readAllLines(file, StandardCharsets.UTF_8);

		this.lines = linesFromFile.size();
		this.columns = linesFromFile.getFirst().length();

		parseLevel(worker, linesFromFile);
	}

	private void parseLevel(Worker worker, List<String> linesFromFile) {
		for (int i = 0; i<this.lines; i++) {
			for(int j = 0; j<this.columns; j++) {

                switch (Character.toString(linesFromFile.get(i).charAt(j))) {
                    case "_" ->
                            case_tableau.add(new Case(i, j, TileType.OUTSIDE, this));
                    case "M" ->
                            case_tableau.add(new Case(i, j, TileType.WALL, this));
                    case "#" ->
                            case_tableau.add(new Case(i, j, TileType.FLOOR, this));
                    case "T" ->
                            case_tableau.add(new Case(i, j, TileType.STORAGE_AREA, this));
                    case "G" -> {
                        case_tableau.add(new Case(i, j, TileType.WORKER_ON_FLOOR, this));
                        worker.moveTo(i, j);
                    }
                    case "C" ->
                            case_tableau.add(new Case(i, j, TileType.UNSTORED_BOX, this));
                    case "B" -> {
                        case_tableau.add(new Case(i, j, TileType.WORKER_IN_STORAGE_AREA, this));
                        worker.moveTo(i, j);
                    }
                    case "V" ->
                            case_tableau.add(new Case(i, j, TileType.STORED_BOX, this));
                }

			}
		}
	}


	public Case getCase(int l, int c) {
    	return case_tableau.get(l*this.columns + c);
    }


    public boolean checkVictory() {
        for (Case caseTableau : case_tableau) {
            if (caseTableau.getContent() == TileType.UNSTORED_BOX) {
                return false;
            }
        }
        return true;
    }

    public int getLines() {
    	return lines;
    }

    public int getColumns() {
    	return columns;
    }

}
