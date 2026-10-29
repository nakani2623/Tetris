package tetris.engine.softdrop;

import tetris.engine.Board;
import tetris.engine.type.Direction;

public class InstantSoftDropHandler implements SoftDropHandler {
    private boolean isActivated;
    @Override
    public void applyEffect(Board board) {
        if (isActivated) {
            board.moveDAS(Direction.DOWN);
        }    
    }

    @Override
    public void startSoftDrop(Board board) {
        isActivated = true;
    }

    @Override
    public void endSoftDrop(Board board) {
        isActivated = false;
    }
}