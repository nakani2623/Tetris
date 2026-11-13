package tetris.engine.operator;

import tetris.engine.piece.Tetromino;

public class RotateClockwiseOperator implements Operator {
    @Override
    public void operate(Tetromino t) {
        t.rotateClockwise();        
    }
}
