package tetris.engine.softdrop;

import tetris.engine.Board;

public class IncreaceGravitySoftDropHandler implements SoftDropHandler {
    private 
    @Override
    public void applyEffect(Board board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyEffect'");
    }

    @Override
    public void startSoftDrop(Board board) {
        resetGravityTimeline(getGravity() * softDropFactor);
    }

    @Override
    public void endSoftDrop(Board board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endSoftDrop'");
    }
    
}
