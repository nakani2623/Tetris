package tetris.engine.generatingStrategy;

import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tetromino generator
 */
public class BagGenerator implements TetrominoGenerator {

//    public Tetromino run() {
//        return i();
//    }

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

//    public Tetromino i() {
//        Tetromino tetromino = new Tetromino(null);
//        tetromino.type = TetrominoType.I;
//        return tetromino;
//    }

//    public Tetromino t() {

//        Tetromino tetromino = new Tetromino(null);
//        tetromino.type = TetrominoType.T;
//        return tetromino;
//    }


}
