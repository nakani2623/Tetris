package tetris.engine.observers;


import tetris.engine.GameEngine;
import tetris.graphics.NextQueuePane;

public class NextQueueDisplayManager implements Observer {
    private GameEngine engine;
    private NextQueuePane nextQueuePane;

    public  NextQueueDisplayManager(GameEngine engine, NextQueuePane nextQueuePane) {
        this.engine = engine;
        this.nextQueuePane = nextQueuePane;
        engine.addObserver(this);
        update();
    }

    @Override 
    public void update() {
        nextQueuePane.showNext(engine.getNextQueue());
    }
}
