package tetris.engine.rotationStrategy;

import tetris.engine.GameEngine;
import tetris.engine.type.Rotation;
import tetris.engine.Tetromino;

public abstract class RotationStrategy {

    public void rotate(GameEngine engine, Tetromino tetromino, Rotation rotation) {
        beforeRotation();
        boolean isWallKicked = moveMinos(engine, tetromino, rotation);
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
    public abstract boolean moveMinos(GameEngine engine, Tetromino tetromino, Rotation rotation); 
}
