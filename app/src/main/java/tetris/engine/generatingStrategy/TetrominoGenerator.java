package tetris.engine.generatingStrategy;

import java.util.List;

import tetris.engine.type.TetrominoType;

public interface TetrominoGenerator {
    /** Restarts the generated sequence using the given seed. */
    void setSeed(long seed);

    /**
     * it generates a tetromino to fill the queue with next pieces.
     */
    List<TetrominoType> generateTetrominos();
}
