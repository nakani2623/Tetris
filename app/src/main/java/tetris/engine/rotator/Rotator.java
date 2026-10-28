package tetris.engine.rotator;

import tetris.engine.Board;
import tetris.engine.type.Rotation;

public interface Rotator {
    boolean rotate(Rotation rotation, Board board);
}
