package tetris.engine.score.scoreEvent;

import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public class SpinClearEvent implements ScoreEvent{
    int linesClear;
    SpinState spinState;
    TetrominoType tetrominoType;

    public SpinClearEvent(int linesClear, SpinState spinState, TetrominoType tetrominoType) {
        this.linesClear = linesClear;
        this.spinState = spinState;
        this.tetrominoType = tetrominoType;
    }
}
