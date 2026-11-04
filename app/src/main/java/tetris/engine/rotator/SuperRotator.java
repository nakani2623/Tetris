package tetris.engine.rotator;

import java.util.List;

import tetris.engine.Board;
import tetris.engine.Tetromino;
import tetris.engine.operator.Operator;
import tetris.engine.operator.RotateClockwiseOperator;
import tetris.engine.operator.RotateCounterClockwiseOperator;
import tetris.engine.operator.RotateR180Operator;
import tetris.engine.type.Rotation;
import tetris.utils.KickTable;
import tetris.utils.Point;

public class SuperRotator implements Rotator {
    public boolean rotate(Rotation rotation, Board board) {
        Operator op;
        if (rotation == Rotation.CLOCKWISE)              {op = new RotateClockwiseOperator();}
        else if (rotation == Rotation.COUNTER_CLOCKWISE) {op = new RotateCounterClockwiseOperator();}
        else                                             {op = new RotateR180Operator();}
        // try without kick
        if (board.tryOperate(op)) {
            return true;
        }
        // failed without kick
        // 1.  get kick table
        Tetromino currentTetromino = board.getcurrentTetromino();
        List<Tetromino> allTetrominos = board.getAllTetrominos();
        List<Point> kickList = KickTable.getKickList(
            currentTetromino.type,
            currentTetromino.rotationState,
            currentTetromino.rotationState.changeState(rotation)
        );
        
        Tetromino backup = new Tetromino(currentTetromino);
        Point oriPos = new Point(currentTetromino.centre);
        int currentIndex = allTetrominos.indexOf(currentTetromino);
        if (kickList == null) {
            return false;
        }
        for (Point shift: kickList) {
            // 2. move to next position (to try every position 1 by 1)
            Point destination = Point.combine(shift, oriPos);
            while (!currentTetromino.centre.equals(destination)) {
                if (currentTetromino.centre.getX() < destination.getX()) {
                    currentTetromino.moveRight();
                }
                else if (currentTetromino.centre.getX() > destination.getX()) {
                    currentTetromino.moveLeft();
                }
                else if (currentTetromino.centre.getY() < destination.getY()) {
                    currentTetromino.moveDown();
                }
                else if (currentTetromino.centre.getY() > destination.getY()) {
                    currentTetromino.moveUp();
                }
            }
            // 3. try rotate at this position
            if (board.tryOperate(op)) {
                return true;
            }
        }
        // failed all attempts: move currTetro to origin
        currentTetromino = new Tetromino(backup);
        if (currentIndex >= 0) {
            allTetrominos.set(currentIndex, currentTetromino);
        }
        return false;
    }

}
