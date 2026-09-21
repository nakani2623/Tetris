package tetris.engine.operator;

import tetris.engine.Tetromino;

public class MoveLeftOperator implements Operator {
    @Override
    public void operate(Tetromino t) {
        t.moveLeft();
    }
}
