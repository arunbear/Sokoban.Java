package logic;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Direction DirectionInverse(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
        }
        return direction;
    }
}


