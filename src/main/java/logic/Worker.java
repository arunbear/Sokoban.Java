package logic;

import java.util.ArrayList;
import java.util.List;


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

    public List<Integer> getPosition() {
        List<Integer> position = new ArrayList<>();
        position.add(line);
        position.add(column);
        return position;
    }
}
	

