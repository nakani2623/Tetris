package tetris.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// import javafx.application.Platform;

import tetris.controller.GameOverListener;
import tetris.controller.dispatch.MainThreadDispatcher;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.operator.*;
import tetris.engine.rotator.Rotator;
import tetris.engine.rotator.SuperRotator;
import tetris.engine.score.ScoreManager;
import tetris.engine.spinDetecter.AllMiniDetecter;
import tetris.engine.spinDetecter.SpinDetecter;
import tetris.engine.type.Direction;
import tetris.engine.type.Rotation;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;

public class GameEngine implements Observable {
    // low level module references
    private ScoreManager scoreManager;
    private Board board;
    private SpinDetecter spinDetecter; 
    private Rotator rotator;
    private NextQueue nextQueue;
    private AutoMoveScheduler autoMoveScheduler;
    // private HoldManager holdManager;

    // hold
    private TetrominoType holdTetrominoType;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> lockTask;
    private int maxLockResets = 15;
    private int lockResets;

    // other self contained field
    private MainThreadDispatcher dispatcher;
    private GameOverListener listener;

    // plan to remove
    private List<Observer> observers; // move to low level modules

    /**
     * Constructs game bord with specific dimension
     * @param width
     * @param height
     */
    public GameEngine(Board board, MainThreadDispatcher dispatcher) {
        this.board = board;
        // configure game, todo: create and use initEngine(JSON? configFile) instead below
        observers = new ArrayList<>();
        this.dispatcher = dispatcher;

        // default components behaviour
        listener = () -> {};
        autoMoveScheduler = new AutoMoveScheduler(1, 0, dispatcher, this);
        spinDetecter = new AllMiniDetecter();
        rotator = new SuperRotator();
        nextQueue = new NextQueue();
        // holdManager = new HoldManager();
    }
//  ███████╗████████╗ █████╗ ██████╗ ████████╗
//  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝
//  ███████╗   ██║   ███████║██████╔╝   ██║
//  ╚════██║   ██║   ██╔══██║██╔══██╗   ██║
//  ███████║   ██║   ██║  ██║██║  ██║   ██║
//  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
    public void start() {
        spawn();
        autoMoveScheduler.onStart();
    }

    /**
     * Reset the board state into initial
     */
    public void reset() {
        scoreManager.reset();
        board.reset();
        nextQueue.reset();
        holdTetrominoType = null;

        notifyObservers();
    }

    public void move(Direction direction){
        Operator op;
        if (direction == Direction.DOWN) {op = new MoveDownOperator();}
        else if (direction == Direction.LEFT) {op = new MoveLeftOperator();}
        else if (direction == Direction.RIGHT) {op = new MoveRightOperator();}
        else {op = null;}
        board.tryOperate(op);
        
    }

    public void moveDAS(Direction direction) {
        Operator op;
        if (direction == Direction.DOWN) {op = new MoveDownOperator();}
        else if (direction == Direction.LEFT) {op = new MoveLeftOperator();}
        else if (direction == Direction.RIGHT) {op = new MoveRightOperator();}
        else {op = null;}

        while (true) {
            if (! board.tryOperate(op)) {
                break;
            }
            checkLanded();
            // let "auto move scheduler move piece by DAS/SoftDrop state"
            autoMoveScheduler.onOperate();
        }
    }

//  ██╗  ██╗ ██████╗ ██╗     ██████╗
//  ██║  ██║██╔═══██╗██║     ██╔══██╗
//  ███████║██║   ██║██║     ██║  ██║
//  ██╔══██║██║   ██║██║     ██║  ██║
//  ██║  ██║╚██████╔╝███████╗██████╔╝
//  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═════╝
//            ◀───[ T ]───▶
    public void hold() {
        // remove current tetromino
        if (lockTask != null)
            lockTask.cancel(false);
        board.getAllTetrominos().remove(board.getcurrentTetromino());

        Tetromino currentTetrominoClone = new Tetromino(board.getcurrentTetromino());
        if (holdTetrominoType == null) {
            spawn();
        }

        else {
            if (!board.spawn(holdTetrominoType) || scoreManager.haveAchievedGoal()) {
                listener.onGameOver();
                return;
            }
            autoMoveScheduler.onSpawn();
            lockResets = 0;
        }

        holdTetrominoType = currentTetrominoClone.type;
        board.updateShadow();
        notifyObservers();
    }

//  ███████╗ ██████╗ ███████╗████████╗    ██████╗ ██████╗  ██████╗ ██████╗
//  ██╔════╝██╔═══██╗██╔════╝╚══██╔══╝    ██╔══██╗██╔══██╗██╔═══██╗██╔══██╗
//  ███████╗██║   ██║█████╗     ██║       ██║  ██║██████╔╝██║   ██║██████╔╝
//  ╚════██║██║   ██║██╔══╝     ██║       ██║  ██║██╔══██╗██║   ██║██╔═══╝
//  ███████║╚██████╔╝██║        ██║       ██████╔╝██║  ██║╚██████╔╝██║
//  ╚══════╝ ╚═════╝ ╚═╝        ╚═╝       ╚═════╝ ╚═╝  ╚═╝ ╚═════╝ ╚═╝
    public void startSoftDrop() {
        autoMoveScheduler.startSoftDrop();
    }

