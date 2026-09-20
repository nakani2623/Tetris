package tetris.engine.generatingStrategy;

import tetris.engine.type.TetrominoType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tetromino generator
 */
public class BagGenerator implements TetrominoGenerator {

    /**
     * Generates 1 bag of Tetrominos
     * @return A List of 7 Tetrominos
     */
    public List<TetrominoType> generateTetrominos() {
        List<TetrominoType> tetrominos = new ArrayList<>();
        for (TetrominoType type : TetrominoType.values()) {
            // Tetromino t = new Tetromino(type);
            tetrominos.add(type);
        }

        Collections.shuffle(tetrominos);
        return tetrominos;
    }
}
