package tetris.engine.operator;

import tetris.engine.Tetromino;

public class RotateCounterClockwiseOperator implements Operator{
    @Override
    public void operate(Tetromino t) {
        t.rotateCounterClockwise();        
    }
    
}
