package tetris.engine;

import java.util.ArrayList;
import java.util.List;

import tetris.utils.Point;

import tetris.engine.type.TetrominoType;


/**
 * Polyomino that consists of 4 Minoes 
 */
public class Tetromino {
    final int order = 4;
    public Point centre;
    public List<Mino> children;
    public TetrominoType type;
//    Mino[] children;

    public Tetromino(Point centre, TetrominoType type) {
        this(type);
        this.centre = centre;
        children = new ArrayList<>();

        double x = centre.getX();
        double y = centre.getY();
        if (type == TetrominoType.I) {
            x += 0.5;
            y += 0.5;
        } else if (type == TetrominoType.O) {
            x += 0.5;
            y -= 0.5;
        }

        if (type == TetrominoType.T) {
           children.add(new Mino((int)x, (int)y-1));
           children.add(new Mino((int)x-1, (int)y));
           children.add(new Mino((int)x, (int)y));
           children.add(new Mino((int)x+1, (int)y));
        } else if (type == TetrominoType.I) {
            children.add(new Mino((int)(x-1.5), (int)(y-0.5)));
            children.add(new Mino((int)(x-0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x+0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x+1.5), (int)(y-0.5)));
        } else if (type == TetrominoType.O) {
            children.add(new Mino((int)(x-0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x+0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x-0.5), (int)(y+0.5)));
            children.add(new Mino((int)(x+0.5), (int)(y+0.5)));
        } else if (type == TetrominoType.L) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x-1), (int)y));
            children.add(new Mino((int)(x+1), (int)y));
            children.add(new Mino((int)(x+1), (int)(y-1)));
        } else if (type == TetrominoType.J) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x-1), (int)y));
            children.add(new Mino((int)(x+1), (int)y));
            children.add(new Mino((int)(x-1), (int)(y-1)));
        } else if (type == TetrominoType.S) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x-1), (int)y));
            children.add(new Mino((int)x, (int)(y-1)));
            children.add(new Mino((int)(x+1), (int)(y-1)));
        } else if (type == TetrominoType.Z) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x+1), (int)y));
            children.add(new Mino((int)x, (int)(y-1)));
            children.add(new Mino((int)(x-1), (int)(y-1)));
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
