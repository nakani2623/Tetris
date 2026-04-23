package tetris.engine;

import tetris.engine.generatingStrategy.BagGenerator;
import tetris.engine.generatingStrategy.TetrominoGenerator;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.rotationStrategy.RotationStrategy;
import tetris.engine.rotationStrategy.classicRotation;
import tetris.engine.type.Rotation;
import tetris.engine.type.TetrominoType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class GameEngine implements Observable {
    private Board board;
    // private List<Tetromino> tetrominos;
    public Tetromino currentTetromino;

    private Score score;
    private Queue<TetrominoType> nextQueue;

    private TetrominoGenerator tetrominoGenerator;
    private LockStrategy lockStrategy;
    private List<Observer> observers;
    private RotationStrategy rotationStrategy;
    public GameEngine() {
        board = new Board();
        score = new Score();
        nextQueue = new LinkedList<TetrominoType>();
        // tetrominos = new ArrayList<>();
        currentTetromino = null;
        observers = new ArrayList<>();

        // configure game, todo: create and use initEngine(JSON? configFile) instead below
        tetrominoGenerator = new BagGenerator();
        rotationStrategy = new classicRotation();
    }
    public void rotate(Rotation rotation) {
        rotationStrategy.rotate(board, currentTetromino, rotation);
    }
    public void start() {
        System.out.println("engine started to execute");
        // populate next queue
        // TODO: auto generate and populate on condition: few elements
        nextQueue.addAll(tetrominoGenerator.generateTetrominos());
    
        //spawn a piece
        TetrominoType currentTetrominoType = nextQueue.remove();
        currentTetromino = new Tetromino(board.centreTopPoint(), currentTetrominoType);

        // place the piece onto the board
        board.initialiseTetromino(currentTetromino);
        System.out.println("T init'd");

        
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

    public Score getScore() {
        return score;
    }
    public void setRotationStrategy(RotationStrategy rotationStrategy) {
        this.rotationStrategy = rotationStrategy;
    }
}

