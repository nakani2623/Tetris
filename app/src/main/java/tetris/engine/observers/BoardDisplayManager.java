package tetris.engine.observers;

import tetris.engine.GameEngine;

public class BoardDisplayManager implements Observer {
    private Observable observable = null;

    public BoardDisplayManager(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    @Override
    public void update() {
        if (!(observable instanceof GameEngine)) {
            return;
        }
        GameEngine gameEngine = (GameEngine) observable;
        // todo: render the board and tetrominos
    }
}

