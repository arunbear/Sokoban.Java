package logic;


/**
 * Represents a warehouse worker (the player in the game).
 */
public class Worker {
    private int line;
    private int column;


    public void moveTo(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public void moveUp() {
        this.line -= 1;
    }

    public void moveDown() {
        this.line += 1;
    }

    public void moveLeft() {
        this.column -= 1;
    }

    public void moveRight() {
        this.column += 1;
    }

}

