package tetris.engine;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;

public class Score implements Observable{
    // score tracking

    private static final int refreshRate_Hz = 30;
    private int score;
    private int linesCleared;
    private int piecesPlaced;
    private double startTime;
    private double elapsedTime;
    private double piecesPerSecond;
    private double attackPerMinute;

    private final ScheduledExecutorService exec =
        Executors.newSingleThreadScheduledExecutor();

    private List<Observer> observers;

    public Score() {
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
    
    public void updateStats() {
        this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0; // in seconds
        if (elapsedTime > 0) {
            this.piecesPerSecond = piecesPlaced / elapsedTime;
            this.attackPerMinute = (piecesPlaced / elapsedTime) * 60;
        }
        notifyObservers();
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

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void dispose() {
        exec.shutdownNow();
    }

}
