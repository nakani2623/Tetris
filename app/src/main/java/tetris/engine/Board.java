package tetris.engine;

import java.util.ArrayList;
import java.util.List;

import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.operator.MoveLeftOperator;
import tetris.engine.operator.MoveRightOperator;
import tetris.engine.operator.Operator;
import tetris.engine.operator.RotateClockwiseOperator;
import tetris.engine.operator.RotateCounterClockwiseOperator;
import tetris.engine.operator.RotateR180Operator;
import tetris.engine.type.Direction;
import tetris.engine.type.Rotation;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class Board implements Observable{
    private int width;
    private int height;
    public List<Tetromino> allTetrominos;
    // public List<Tetromino> upcomingTetrominos;
    public Tetromino currentTetromino;
    private Mino[][] allMinos;
    List<Observer> observers;

    public Operator op;
    
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
        observers = new ArrayList<Observer>();
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

    public void spawn(TetrominoType type) {
        currentTetromino = new Tetromino(centreTopPoint(), type);
        allTetrominos.add(currentTetromino);
        notifyObservers();
    }

    public void moveCurrent(Direction d) {
        switch (d) {
            case LEFT -> op = new MoveLeftOperator();
            case RIGHT -> op = new MoveRightOperator();
        }
        // create a clone that simulates the result of movement, move the real piece if
        // there is no collision 
        Tetromino clone = new Tetromino(currentTetromino);
        op.operate(clone);

        if (!hasCollision(clone)) {
            op.operate(currentTetromino);
            notifyObservers();
        }
    }

    public void rotateCurrent(Rotation r) {
        switch (r) {
            case CLOCKWISE -> op = new RotateClockwiseOperator();
            case COUNTER_CLOCKWISE -> op = new RotateCounterClockwiseOperator();
            case R_180 -> op = new RotateR180Operator();
        }
        // create a clone that simulates the result of movement, move the real piece if
        // there is no collision 
        Tetromino clone = new Tetromino(currentTetromino);
        op.operate(clone);

        if (!hasCollision(clone)) {
            op.operate(currentTetromino);
            notifyObservers();
        }
    }

    /**
     * check if the tetromino collide with other objects on the board
     * @param m
     * @return
     */
    public boolean hasCollision(Tetromino t) {
        for (Tetromino other : allTetrominos) {
            if (other.equals(currentTetromino))
                continue;

            if (other.collidesWith(t)) {
                return true;
            }
        }

        if (hasWallCollision(t)) {
            return true;
        }
       
        return false;
    }

    protected boolean hasWallCollision(Tetromino t) {
        for (Mino m : t.children) {
            double x = m.getPosition().getX();
            double y = m.getPosition().getY();

            // side walls
            if (x < 0 || x > width - 1) { 
                return true;
            }
            // bottom wall
            if (y > height - 1) {
                return true;
            }
        }
        return false;
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
