package tetris.engine.operator;

import tetris.engine.Tetromino;

public class MoveDownOperator implements Operator {
    @Override
    public void operate(Tetromino t) {
        t.moveDown();
    }
}
