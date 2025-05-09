package logic;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Controller {

	private static final int customLevel = 99;

	private int level = customLevel;
    private final String pathToLevel;
    public Warehouse warehouse;
    public Worker worker;

    public Controller(String pathToLevel) throws IOException {
		var levelsPath = "levels%slevel".formatted(FileSystems.getDefault().getSeparator());

		if (pathToLevel.contains(levelsPath)) {
			Optional<Integer> levels = parseLevel(pathToLevel);
			this.level = levels.orElse(customLevel);
    	}

    	this.pathToLevel = pathToLevel;
    	this.worker = new Worker();

    	this.warehouse = new Warehouse(pathToLevel ,this.level, this);
    }

    public void action(Direction direction) {
    	int l = worker.getLine();
    	int c = worker.getColumn();
    	switch (direction) {
    		case UP:
    			if (l != 0) {
    				if (warehouse.getCase((l - 1), c).acceptGardian(direction)) {
						worker.moveUp();
    				}
                }
                break;
    		case DOWN:
    			if (l != warehouse.getLines() - 1) {
    				if (warehouse.getCase((l + 1), c).acceptGardian(direction)) {
    					worker.moveDown();
    				}
                }
                break;
    		case LEFT:
    			if (c != 0) {
    				if (warehouse.getCase(l, c - 1).acceptGardian(direction)) {
    					worker.moveLeft();
    				}
                }
                break;
    		case RIGHT:
    			if (c != warehouse.getColumns() - 1) {
    				if (warehouse.getCase(l, c + 1).acceptGardian(direction)) {
    					worker.moveRight();
    				}
                }
                break;
    	}
    }

    public boolean levelEnd() {
    	return warehouse.checkVictory();
    }

    public int getLevel() {
    	return level;
    }

	public boolean isOnCustomLevel() {
		return level == customLevel;
	}

    public String getPathToLevel() {
    	return pathToLevel;
    }

	public void restart() throws IOException {
		this.worker = new Worker();
    	this.warehouse = new Warehouse(this.pathToLevel, this.level, this);
	}

	/**
	 * Parse the level number from the given path.
	 *
	 * @param path The path to parse, e.g. "levels/level2.txt"
	 * @return The level number as an {@link Optional} or an empty {@link Optional} if it cannot be parsed.
	 */
	Optional<Integer> parseLevel(String path) {
		final Pattern pattern = Pattern.compile("\\d+");
		final Matcher matcher = pattern.matcher(path);
		return matcher.find() ? Optional.of(Integer.parseInt(matcher.group())) : Optional.empty();
	}

}
