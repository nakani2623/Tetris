package tetris.engine;

import tetris.engine.generatingStrategy.BagGenerator;
import tetris.engine.generatingStrategy.TetrominoGenerator;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.rotationStrategy.RotationStrategy;
import tetris.engine.rotationStrategy.classicRotation;
import tetris.engine.type.GameState;
import tetris.engine.type.Rotation;
import tetris.engine.type.TetrominoType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameEngine implements Observable {
    private Board board;
    private Score score;
    private Queue<TetrominoType> nextQueue;

    private TetrominoGenerator tetrominoGenerator;
    private LockStrategy lockStrategy;
    private List<Observer> observers;
    private RotationStrategy rotationStrategy;
    private GameState gameState;

    public GameEngine() {
        board = new Board();
        board.engine = this;
        score = new Score(this);
        nextQueue = new LinkedList<TetrominoType>();
        observers = new ArrayList<>();

        // configure game, todo: create and use initEngine(JSON? configFile) instead below
        tetrominoGenerator = new BagGenerator();
        rotationStrategy = new classicRotation();
    }

    public void rotate(Rotation rotation) {
        rotationStrategy.rotate(board, board.currentTetromino, rotation);
    }

    public void start() {
        gameState = GameState.active;
        spawn();
    }

    public void reset() {
        board.reset();
        score.reset();
        nextQueue = new LinkedList<TetrominoType>();
    }

    public void spawn() {
        // populate next queue
        while (nextQueue.size() < 7) {
            nextQueue.addAll(tetrominoGenerator.generateTetrominos());
        }

        //spawn a piece
        TetrominoType currentTetrominoType = nextQueue.remove();
        board.spawn(currentTetrominoType);
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }
    
    public void setRotationStrategy(RotationStrategy rotationStrategy) {
        this.rotationStrategy = rotationStrategy;
    }
}

