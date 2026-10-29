package tetris.engine.softdrop;

import tetris.engine.Board;

public interface SoftDropHandler {
    void applyEffect(Board board);
    void startSoftDrop(Board board);
    void endSoftDrop(Board board);
}
