package tetris.engine.operator;

import tetris.engine.piece.Tetromino;

public class MoveDownOperator implements Operator {
    @Override
    public void operate(Tetromino t) {
        t.moveDown();
    }
}
