package tetris.engine.observers;


import javafx.application.Platform;
import tetris.engine.Score;
import tetris.graphics.ScorePane;

public class ScoreDisplayManager implements Observer {
    private final Observable observable;
    private final ScorePane panel;


    public  ScoreDisplayManager(Observable observable, ScorePane panel) {
        this.observable = observable;
        this.panel = panel;
        observable.addObserver(this);
        // score UI rarely needs 60Hz; consider 100–500ms
    }

    @Override
    public void update() {
        if (!(observable instanceof Score score)) return;

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

