package logic;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Direction DirectionInverse(Direction direction) {
        return switch (direction) {
            case UP    -> Direction.DOWN;
            case DOWN  -> Direction.UP;
            case LEFT  -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
    }
}


