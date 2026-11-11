package tetris.engine.observers;


import tetris.engine.NextQueue;
import tetris.graphics.gameActive.NextQueuePane;

public class NextQueueDisplayManager implements Observer {
    private NextQueue nextQueue;
    private NextQueuePane nextQueuePane;

    public  NextQueueDisplayManager(NextQueue nextQueue, NextQueuePane nextQueuePane) {
        this.nextQueue = nextQueue;
        this.nextQueuePane = nextQueuePane;
        nextQueue.addObserver(this);
        update();
    }

    @Override 
    public void update() {
        nextQueuePane.showNext(nextQueue.getNextQueue());
    }
}
