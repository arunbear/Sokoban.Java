package logic;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

@NullMarked
public class Warehouse {
    private final int lines;
    private final int columns;

    private final List<Cell> cells = new ArrayList<>();

	public Warehouse(String path_to_level, Worker worker)  {

    	Path file = Paths.get(path_to_level);
        List<String> linesFromFile;
        try {
            linesFromFile = Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.lines = linesFromFile.size();
        checkState(this.lines >= 7);

        this.columns = linesFromFile.getFirst().length();
        checkState(this.columns >= 5);

		parseLevel(worker, linesFromFile);
	}

	private void parseLevel(Worker worker, List<String> linesFromFile) {
		for (int i = 0; i<this.lines; i++) {
			for(int j = 0; j<this.columns; j++) {

                switch (Character.toString(linesFromFile.get(i).charAt(j))) {
                    case "_" ->
                            cells.add(new Cell(i, j, TileType.OUTSIDE, this));
                    case "M" ->
                            cells.add(new Cell(i, j, TileType.WALL, this));
                    case "#" ->
                            cells.add(new Cell(i, j, TileType.FLOOR, this));
                    case "T" ->
                            cells.add(new Cell(i, j, TileType.STORAGE_AREA, this));
                    case "G" -> {
                        cells.add(new Cell(i, j, TileType.WORKER_ON_FLOOR, this));
                        worker.moveTo(i, j);
                    }
                    case "C" ->
                            cells.add(new Cell(i, j, TileType.UNSTORED_BOX, this));
                    case "B" -> {
                        cells.add(new Cell(i, j, TileType.WORKER_IN_STORAGE_AREA, this));
                        worker.moveTo(i, j);
                    }
                    case "V" ->
                            cells.add(new Cell(i, j, TileType.STORED_BOX, this));

                    default -> throw new RuntimeException("Invalid tile type: " + linesFromFile.get(i).charAt(j));
                }

			}
		}
	}


	public Cell getCase(int l, int c) {
    	return cells.get(l*this.columns + c);
    }


    public boolean checkVictory() {
        return cells.stream().noneMatch(cell -> cell.getContent() == TileType.UNSTORED_BOX);
    }

    public int getLines() {
    	return lines;
    }

    public int getColumns() {
    	return columns;
    }

}
