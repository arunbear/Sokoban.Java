package logic;

import java.nio.file.FileSystems;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Controller {

	private static final int customLevel = 99;

	private int level = customLevel;
    private final String pathToLevel;

    public int getLines() {
        return warehouse.getLines();
    }

    public int getColumns() {
        return warehouse.getColumns();
    }

    public Warehouse warehouse;
    public Worker worker;

    public Controller(String pathToLevel) {
		var levelsPath = pathToLevels();

		if (pathToLevel.contains(levelsPath)) {
			Optional<Integer> levels = parseLevel(pathToLevel);
			this.level = levels.orElse(customLevel);
    	}

    	this.pathToLevel = pathToLevel;
    	this.worker = new Worker();

    	this.warehouse = new Warehouse(pathToLevel, this.worker);
    }

	private static String pathToLevels() {
        return "levels%slevel".formatted(FileSystems.getDefault().getSeparator());
	}

	public Controller nextLevel() {
		int nextLevel = this.level + 1;
		return new Controller("%s%s.txt".formatted(pathToLevels(), nextLevel));
	}

    public void action(Direction direction) {
    	int l = worker.getLine();
    	int c = worker.getColumn();
        switch (direction) {
            case UP -> {
                if (l != 0) {
                    if (warehouse.getCase((l - 1), c).acceptGardian(direction)) {
                        worker.moveUp();
                    }
                }
            }
            case DOWN -> {
                if (l != warehouse.getLines() - 1) {
                    if (warehouse.getCase((l + 1), c).acceptGardian(direction)) {
                        worker.moveDown();
                    }
                }
            }
            case LEFT -> {
                if (c != 0) {
                    if (warehouse.getCase(l, c - 1).acceptGardian(direction)) {
                        worker.moveLeft();
                    }
                }
            }
            case RIGHT -> {
                if (c != warehouse.getColumns() - 1) {
                    if (warehouse.getCase(l, c + 1).acceptGardian(direction)) {
                        worker.moveRight();
                    }
                }
            }
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

	public void restart() {
		this.worker = new Worker();
    	this.warehouse = new Warehouse(this.pathToLevel, this.worker);
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