    public void endSoftDrop() {
        autoMoveScheduler.endSoftDrop();
    }

    public void setSoftDropFactor(double softDropFactor) {
        autoMoveScheduler.setSoftDropFactor(softDropFactor);
    }
//  ██████╗  █████╗ ███████╗     ██████╗██╗  ██╗ █████╗ ██████╗  ██████╗ ███████╗
//  ██╔══██╗██╔══██╗██╔════╝    ██╔════╝██║  ██║██╔══██╗██╔══██╗██╔════╝ ██╔════╝
//  ██║  ██║███████║███████╗    ██║     ███████║███████║██████╔╝██║  ███╗█████╗
//  ██║  ██║██╔══██║╚════██║    ██║     ██╔══██║██╔══██║██╔══██╗██║   ██║██╔══╝
//  ██████╔╝██║  ██║███████║    ╚██████╗██║  ██║██║  ██║██║  ██║╚██████╔╝███████╗
//  ╚═════╝ ╚═╝  ╚═╝╚══════╝     ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝
    public void chargeDAS(Direction direction) {
        autoMoveScheduler.chargeDAS(direction);
    }  
    
    public void unchargeDAS(Direction direction) {
        autoMoveScheduler.unchargeDAS(direction);
    }   
    

//  ██████╗  ██████╗ ████████╗ █████╗ ████████╗███████╗
//  ██╔══██╗██╔═══██╗╚══██╔══╝██╔══██╗╚══██╔══╝██╔════╝
//  ██████╔╝██║   ██║   ██║   ███████║   ██║   █████╗
//  ██╔══██╗██║   ██║   ██║   ██╔══██║   ██║   ██╔══╝
//  ██║  ██║╚██████╔╝   ██║   ██║  ██║   ██║   ███████╗
//  ╚═╝  ╚═╝ ╚═════╝    ╚═╝   ╚═╝  ╚═╝   ╚═╝   ╚══════╝
    public void rotate(Rotation rotation) {
        if (!rotator.rotate(rotation, board)) {
            return;
        }
        autoMoveScheduler.onOperate();
        // detect spin
        SpinState currSpinState = spinDetecter.detectSpin(board);

        // score manager handle spin event
        scoreManager.onSpin(currSpinState, board.getcurrentTetromino().type);
    }  
    
//  ███████╗██████╗  █████╗ ██╗    ██╗███╗   ██╗
//  ██╔════╝██╔══██╗██╔══██╗██║    ██║████╗  ██║
//  ███████╗██████╔╝███████║██║ █╗ ██║██╔██╗ ██║
//  ╚════██║██╔═══╝ ██╔══██║██║███╗██║██║╚██╗██║
//  ███████║██║     ██║  ██║╚███╔███╔╝██║ ╚████║
//  ╚══════╝╚═╝     ╚═╝  ╚═╝ ╚══╝╚══╝ ╚═╝  ╚═══╝
//                        ▼
    void spawn() {
        // get tetr type from queue
        TetrominoType currentTetrominoType = nextQueue.remove();

        // try spawn, if gameover, notify controller
        if (!board.spawn(currentTetrominoType) || scoreManager.haveAchievedGoal()) {
            listener.onGameOver();
            return;
        }
        autoMoveScheduler.onSpawn();
        lockResets = 0;

        checkLanded();
        board.updateShadow();
    }

//  ██╗      ██████╗  ██████╗██╗  ██╗
//  ██║     ██╔═══██╗██╔════╝██║ ██╔╝
//  ██║     ██║   ██║██║     █████╔╝
//  ██║     ██║   ██║██║     ██╔═██╗
//  ███████╗╚██████╔╝╚██████╗██║  ██╗
//  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝
//          ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
    void lock() {
        int nLinesCleared = board.clearCompleted();
        scoreManager.onLock(nLinesCleared);
        if (lockTask != null)
            lockTask.cancel(false);
        spawn();
    }

    public void hardDrop() {
        moveDAS(Direction.DOWN);
        lock();
    }
    /**
     * check if currentTetromino is landed, 
     * then if landed, start lockTimer
     * otherwise, end lock Timer
     */
    void checkLanded() {
        if (board.landed(board.getcurrentTetromino())) {
            if (lockTask != null) {
                lockTask.cancel(false);
            }
            lockTask = scheduler.schedule(() -> { 
                dispatcher.dispatch(() -> {
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

//   ██████╗ ████████╗██╗  ██╗███████╗██████╗ ███████╗
//  ██╔═══██╗╚══██╔══╝██║  ██║██╔════╝██╔══██╗██╔════╝
//  ██║   ██║   ██║   ███████║█████╗  ██████╔╝███████╗
//  ██║   ██║   ██║   ██╔══██║██╔══╝  ██╔══██╗╚════██║
//  ╚██████╔╝   ██║   ██║  ██║███████╗██║  ██║███████║
//   ╚═════╝    ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝
    public void dispose() {
        scheduler.shutdown();
        scoreManager.dispose();
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public double getGravity() {
        return autoMoveScheduler.getGravity();
     }
    
    public TetrominoType getHoldTetrominoType() {
        return this.holdTetrominoType;
    }

    public void setGravity(double gravity){
        autoMoveScheduler.setGravity(gravity);
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

    public NextQueue getNextQueueManager() {
        return this.nextQueue;
    }

    public Board getBoard() {
        return board;
    }
}
