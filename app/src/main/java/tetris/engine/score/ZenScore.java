package tetris.engine.score;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import tetris.engine.observers.ScoreObserver;
import tetris.engine.score.scoreEvent.ScoreEvent;
import tetris.engine.score.scoreEvent.SpinClearEvent;
import tetris.engine.score.scoreEvent.StatsEvent;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public class ZenScore implements ScoreManager {
    // score tracking
    private static final int refreshRate_Hz = 30;
    private int score;
    private int linesCleared;
    private int piecesPlaced;
    private double startTime;
    private double elapsedTime;
    private double piecesPerSecond;
    private double attackPerMinute;
    private SpinState currentSpinState;
    private TetrominoType currentTetrominoType;

    private final ScheduledExecutorService exec =
        Executors.newSingleThreadScheduledExecutor();

    private List<ScoreObserver> observers;

    public ZenScore() {
        this.score = 0;
        this.linesCleared = 0;
        this.piecesPlaced = 0;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.piecesPerSecond = 0;
        this.attackPerMinute = 0;

        exec.scheduleAtFixedRate(this::updateStats, 0, 1000 / refreshRate_Hz, TimeUnit.MILLISECONDS);
        this.observers = new ArrayList<>();
    }

    /**
     * set the score into initial state
     */
    public void reset() {
        this.score = 0;
        this.linesCleared = 0;
        this.piecesPlaced = 0;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.piecesPerSecond = 0;
        this.attackPerMinute = 0;

    }
    
    public void updateStats() {
        this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0; // in seconds
        if (elapsedTime > 0) {
            this.piecesPerSecond = piecesPlaced / elapsedTime;
            this.attackPerMinute = (piecesPlaced / elapsedTime) * 60;
        }
        notifyObservers(new StatsEvent());
    }

    // public void onSpinOrClear(TetrominoType currentTetrominoType, SpinState currentSpinState, int cleared) { // default
    //     // update internal state
    //     // notify graphics
    //     notifyObservers(new SpinClearEvent(cleared, currentSpinState, currentTetrominoType));
    // }
    public void onSpin(SpinState spinState, TetrominoType tetrominoType) {
        currentSpinState = spinState;
        currentTetrominoType = tetrominoType;
    }

    public void onLock(int linesCleared) {
        if (linesCleared != 0 || currentSpinState != SpinState.NOT_SPIN) {
            notifyObservers(new SpinClearEvent(linesCleared, currentSpinState, currentTetrominoType));
        }
    }

    public int getScore() {
        return score;
    }

    public int getLinesCleared() {
        return linesCleared;
    }


    public int getPiecesPlaced() {
        return piecesPlaced;
    }   

    public double getPiecesPerSecond() {
        return piecesPerSecond;
    }

    public double getAttackPerMinute() {
        return attackPerMinute;
    }

    public double getElapsedTime() {
        return elapsedTime;
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

}
