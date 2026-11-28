package tetris.engine.score;

import tetris.engine.observers.ScoreObserver;
import tetris.engine.type.LineClearResult;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public interface ScoreManager {
    default void onStart() {}
    default void onSpin(SpinState spin, TetrominoType type) { return; }
    void onLock(LineClearResult clearResult);
    default void onSoftDrop(int nLinesMoved) { return; }
    default void onHardDrop(int nLinesMoved) { return; }
    void addObserver(ScoreObserver observer);
    void dispose();
    double getElapsedTime();
    double getPiecesPerSecond();
    boolean haveAchievedGoal();
    void stop();
    void reset();
}
