package tetris.engine.type;

public enum RotationState {
    ZERO, RIGHT, TWO, LEFT;
    public RotationState changeState(Rotation rotation) {
        return values()[(ordinal() + rotation.getValue()) % 4];
    }
}
