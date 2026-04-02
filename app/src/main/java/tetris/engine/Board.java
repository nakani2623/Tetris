 package tetris.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.utils.Point;

public class Board implements Observable{
    private int width;
    private int height;
    public List<Tetromino> allTetrominos;
    // public List<Tetromino> upcomingTetrominos;
    // public Tetromino currentTetromino;
    private Mino[][] allMinos;
    List<Observer> observers;



    
    /**
     * Constructs game bord with specific dimension
     * @param width
     * @param height
     */
    public Board(int width, int height) {
        this.height = height;
        this.width = width;

        allTetrominos = new ArrayList<Tetromino>();
        // upcomingTetrominos = new LinkedList<>();
    }

    /**
     * Constructs default game board 10x20 dimension
     */
    public Board() {
        this(10, 20);
    }

    // public void populateUpcoming(List<Tetromino> newTetrominos) {
    //     //TODO: Exceptional whenever upcoming is less than 7
    //     upcomingTetrominos.addAll(newTetrominos);
    // }

    public void initialiseTetromino(Tetromino t) {
        allTetrominos.add(t);
        notifyObservers();
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Point centreTopPoint() {
        return new Point((this.width/2)-1, 1);
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
