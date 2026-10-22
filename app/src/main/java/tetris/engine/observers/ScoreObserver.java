package tetris.engine.observers;

import tetris.engine.score.scoreEvent.ScoreEvent;

public interface ScoreObserver {
    void update(ScoreEvent e);
}
