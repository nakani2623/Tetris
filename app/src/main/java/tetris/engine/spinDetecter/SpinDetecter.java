package tetris.engine.spinDetecter;

import tetris.engine.Board;
import tetris.engine.type.SpinState;

public interface SpinDetecter {
    SpinState detectSpin(Board board);
}
