package tetris.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// import javafx.application.Platform;

import java.util.Iterator;
import java.util.LinkedList;

import tetris.controller.GameOverListener;
import tetris.controller.dispatch.MainThreadDispatcher;
import tetris.controller.Controllable;
import tetris.engine.generatingStrategy.BagGenerator;
import tetris.engine.generatingStrategy.TetrominoGenerator;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.operator.*;
import tetris.engine.rotator.Rotator;
import tetris.engine.score.ScoreManager;
import tetris.engine.softdrop.SoftDropHandler;
import tetris.engine.type.Direction;
import tetris.engine.type.Rotation;
import tetris.engine.type.RotationState;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;
import tetris.utils.KickTable;
import tetris.utils.Point;

public class GameEngine implements Observable {
    // low level module references
    private ScoreManager scoreManager;
    private Board board;
    private SoftDropHandler softDropHandler;

    private AutoMoveScheduler autoMoveScheduler;
    // next queue / generate piece
    private Queue<TetrominoType> nextQueue;
    private TetrominoGenerator tetrominoGenerator;

    // hold
    private TetrominoType holdTetrominoType;
    
    // score? rotation
    // private SpinState currentSpinState;

    

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> lockTask;
    private int maxLockResets = 15;
    private int lockResets;

    // other self contained field
    private MainThreadDispatcher dispatcher;
    private GameOverListener listener;


    // plan to remove
    private List<Observer> observers; // move to low level modules
    // private boolean lockTimerFinished;
    // private AutoOperationListener autoOperationListener;

    /**
     * Constructs game bord with specific dimension
     * @param width
     * @param height
     */
    public GameEngine(Board board, MainThreadDispatcher dispatcher) {
        // this.board = board;
        this.gravity = 1;
        // configure game, todo: create and use initEngine(JSON? configFile) instead below
        tetrominoGenerator = new BagGenerator();
        nextQueue = new LinkedList<TetrominoType>();
        observers = new ArrayList<>();
        this.dispatcher = dispatcher;

    }

