package tetris.engine.observers;


import javafx.application.Platform;
import tetris.engine.score.ScoreManager;
import tetris.engine.score.ZenScore;
import tetris.engine.score.scoreEvent.ScoreEvent;
import tetris.engine.score.scoreEvent.StatsEvent;
import tetris.graphics.gameActive.ScorePane;

public class StatsDisplayManager implements ScoreObserver {
    private final ScoreManager score;
    private final ScorePane panel;


    public StatsDisplayManager(ScoreManager observable, ScorePane panel) {
        this.score = observable;
        this.panel = panel;
        score.addObserver(this);
        // score UI rarely needs 60Hz; consider 100–500ms
    }

    @Override
    public void update(ScoreEvent event) {
        if (!(event instanceof StatsEvent e))   return;
        if (!(score instanceof ZenScore score)) return;

        // Read engine state (must be thread-safe!)
        double elapsed = score.getElapsedTime();
        double points = score.getScore();
        double pps = score.getPiecesPerSecond();

        // Update UI on JavaFX thread
        Platform.runLater(() -> {
            panel.updateTime(elapsed);
            panel.updateScore(points);
            panel.updatePPS(pps);
        });

    }
}

