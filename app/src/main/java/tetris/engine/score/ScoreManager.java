package tetris.engine.score;

import tetris.engine.observers.ScoreObserver;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public interface ScoreManager {
    void reset();
    // void onSpinOrClear(TetrominoType currentTetrominoType, SpinState currentSpinState, int cleared);
    void onSpin(SpinState spin, TetrominoType type);
    void onLock(int nLinesCleared);
    default void onSoftDrop(int nLinesMoved) { return; }
    default void onHardDrop(int nLinesMoved) { return; }
    void addObserver(ScoreObserver observer);
    void dispose();
}
