package tetris.engine.score;

import tetris.engine.observers.ScoreObserver;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public interface ScoreManager {
    void reset();
    void onSpinOrClear(TetrominoType currentTetrominoType, SpinState currentSpinState, int numLineCleared);
    void addObserver(ScoreObserver observer);
    void dispose();
}
