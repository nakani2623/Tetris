package tetris.engine.operator;

import tetris.engine.Tetromino;

public class RotateR180Operator implements Operator{
    @Override
    public void operate(Tetromino t) {
        t.rotate180();        
    }
}
