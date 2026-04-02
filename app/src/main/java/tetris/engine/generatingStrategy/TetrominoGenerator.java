package tetris.engine.generatingStrategy;

import java.util.List;

import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;

public interface TetrominoGenerator {
    /**
     * it generates a tetromino to fill the queue with next pieces.
     */
    List<TetrominoType> generateTetrominos();
}
