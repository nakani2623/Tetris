package tetris.engine;

import tetris.engine.generatingStrategy.TetrominoGenerator;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class GameEngine implements Observable {
    private Board board;
    private List<Tetromino> tetrominos;
    public Tetromino currentTetromino;


    private TetrominoGenerator tetrominoGenerator;
    private lockStrategy lockStrategy;
    private List<Observer> observers;
    public GameEngine() {
        board = new Board();
        tetrominos = new ArrayList<>();
        currentTetromino = null;

        observers = new ArrayList<>();
    }

    public void loop() {
        while (currentTetromino != null) {
            
    	    currentTetromino = new Tetromino(board.centreTopPoint(), null);
            
        }
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

//    public void place() {
//        Tetromino tm = generator.run();
//        board.allTetrominos.add(tm);
//    }
}

