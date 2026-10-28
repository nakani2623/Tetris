package tetris.engine.spinDetecter;

import java.util.List;

import tetris.engine.Board;
import tetris.engine.Mino;
import tetris.engine.Piece;
import tetris.engine.Tetromino;
import tetris.engine.operator.MoveDownOperator;
import tetris.engine.operator.MoveLeftOperator;
import tetris.engine.operator.MoveRightOperator;
import tetris.engine.operator.MoveUpOperator;
import tetris.engine.operator.Operator;
import tetris.engine.type.RotationState;
import tetris.engine.type.SpinState;
import tetris.utils.Point;

public class SpinDetectUtils {
        /**
     * 
     * @return true if current tetromino is immobile
     */
    public static boolean isImmobile(Board board) {
        Operator[] operators = {new MoveDownOperator(), new MoveLeftOperator(), new MoveRightOperator(), new MoveUpOperator()};
        for (Operator operator : operators) {
            if (board.canOperate(operator))
                return false;
        }
        return true;
    }
    public static SpinState detectTSpin(Board board) { 
        // (optional)TODO: re-implement this method not depend on board
        // 1. get 4 corner postions
        // 2. check if the corner is part of any locked Tetrominos

        Tetromino currentTetromino = board.getcurrentTetromino();
        // spawn single mino piece at top-left corner
        Point centre = currentTetromino.getCentre();
        Piece corner = new Piece(new Mino((int)centre.getX() - 1, (int)centre.getY() - 1));

        // rotate to be front-left corner of current T
        int cwRotationForCorner = 0;
        if (currentTetromino.rotationState == RotationState.ZERO)
            ;
        else if (currentTetromino.rotationState == RotationState.RIGHT)
            cwRotationForCorner = 1;
        else if (currentTetromino.rotationState == RotationState.TWO)
            cwRotationForCorner = 2;
        else if (currentTetromino.rotationState == RotationState.LEFT)
            cwRotationForCorner = 3;
        for (int i = 0; i < cwRotationForCorner; i++)
            corner.rotateClockwise(centre);

        // collision tests, (test & rotatecw of corner & next test, 4 in total)
        // first 2: front, last 2: other corners
        int nBlockedFrontCorner = 0;
        int nBlockedOtherCorner = 0;
        for (int i = 0; i < 4; i++) {
            // collision test
            if (board.hasCollision(corner)) {
                if (i < 2) {
                    nBlockedFrontCorner++;
                } else {
                    nBlockedOtherCorner++;
                }
            }
            corner.rotateClockwise(centre);
        }

        if (nBlockedFrontCorner + nBlockedOtherCorner >= 3) {
            // is a T-spin! then decide proper or mini:
            if (nBlockedFrontCorner == 2) 
                return SpinState.PROPER_SPIN;
            else
                return SpinState.MINI_SPIN;
        }
        return SpinState.NOT_SPIN;
    }


}
