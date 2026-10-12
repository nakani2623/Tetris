package tetris.engine.observers;

import tetris.engine.GameEngine;
import tetris.engine.type.TetrominoType;
import tetris.graphics.HoldPane;

public class HoldDisplayManager implements Observer {
    private GameEngine engine;
    private HoldPane holdPane;

    public HoldDisplayManager(GameEngine engine, HoldPane holdPane) {
        this.engine = engine;
        this.holdPane = holdPane;
        engine.addObserver(this);
    }

    public void update() {
        TetrominoType type = engine.getHoldTetrominoType();
        holdPane.showPiece(type);
    }
}
