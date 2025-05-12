package tetris.engine;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import tetris.engine.type.TetrominoType;


/**
 * Polyomino that consists of 4 Minoes 
 */
public class Tetromino {
    final int order = 4;
    public Point2D centre;
    public List<Mino> children;
    public TetrominoType type;
//    Mino[] children;

    public Tetromino(Point2D centre, TetrominoType type) {
        this(type);
        this.centre = centre;
        children = new ArrayList<>();

        int x = (int)centre.getX();
        int y = (int)centre.getY();
        if (type == TetrominoType.T) {
           children.add(new Mino(x, y-1));
           children.add(new Mino(x-1, y));
           children.add(new Mino(x, y));
           children.add(new Mino(x+1, y));
        }
    }

    public Tetromino(TetrominoType type) {
        this.type = type;
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
