package logic;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkState;

@NullMarked
public class Warehouse {
    private final int lines;
    private final int columns;

    private final List<Cell> cells = new ArrayList<>();

    public Warehouse(String path_to_level, Worker worker) {

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
        IntStream.range(0, this.lines)
            .forEach(i -> IntStream.range(0, this.columns)
                .forEach(j -> {
                    char cellChar = linesFromFile.get(i).charAt(j);

                    TileType.fromCode(cellChar).ifPresentOrElse(
                        type -> {
                            cells.add(new Cell(i, j, type, this));
                            if (type == TileType.WORKER_ON_FLOOR || type == TileType.WORKER_IN_STORAGE_AREA) {
                                worker.moveTo(i, j);
                            }
                        },
                        () -> { throw new RuntimeException("Invalid tile type: " + cellChar); }
                    );
                })
            );
    }


    public Cell getCell(int l, int c) {
        return cells.get(l * this.columns + c);
    }


    public boolean checkVictory() {
        return cells.stream().noneMatch(cell -> cell.getTileType() == TileType.UNSTORED_BOX);
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

}
