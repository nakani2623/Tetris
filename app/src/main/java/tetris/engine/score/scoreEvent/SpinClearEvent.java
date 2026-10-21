package tetris.engine.score.scoreEvent;

import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public class SpinClearEvent implements ScoreEvent{
    public int linesClear;
    public SpinState spinState;
    public TetrominoType tetrominoType;

    public SpinClearEvent(int linesClear, SpinState spinState, TetrominoType tetrominoType) {
        this.linesClear = linesClear;
        this.spinState = spinState;
        this.tetrominoType = tetrominoType;
    }
}
