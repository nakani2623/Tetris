package tetris.engine.score;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tetris.engine.observers.ScoreObserver;
import tetris.engine.score.scoreEvent.ScoreEvent;
import tetris.engine.score.scoreEvent.StatsEvent;
import tetris.engine.type.LineClearResult;

public class SprintScore implements ScoreManager {

    protected int goalLines;
    protected static final int refreshRate_Hz = 30;
    protected  int totalLinesCleared;
    protected int piecesPlaced;
    protected double startTime;
    protected double elapsedTime;
    protected double piecesPerSecond;
    protected final ScheduledExecutorService exec =
        Executors.newSingleThreadScheduledExecutor();
    protected ScheduledFuture<?> timer;
    protected List<ScoreObserver> observers;

    public SprintScore(int goalLines) {
        this.goalLines = goalLines;
        this.observers = new ArrayList<>();
        reset();
    }

    public void reset() {
        this.totalLinesCleared = 0;
        this.piecesPlaced = 0;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.piecesPerSecond = 0;

        // restart exec
        stopTimer();
        startTimer();
    }

    public void stop() {
        stopTimer();
    }

    void startTimer() {
        if (timer == null || timer.isCancelled())
            timer = exec.scheduleAtFixedRate(this::updateStats, 0, TimeUnit.SECONDS.toNanos(1) / refreshRate_Hz, TimeUnit.NANOSECONDS);
    }

    void stopTimer() {
        if (timer != null) {
            timer.cancel(false);
            timer = null;
        }
    }

    public void updateStats() {
        this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0; // in seconds
        if (elapsedTime > 0) {
            this.piecesPerSecond = piecesPlaced / elapsedTime;
        }
        notifyObservers(new StatsEvent());
    }

    public void onLock(LineClearResult result) {
        this.totalLinesCleared += result.totalLinesCleared();
        this.piecesPlaced++;
    }

    public boolean haveAchievedGoal() {
        if (totalLinesCleared >= goalLines) {
            stopTimer();
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
