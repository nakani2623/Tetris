package tetris.engine.spinDetecter;

import tetris.engine.Board;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public class AllMiniDetecter implements SpinDetecter {

    @Override
    public SpinState detectSpin(Board board) {
        SpinState currentSpinState = SpinState.NOT_SPIN;
        if (board.getcurrentTetromino().type == TetrominoType.T) { // detect T-spin
            SpinState threeCornerTSpinState = SpinDetectUtils.detectTSpin(board);
            if (SpinDetectUtils.isImmobile(board)) {
                switch (threeCornerTSpinState) {
                    case PROPER_SPIN -> currentSpinState = SpinState.PROPER_SPIN;
                    case MINI_SPIN -> currentSpinState = SpinState.MINI_SPIN;
                    case NOT_SPIN -> currentSpinState = SpinState.MINI_SPIN;
                    default -> currentSpinState = SpinState.NOT_SPIN;
                }
            } else currentSpinState = threeCornerTSpinState;
            
        } else { // detect other spin
            if (SpinDetectUtils.isImmobile(board))
                currentSpinState = SpinState.MINI_SPIN;
        }
        return currentSpinState;
    }

}
