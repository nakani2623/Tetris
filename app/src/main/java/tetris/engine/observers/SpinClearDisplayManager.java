package tetris.engine.observers;

import tetris.engine.score.ScoreManager;
import tetris.engine.score.scoreEvent.ScoreEvent;
import tetris.engine.score.scoreEvent.SpinClearEvent;
import tetris.graphics.gameActive.SpinClearPane;

public class SpinClearDisplayManager implements ScoreObserver {
    private ScoreManager scoreManager;
    private SpinClearPane panel;
    public SpinClearDisplayManager(ScoreManager scoreManager, SpinClearPane panel) {
        this.scoreManager = scoreManager;
        this.panel = panel;
        scoreManager.addObserver(this);
        // score UI rarely needs 60Hz; consider 100–500ms
    }
    public void update(ScoreEvent e) {
        if (!(e instanceof SpinClearEvent event)) return;
        panel.update(event);
    }
}
