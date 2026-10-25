package tetris.engine.score;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tetris.engine.observers.ScoreObserver;
import tetris.engine.score.scoreEvent.ScoreEvent;
import tetris.engine.score.scoreEvent.StatsEvent;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public class SprintScore implements ScoreManager {

    private int goalLines;
    private static final int refreshRate_Hz = 30;
    private int totalLinesCleared;
    private int piecesPlaced;
    private double startTime;
    private double elapsedTime;
    private double piecesPerSecond;
    // private SpinState currentSpinState;
    // private TetrominoType currentTetrominoType;
    private final ScheduledExecutorService exec =
        Executors.newSingleThreadScheduledExecutor();
    private List<ScoreObserver> observers;

    public SprintScore(int goalLines) {
        this.goalLines = goalLines;
        exec.scheduleAtFixedRate(this::updateStats, 0, 1000 / refreshRate_Hz, TimeUnit.MILLISECONDS);
        this.observers = new ArrayList<>();
    }

    public void reset() {
        this.totalLinesCleared = 0;
        this.piecesPlaced = 0;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.piecesPerSecond = 0;
    }

    public void updateStats() {
        this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0; // in seconds
        if (elapsedTime > 0) {
            this.piecesPerSecond = piecesPlaced / elapsedTime;
        }
        notifyObservers(new StatsEvent());
    }

    public void onLock(int nLinesCleared) {
        this.totalLinesCleared += nLinesCleared;
        this.piecesPlaced++;
    }

    public boolean haveAchievedGoal() {
        if (totalLinesCleared >= goalLines) {
            return true;
        }
        return false;
    }

    public void addObserver(ScoreObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ScoreObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(ScoreEvent e) {
        for (ScoreObserver observer : observers) {
            observer.update(e);
        }
    }

    public void dispose() {
        exec.shutdownNow();
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public double getPiecesPerSecond() {
        return piecesPerSecond;
    }

}
