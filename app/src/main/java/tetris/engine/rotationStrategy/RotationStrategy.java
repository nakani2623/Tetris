package tetris.engine.rotationStrategy;

import tetris.engine.Board;
import tetris.engine.Rotation;
import tetris.engine.Tetromino;

public interface RotationStrategy {
    void rotate(Board board, Tetromino tetromino, Rotation rotation);
}
