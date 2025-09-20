package logic;

public enum TileType {
    FLOOR('#'),
    WALL('M'),
    UNSTORED_BOX('C'),
    STORED_BOX('V'),
    STORAGE_AREA('T'),
    WORKER_ON_FLOOR('G'),
    WORKER_IN_STORAGE_AREA('B'),
    OUTSIDE('_');

    private final char code;

    TileType(char code) {
        this.code = code;
    }

    /**
     * Returns the character code used to represent this tile type in level files.
     * @return the character code for this tile type
     */
    public char getCode() {
        return code;
    }
}
