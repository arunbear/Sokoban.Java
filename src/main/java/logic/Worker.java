package logic;


/**
 * Represents a warehouse worker (the player in the game).
 */
public class Worker {
    private int line;
    private int column;


    public void set_pos(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
	

