package logic;

import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple utility class for handling level file paths.
 */
@NullMarked
public class LevelFile {
    private static final Logger LOGGER = Logger.getLogger(LevelFile.class.getName());
    private static final String LEVELS_DIR = "levels";

    /**
     * Creates a new LevelFile instance for the given level name.
     * @param levelName the name of the level (without .txt extension)
     * @return a new LevelFile instance
     */
    public static LevelFile of(String levelName) {
        return new LevelFile(levelName);
    }

    /**
     * Deletes the level file if it exists.
     * @return true if the file was successfully deleted, false otherwise
     */
    public boolean delete() {
        try {
            return Files.deleteIfExists(getFilePath());
        } catch (IOException e) {
            String errorMsg = "Error while deleting level file: %s".formatted(levelName);
            LOGGER.log(Level.SEVERE, errorMsg, e);
            return false;
        }
    }

    /**
     * Writes content to the level file with the specified options.
     * @param content The content to write to the file
     * @param options Options specifying how the file is opened
     */
    public void write(String content, OpenOption... options) {
        try {
            Files.createDirectories(getLevelsDirectory());
            Files.writeString(getFilePath(), content, options);
        } catch (IOException e) {
            String errorMsg = "Error while writing to level file: %s".formatted(levelName);
            LOGGER.log(Level.SEVERE, errorMsg, e);
        }
    }

    private final String levelName;

    private LevelFile(String levelName) {
        this.levelName = levelName + ".txt";
    }

    private Path getFilePath() throws IOException {
        return getLevelsDirectory().resolve(levelName);
    }

    private Path getLevelsDirectory() throws IOException {
        return new File(".").getCanonicalFile().toPath().resolve(LEVELS_DIR);
    }
}
