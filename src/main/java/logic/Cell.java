package logic;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class Cell {
    private final int line;
    private final int column;
    private TileType content;
    private final Warehouse warehouse;

    public Cell(int line, int column, TileType content, Warehouse warehouse) {
    	this.line = line;
    	this.column = column;
    	this.content = content;
    	this.warehouse = warehouse;
    }

    public TileType getContent() {
    	return this.content;
    }

    public void setContent (TileType content) {
    	this.content = content;
    }

    public void setAdjacentCellContent(Direction direction, TileType content) {
        switch (direction) {
            case UP    -> warehouse.getCase(line - 1, column).setContent(content);
            case DOWN  -> warehouse.getCase(line + 1, column).setContent(content);
            case LEFT  -> warehouse.getCase(line, column - 1).setContent(content);
            case RIGHT -> warehouse.getCase(line, column + 1).setContent(content);
        }
    }

    public TileType getAdjacentCellContent(Direction direction) {
        return switch (direction) {
            case UP    -> warehouse.getCase(line - 1, column).getContent();
            case DOWN  -> warehouse.getCase(line + 1, column).getContent();
            case LEFT  -> warehouse.getCase(line, column - 1).getContent();
            case RIGHT -> warehouse.getCase(line, column + 1).getContent();
        };
    }

    public boolean canAcceptWorker(Direction direction) {
        switch (this.content) {
            case WALL, OUTSIDE -> {
                return false;
            }
            case UNSTORED_BOX -> {
                if (boxCellCannotAcceptWorker(direction)) {
                    return false;
                }
                switch (this.getAdjacentCellContent(direction)) {
                    case UNSTORED_BOX, STORED_BOX, WALL, OUTSIDE -> {
                        return false;
                    }
                    case STORAGE_AREA -> {
                        this.setContent(TileType.WORKER_ON_FLOOR);
                        this.setAdjacentCellContent(direction, TileType.STORED_BOX);
                    }
                    case FLOOR -> {
                        this.setContent(TileType.WORKER_ON_FLOOR);
                        this.setAdjacentCellContent(direction, TileType.UNSTORED_BOX);
                    }
                    default -> {
                    }
                }
            }
            case STORED_BOX -> {
                if (boxCellCannotAcceptWorker(direction)) {
                    return false;
                }
                switch (this.getAdjacentCellContent(direction)) {
                    case UNSTORED_BOX, WALL, STORED_BOX -> {
                        return false;
                    }
                    case STORAGE_AREA -> {
                        this.setContent(TileType.WORKER_IN_STORAGE_AREA);
                        this.setAdjacentCellContent(direction, TileType.STORED_BOX);
                    }
                    case FLOOR -> {
                        this.setContent(TileType.WORKER_IN_STORAGE_AREA);
                        this.setAdjacentCellContent(direction, TileType.UNSTORED_BOX);
                    }
                    default -> {
                    }
                }
            }
            case FLOOR -> this.setContent(TileType.WORKER_ON_FLOOR);
            case STORAGE_AREA -> this.setContent(TileType.WORKER_IN_STORAGE_AREA);
            default -> {
            }
        }
    	Direction previous_player = direction.reverse();
        return switch (this.getAdjacentCellContent(previous_player)) {
            case WORKER_ON_FLOOR -> {
                this.setAdjacentCellContent(previous_player, TileType.FLOOR);
                yield true;
            }
            case WORKER_IN_STORAGE_AREA -> {
                this.setAdjacentCellContent(previous_player, TileType.STORAGE_AREA);
                yield true;
            }
            default -> false;
        };

    }

    private boolean boxCellCannotAcceptWorker(Direction direction) {
        switch (direction) {
            case UP -> {
                if (this.line == 0) {
                    return true;
                }
            }
            case DOWN -> {
                if (this.line == warehouse.getLines() - 1) {
                    return true;
                }
            }
            case LEFT -> {
                if (this.column == 0) {
                    return true;
                }
            }
            case RIGHT -> {
                if (this.column == warehouse.getColumns() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

}
