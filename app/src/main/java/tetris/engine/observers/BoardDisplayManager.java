package tetris.engine.observers;

import tetris.engine.Board;
import tetris.engine.GameEngine;
import tetris.graphics.BoardPane;

public class BoardDisplayManager implements Observer {
    private Observable observable = null;
    private BoardPane boardPane;

    public BoardDisplayManager(Observable observable, BoardPane boardPane) {
        this.observable = observable;
        this.boardPane = boardPane;
        observable.addObserver(this);
    }

    @Override
    public void update() {
        if (!(observable instanceof Board)) {
            return;
        }
        // GameEngine gameEngine = (GameEngine) observable;
        // todo: render the board and tetrominos
        
    }
}

