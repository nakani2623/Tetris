package tetris.engine.generatingStrategy;

import tetris.engine.type.TetrominoType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Tetromino generator
 */
public class BagGenerator implements TetrominoGenerator {
    private Random bagRandom = new Random();

    @Override
    public void setSeed(long seed) {
        bagRandom.setSeed(seed);
    }

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

        Collections.shuffle(tetrominos, bagRandom);
        return tetrominos;
    }
}
