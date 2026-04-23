package tetris.engine.rotationStrategy;

import tetris.engine.Board;
import tetris.engine.type.Rotation;
import tetris.engine.Tetromino;

public abstract class RotationStrategy {

    public void rotate(Board board, Tetromino tetromino, Rotation rotation) {
        beforeRotation();
        boolean isWallKicked = moveMinos(board, tetromino, rotation);
        afterRotation(isWallKicked);
    }
    public void beforeRotation() {return;}
    public void afterRotation(boolean isWallKicked) {return;}

    /**
     * 
     * @param board
     * @param tetromino
     * @param rotation 
     * @return boolean of wallkicked or not
     */
    public abstract boolean moveMinos(Board board, Tetromino tetromino, Rotation rotation); 
}
