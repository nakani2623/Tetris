package tetris.engine.type;

public enum Rotation {
    CLOCKWISE(1), COUNTER_CLOCKWISE(3), R_180(2);
    private final int value;
    Rotation(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
