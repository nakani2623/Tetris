package tetris.engine.generatingStrategy;

import tetris.engine.Tetromino;

public interface TetrominoGenerator {
    /**
     * it generates a tetromino to fill the queue with next pieces.
     */
    Tetromino generateTetromino();
}
