package tetris.engine.operator;

import tetris.engine.piece.Tetromino;

public class MoveUpOperator implements Operator{
    public void operate(Tetromino t) {
        t.moveUp();
    }
}