    private void handleDASEvent(){
        if (ARR != 0 && arrTask != null) {
            // stop arr timeline
            arrTask.cancel(false);
        }
        // calculate das direction
        int leftDAS = DASCharges.get(Direction.LEFT);
        int rightDAS = DASCharges.get(Direction.RIGHT);
        Direction direction;
        if (leftDAS - rightDAS == 0) { 
            return; 
        } 
        else if (leftDAS - rightDAS > 0) { direction = Direction.LEFT; }
        else { direction = Direction.RIGHT; }

        if (ARR == 0) { // 0 ARR
            try {
                moveDAS(direction);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        else { // non-zero ARR
            // instantly move one step
            // horizontalMove(direction);
            move(direction);
            handleSoftDropEvent();
            long duration = (long)(ARR * 1000 / 60);
            arrTask = scheduler.scheduleAtFixedRate( () -> {
                dispatcher.dispatch(() -> {
                    // horizontalMove(direction);
                    move(direction);
                    handleSoftDropEvent();
                });
            }, 0, duration, TimeUnit.MILLISECONDS);
        }
        
    }

    private void handleSoftDropEvent() {
        softDropHandler.applyEffect(board);
    }

    public void resetGravityTimeline(double gravity){
        if (gravityTask != null) {
            gravityTask.cancel(false);
        }
        long duration = (long)(1000 / gravity);
        gravityTask = scheduler.scheduleAtFixedRate( () -> {
            dispatcher.dispatch(() -> {
                try {
                    move(Direction.DOWN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }, 0, duration, TimeUnit.MILLISECONDS);
    }

    public void startSoftDrop() {
        isSoftDropping = true;
        if (softDropFactor != Double.MAX_VALUE) {
            resetGravityTimeline(getGravity() * softDropFactor);
        }
        handleSoftDropEvent();
    }

    public void endSoftDrop() {
        isSoftDropping = false;
        if (softDropFactor != Double.MAX_VALUE)
            resetGravityTimeline(getGravity());
    }

    public void setSoftDropFactor(double softDropFactor) {
        this.softDropFactor = softDropFactor;
    }

    public void chargeDAS(Direction direction) {
        DASCharges.put(direction, DASCharges.get(direction) + 1);
        handleDASEvent();
    }  
    
    public void unchargeDAS(Direction direction) {
        DASCharges.put(direction, DASCharges.get(direction) - 1);
        handleDASEvent();
    }   
    
    /**
     * Reset the board state into initial
     */
    public void reset() {
        scoreManager.reset();
        board.reset();
        holdTetrominoType = null;
        nextQueue = new LinkedList<TetrominoType>();
        notifyObservers();
    }
    
    public void start() {
        spawn();
        resetGravityTimeline(gravity);
    }

    public void rotate(Rotation rotation) {
        if (!board.rotate(rotation)) {
            return;
        }

        // SpinState currentSpinState = SpinState.NOT_SPIN;
        // detect & let score handle spins
        
        // if 
        scoreManager.onSpin(currentSpinState, board.getcurrentTetromino().type);
    }

    

    public void hardDrop() {
        moveDAS(Direction.DOWN);
        lock();
    }
    
    /**
     * Swap holdTetromino and currentTetromino. spawn a new one if hold never used
     * 
     */
    public void hold() {
        removeCurrentTetromino();
        Tetromino currentTetrominoClone = new Tetromino(board.getcurrentTetromino());
        if (holdTetrominoType == null) {
            spawn();

        }

        else {
            board.setCurrentTetromino(new Tetromino(centreTopPoint(), holdTetrominoType)); // duplicate_1 2
            board.getAllTetrominos().add(board.getcurrentTetromino());
            handleSoftDropEvent(); // this is temp quick fix, planned refactor
            handleDASEvent();
        }

        holdTetrominoType = currentTetrominoClone.type;
        board.updateShadow();
    }



    void spawn() {
        // from previous piece's leftover data, decide isAutoSpawned true/false, 
        // to know if needed to notify controller RIGHT AFTER current piece spawned
        // boolean isAutoSpawned = false;
        // if ((lockResets >= maxLockResets) || lockTimerFinished) 
        //     isAutoSpawned = true;        
        // populate next queue
        while (nextQueue.size() < 7) {
            nextQueue.addAll(tetrominoGenerator.generateTetrominos());
        }

        // when gameover, notify controller
        if (!board.spawn() || scoreManager.haveAchievedGoal()) {
            listener.onGameOver();
            return;
        }
        
        lockResets = 0;

        

        // currentSpinState = SpinState.NOT_SPIN;
        checkLanded();
        handleDASEvent();
        handleSoftDropEvent();
        updateShadow();
    }

    /**
     * check if currentTetromino is landed, 
     * then if landed, start lockTimer
     * otherwise, end lock Timer
     */
    void checkLanded() {
        if (landed(currentTetromino)) {
            if (lockTask != null) {
                lockTask.cancel(false);
            }
            lockTask = scheduler.schedule(() -> { 
                dispatcher.dispatch(() -> {
                    // this.lockTimerFinished = true;
                    lock();
                }); 
                
            }, 500, TimeUnit.MILLISECONDS);
            lockResets ++;
            if (lockResets > maxLockResets) {
                lock();
            }
        }
        else {
            // lockTimer.stop();
            if (lockTask != null)
                lockTask.cancel(false);
        }
    }


    /**
     * standard 3 corners detection, ref: https://tetris.wiki/T-Spin
     * impl by: spawn a single at 4 corners of T, then do collison tests, 
     * if 3/4 corners colide, is T spin, then if 2/2 front corners colide, is proper T spin 
     * @return SpinState: (NOT/MINI/PROPER)_SPIN
     */
    

    void lock() {
        int nLinesClearedcleared = checkCompletedLines(currentTetromino);
        scoreManager.onLock(nLinesClearedcleared);
        lockTask.cancel(false);
        spawn();
        // notifyObservers();
    }

   
    
    /**
     * Checks if the current tetromino has touches the gound or other tetrominos on the bottom
     * @param t
     * @return
     */
    boolean landed(Tetromino t) {
        // simulate a downward move, check if there is a collision
        Tetromino clone  = new Tetromino(t);
        clone.moveDown();

        if (hasCollision(clone)) {
            return true;
        }

        return false;
    }


    /**
     * everything need to do beside removing from allTetrominos list
     */
    void removeCurrentTetromino() {
        // lockTimer.stop();
        if (lockTask != null)
            lockTask.cancel(false);
        allTetrominos.remove(currentTetromino);
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public double getGravity() {
        return gravity;
    }
    
    public TetrominoType getHoldTetrominoType() {
        return this.holdTetrominoType;
    }

    public List<TetrominoType> getNextQueue() {
        return List.copyOf(nextQueue);
    }

    public void setGravity(double gravity){
        this.gravity = gravity;
    }

    public void setGameOverListener(GameOverListener listener) {
        this.listener = listener;
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

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void dispose() {
        scheduler.shutdown();
        scoreManager.dispose();
    }
}
