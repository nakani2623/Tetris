package tetris.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import tetris.engine.generatingStrategy.BagGenerator;
import tetris.engine.generatingStrategy.TetrominoGenerator;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.type.TetrominoType;

public class NextQueue implements Observable {
        // next queue / generate piece
    private Queue<TetrominoType> nextQueue = new LinkedList<TetrominoType>();
    private TetrominoGenerator tetrominoGenerator;
    private int displaySize;
    private List<Observer> observers = new ArrayList<>();
    public NextQueue() {
        
        // default (7-bag)
        tetrominoGenerator = new BagGenerator();
        displaySize = 5;
    }

    public TetrominoType remove() {
        // populate next queue
        if (nextQueue.size() <= displaySize) {
            nextQueue.addAll(tetrominoGenerator.generateTetrominos());
        }
        TetrominoType currentTetrominoType = nextQueue.remove();
        notifyObservers();
        return currentTetrominoType;
        
    }

    public void reset() {
        nextQueue.clear();
    }

    public List<TetrominoType> getNextQueue() {
        return List.copyOf(nextQueue);
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
}
