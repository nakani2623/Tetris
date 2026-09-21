package tetris.engine.operator;

import tetris.engine.Tetromino;

public class MoveRightOperator implements Operator {
    @Override
    public void operate(Tetromino t) {
        t.moveRight();
    }
}
