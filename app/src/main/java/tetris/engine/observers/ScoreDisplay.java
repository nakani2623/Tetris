package tetris.engine.observers;

import tetris.engine.GameEngine;

public class ScoreDisplay implements Observer {
    private Observable observable = null;

    public ScoreDisplay(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    @Override
    public void update() {
        if (!(observable instanceof GameEngine)) {
            return;
        }
        GameEngine gameEngine = (GameEngine) observable;
        // todo: update data for rendering scores
    }
}
