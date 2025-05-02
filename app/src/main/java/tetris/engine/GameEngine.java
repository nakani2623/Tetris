package tetris.engine;

import tetris.engine.generatingStrategy.TetrominoGenerator;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class GameEngine implements Observable {
    private Board board;
    private List<Tetromino> tetrominos;
    private TetrominoGenerator tetrominoGenerator;
    private lockStrategy lockStrategy;
    private List<Observer> observers;
    public GameEngine() {
        board = new Board();
        tetrominos = new ArrayList<>();
        observers = new ArrayList<>();
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

    public Board getBoard() {
        return board;
    }
}
