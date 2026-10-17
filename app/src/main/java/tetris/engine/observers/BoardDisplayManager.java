package tetris.engine.observers;

import tetris.engine.GameEngine;
import tetris.graphics.BoardPane;

public class BoardDisplayManager implements Observer {
    private GameEngine engine;
    private BoardPane boardPane;

    public BoardDisplayManager(GameEngine engine, BoardPane boardPane) {
        this.engine = engine;
        this.boardPane = boardPane;
        engine.addObserver(this);
    }

    @Override
    public void update() {
        boardPane.render(
            engine.getAllTetrominos(),
            engine.getShadowTetromino(),
            (int)engine.getWidth(),
            (int)engine.getHeight());
    }
}

