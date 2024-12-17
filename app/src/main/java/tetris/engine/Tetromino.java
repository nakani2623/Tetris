package tetris.engine;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;


/**
 * Polyomino that consists of 4 Minoes 
 */
public class Tetromino {
    final int order = 4;
    Point2D centre;
    List<Mino> children;
//    Mino[] children;

    public Tetromino(List<Mino> children) {
        this.children = children;
    }

//    /**
//     * Moves the tetromino a number of unit spaces
//     * @param direction
//     * @param distance
//     */
//    abstract public void move(int distance, Direction direction);
//
//    /**
//     * Rotates the tetromino
//     * @param rotation
//     */
//    abstract public void rotate(Rotation rotation);
}
