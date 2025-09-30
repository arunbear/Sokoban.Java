package logic;

import java.util.Arrays;
import java.util.Optional;

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

    /**
     * Returns the character code used to represent this tile type in level files as a String.
     * @return the character code for this tile type as a single-character String
     */
    public String codeAsString() {
        return String.valueOf(code);
    }

    /**
     * Returns an Optional containing the TileType corresponding to the given character code.
     * The character code should match one of the tile type codes defined in this enum.
     *
     * @param code The character code to look up
     * @return An Optional containing the matching TileType if found, or an empty Optional if no match is found
     */
    public static Optional<TileType> fromCode(char code) {
        return Arrays
                .stream(TileType.values())
                .filter(type -> type.getCode() == code)
                .findFirst();
    }

}
