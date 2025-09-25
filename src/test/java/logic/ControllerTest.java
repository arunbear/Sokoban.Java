package logic;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import static org.assertj.core.api.BDDAssertions.then;

@NullMarked
class ControllerTest {

    private Controller controller;

    @BeforeEach
    void setUp() {
        // Initialize controller with the test level
        String testLevelPath = Paths.get("src/test/resources/levels/test_level.txt").toAbsolutePath().toString();
        controller = new Controller(testLevelPath);
    }

    @Test
    void initializes_with_correct_level_properties() {
        // Then
        then(controller.isOnCustomLevel()).isTrue();
        then(controller.warehouse).isNotNull();
        then(controller.worker).isNotNull();
    }

    @Test
    void pushes_box_onto_target_and_completes_level() {
        // Given - move to the box position first
        controller.action(Direction.DOWN);

        // When - push the box right onto the target
        controller.action(Direction.RIGHT);

        // Then - box should be on target and level should be complete
        then(controller.levelEnd()).isTrue();
    }

    @Test
    void can_undo_last_move() {
        // Given - initial position and first move
        int initialX = controller.worker.getColumn();
        int initialY = controller.worker.getLine();

        // When - move right then undo
        controller.action(Direction.RIGHT);
        controller.restart();

        // Then - should be back at initial position
        then(controller.worker.getColumn()).isEqualTo(initialX);
        then(controller.worker.getLine()).isEqualTo(initialY);
    }

    @Test
    void can_undo_multiple_moves() {
        // Given - make several moves
        controller.action(Direction.RIGHT);
        controller.action(Direction.DOWN);

        // When - restart the level
        controller.restart();

        // Then - should be back at initial position
        then(controller.worker.getColumn()).isEqualTo(2);
        then(controller.worker.getLine()).isEqualTo(2);
    }

    @Test
    void handles_restart_with_no_moves_made() {
        // When - restart without making any moves
        controller.restart();

        // Then worker should be at initial position
        then(controller.worker.getColumn()).isEqualTo(2);
        then(controller.worker.getLine()).isEqualTo(2);
    }

}
