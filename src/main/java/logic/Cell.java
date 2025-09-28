package logic;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class Cell {
    private final int line;
    private final int column;
    private TileType tileType;
    private final Warehouse warehouse;

    public Cell(int line, int column, TileType tileType, Warehouse warehouse) {
    	this.line = line;
    	this.column = column;
    	this.tileType = tileType;
    	this.warehouse = warehouse;
    }

    public TileType getTileType() {
    	return this.tileType;
    }

    public void setTileType(TileType tileType) {
    	this.tileType = tileType;
    }

    public void setAdjacentCellContent(Direction direction, TileType content) {
        switch (direction) {
            case UP    -> warehouse.getCell(line - 1, column).setTileType(content);
            case DOWN  -> warehouse.getCell(line + 1, column).setTileType(content);
            case LEFT  -> warehouse.getCell(line, column - 1).setTileType(content);
            case RIGHT -> warehouse.getCell(line, column + 1).setTileType(content);
        }
    }

    public TileType getAdjacentCellContent(Direction direction) {
        return switch (direction) {
            case UP    -> warehouse.getCell(line - 1, column).getTileType();
            case DOWN  -> warehouse.getCell(line + 1, column).getTileType();
            case LEFT  -> warehouse.getCell(line, column - 1).getTileType();
            case RIGHT -> warehouse.getCell(line, column + 1).getTileType();
        };
    }

    public boolean canAcceptWorker(Direction direction) {
        switch (this.tileType) {
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
                        this.setTileType(TileType.WORKER_ON_FLOOR);
                        this.setAdjacentCellContent(direction, TileType.STORED_BOX);
                    }
                    case FLOOR -> {
                        this.setTileType(TileType.WORKER_ON_FLOOR);
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
                        this.setTileType(TileType.WORKER_IN_STORAGE_AREA);
                        this.setAdjacentCellContent(direction, TileType.STORED_BOX);
                    }
                    case FLOOR -> {
                        this.setTileType(TileType.WORKER_IN_STORAGE_AREA);
                        this.setAdjacentCellContent(direction, TileType.UNSTORED_BOX);
                    }
                    default -> {
                    }
                }
            }
            case FLOOR -> this.setTileType(TileType.WORKER_ON_FLOOR);
            case STORAGE_AREA -> this.setTileType(TileType.WORKER_IN_STORAGE_AREA);
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
