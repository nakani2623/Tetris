package tetris.engine;

import java.util.List;

import javafx.geometry.Point2D;

enum Direction {
    LEFT, RIGHT, UP, DOWN;
}

enum Rotation {
    CLOCKWISE, CUNTER_CLOCKWISE, R_180;
}

/**
 * Polyomino that consists of 4 Minoes 
 */
abstract public class Tetromino {
    final int order = 4;
    Point2D centre;
    List<Mino> components;
    Mino[] children;

    public Tetromino() {
        children = new Mino[order];
    }

    /**
     * Moves the tetromino a number of unit spaces
     * @param direction
     * @param distance
     */
    abstract public void move(int distance, Direction direction);

    /**
     * Rotates the tetromino
     * @param rotation
     */
    abstract public void rotate(Rotation rotation);
}
