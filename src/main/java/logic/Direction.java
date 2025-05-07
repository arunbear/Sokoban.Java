package logic;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction reverse() {
        return switch (this) {
            case UP    -> Direction.DOWN;
            case DOWN  -> Direction.UP;
            case LEFT  -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
    }
}


