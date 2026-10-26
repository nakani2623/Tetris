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
import tetris.engine.score.ScoreManager;
import tetris.engine.type.Direction;
import tetris.engine.type.Rotation;
import tetris.engine.type.RotationState;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;
import tetris.utils.KickTable;
import tetris.utils.Point;

public class GameEngine implements Observable, Controllable{
    // low level module references
    private ScoreManager scoreManager;

    // next queue / generate piece
    private Queue<TetrominoType> nextQueue;
    private TetrominoGenerator tetrominoGenerator;

    // board
    private int width;
    private int height;
    private List<Tetromino> allTetrominos;
    private Tetromino currentTetromino;
    private Tetromino shadowTetromino;

    // hold
    private TetrominoType holdTetrominoType;
    
    // score? rotation
    private SpinState currentSpinState;

    // gameplay state & spawn
    private double gravity; // move down num per sec
    private double softDropFactor;
    private boolean isSoftDropping = false;
    private ScheduledFuture<?> gravityTask;
    private ScheduledFuture<?> arrTask;
    private Map<Direction, Integer> DASCharges = new HashMap<>(
        Map.of(Direction.LEFT, 0, Direction.RIGHT, 0)
    );
    // private double DAS = 5;
    private double ARR = 0;

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
    public GameEngine(int width, int height, MainThreadDispatcher dispatcher) {
        this.height = height;
        this.width = width;
        this.gravity = 1;

        // configure game, todo: create and use initEngine(JSON? configFile) instead below
        tetrominoGenerator = new BagGenerator();
        nextQueue = new LinkedList<TetrominoType>();
        allTetrominos = new ArrayList<Tetromino>();
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
        if (isSoftDropping && softDropFactor == Double.MAX_VALUE) {
            moveDAS(Direction.DOWN);
        }
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
        allTetrominos = new ArrayList<Tetromino>();
        currentTetromino = null;
        holdTetrominoType = null;
        nextQueue = new LinkedList<TetrominoType>();
        notifyObservers();
    }
    
    public void start() {
        spawn();
        resetGravityTimeline(gravity);
    }

    public void rotate(Rotation rotation) {
        if (!superRotate(rotation)) {
            return;
        }
        updateShadow();

        SpinState currentSpinState = SpinState.NOT_SPIN;
        // detect & let score handle spins
        if (currentTetromino.type == TetrominoType.T) { // detect T-spin
            SpinState threeCornerTSpinState = detectTSpin();
            if (isImmobile()) {
                switch (threeCornerTSpinState) {
                    case PROPER_SPIN -> currentSpinState = SpinState.PROPER_SPIN;
                    case MINI_SPIN -> currentSpinState = SpinState.MINI_SPIN;
                    case NOT_SPIN -> currentSpinState = SpinState.MINI_SPIN;
                    default -> currentSpinState = SpinState.NOT_SPIN;
                }
            } else currentSpinState = threeCornerTSpinState;
            
        } else { // detect other spin
            if (isImmobile())
                currentSpinState = SpinState.MINI_SPIN;
        }

        scoreManager.onSpin(currentSpinState, currentTetromino.type);
    }

    public void move(Direction direction){
        Operator op;
        if (direction == Direction.DOWN) {op = new MoveDownOperator();}
        else if (direction == Direction.LEFT) {op = new MoveLeftOperator();}
        else if (direction == Direction.RIGHT) {op = new MoveRightOperator();}
        else {op = null;}
        tryOperate(op);
        if (direction != Direction.DOWN) {
            updateShadow();
            // handleSoftDropEvent(); // new
        }
    }

    public void moveDAS(Direction direction) {
        Operator op;
        if (direction == Direction.DOWN) {op = new MoveDownOperator();}
        else if (direction == Direction.LEFT) {op = new MoveLeftOperator();}
        else if (direction == Direction.RIGHT) {op = new MoveRightOperator();}
        else {op = null;}

        while (true) {
            if (! tryOperate(op)) {
                break;
            }
            if (direction != Direction.DOWN) {
                // {autoOperationListener.onAutoHorizontalMove();}
                // handleSoftDropEvent();
            }
            else if (direction == Direction.DOWN){
                // {autoOperationListener.onAutoMoveDown();}
                // handleDASEvent();
            }
        }
        if (direction != Direction.DOWN)
            updateShadow();
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
        Tetromino currentTetrominoClone = new Tetromino(currentTetromino);
        if (holdTetrominoType == null) {
            spawn();

        }

        else {
            currentTetromino = new Tetromino(centreTopPoint(), holdTetrominoType); // duplicate_1 2
            allTetrominos.add(currentTetromino);
            handleSoftDropEvent(); // this is temp quick fix, planned refactor
            handleDASEvent();
        }

        holdTetrominoType = currentTetrominoClone.type;
        updateShadow();
    }

    void updateShadow() {
        shadowTetromino = new Tetromino(currentTetromino);
        Tetromino clone = new Tetromino(currentTetromino);
        Operator op = new MoveDownOperator();
        while (!hasCollision(shadowTetromino)) {
            shadowTetromino.moveDown();
        }
        shadowTetromino.moveUp();
        notifyObservers();
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

        //spawn a piece
        TetrominoType currentTetrominoType = nextQueue.remove();
        Tetromino t = new Tetromino(centreTopPoint(), currentTetrominoType); // duplicate_1 1
        currentTetromino = null;
        // when gameover, notify controller
        if (hasCollision(t) || scoreManager.haveAchievedGoal()) {
            listener.onGameOver();
            return;
        }

        currentTetromino = t;
        allTetrominos.add(currentTetromino);
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
     * operates the current Tetromino with collision detection
     * operations includes: horizontal movements, rotation
     * @param op
     * @return true if operation success
     */
    boolean tryOperate(Operator op) {
        if (!canOperate(op)) {
            return false;
        }
        op.operate(currentTetromino);
        checkLanded();
        handleSoftDropEvent();
        handleDASEvent();

        notifyObservers();
        
        return true;
    }

    boolean canOperate(Operator op) {
        Tetromino clone = new Tetromino(currentTetromino);
        op.operate(clone);
        if (hasCollision(clone)) {
            return false;
        }
        return true;
    }


    boolean superRotate(Rotation rotation) {
        Operator op;
        if (rotation == Rotation.CLOCKWISE)              {op = new RotateClockwiseOperator();}
        else if (rotation == Rotation.COUNTER_CLOCKWISE) {op = new RotateCounterClockwiseOperator();}
        else                                             {op = new RotateR180Operator();}
        
        // try without kick
        if (tryOperate(op)) {
            return true;
        }

        // failed without kick

        // 1.  get kick table
        List<Point> kickList = KickTable.getKickList(
            currentTetromino.type,
            currentTetromino.rotationState,
            currentTetromino.rotationState.changeState(rotation)
        );
        

        Tetromino backup = new Tetromino(currentTetromino);
        Point oriPos = new Point(currentTetromino.centre);
        int currentIndex = allTetrominos.indexOf(currentTetromino);
        if (kickList == null) {
            return false;
        }
        for (Point shift: kickList) {
            // 2. move to next position (to try every position 1 by 1)
            Point destination = Point.combine(shift, oriPos);
            while (!currentTetromino.centre.equals(destination)) {
                if (currentTetromino.centre.getX() < destination.getX()) {
                    currentTetromino.moveRight();
                }
                else if (currentTetromino.centre.getX() > destination.getX()) {
                    currentTetromino.moveLeft();
                }
                else if (currentTetromino.centre.getY() < destination.getY()) {
                    currentTetromino.moveDown();
                }
                else if (currentTetromino.centre.getY() > destination.getY()) {
                    currentTetromino.moveUp();
                }
            }
            // 3. try rotate at this position

            if (tryOperate(op)) {
                return true;
            }

            
        }
        // failed all attempts: move currTetro to origin
        currentTetromino = new Tetromino(backup);

        if (currentIndex >= 0) {
            allTetrominos.set(currentIndex, currentTetromino);
        }
        return false;
    }

    /**
     * 
     * @return true if current tetromino is immobile
     */
    boolean isImmobile() {
        Operator[] operators = {new MoveDownOperator(), new MoveLeftOperator(), new MoveRightOperator(), new MoveUpOperator()};
        for (Operator operator : operators) {
            if (canOperate(operator))
                return false;
        }
        return true;
    }
    
    /**
     * standard 3 corners detection, ref: https://tetris.wiki/T-Spin
     * impl by: spawn a single at 4 corners of T, then do collison tests, 
     * if 3/4 corners colide, is T spin, then if 2/2 front corners colide, is proper T spin 
     * @return SpinState: (NOT/MINI/PROPER)_SPIN
     */
    SpinState detectTSpin() { 
        // spawn single mino piece at top-left corner
        Point centre = currentTetromino.getCentre();
        Piece corner = new Piece(new Mino((int)centre.getX() - 1, (int)centre.getY() - 1));

        // rotate to be front-left corner of current T
        int cwRotationForCorner = 0;
        if (currentTetromino.rotationState == RotationState.ZERO)
            ;
        else if (currentTetromino.rotationState == RotationState.RIGHT)
            cwRotationForCorner = 1;
        else if (currentTetromino.rotationState == RotationState.TWO)
            cwRotationForCorner = 2;
        else if (currentTetromino.rotationState == RotationState.LEFT)
            cwRotationForCorner = 3;
        for (int i = 0; i < cwRotationForCorner; i++)
            corner.rotateClockwise(centre);

        // collision tests, (test & rotatecw of corner & next test, 4 in total)
        // first 2: front, last 2: other corners
        int nBlockedFrontCorner = 0;
        int nBlockedOtherCorner = 0;
        for (int i = 0; i < 4; i++) {
            // collision test
            if (hasCollision(corner)) {
                if (i < 2) {
                    nBlockedFrontCorner++;
                } else {
                    nBlockedOtherCorner++;
                }
            }
            corner.rotateClockwise(centre);
        }

        if (nBlockedFrontCorner + nBlockedOtherCorner >= 3) {
            // is a T-spin! then decide proper or mini:
            if (nBlockedFrontCorner == 2) 
                return SpinState.PROPER_SPIN;
            else
                return SpinState.MINI_SPIN;
        }
        return SpinState.NOT_SPIN;
    }

    void lock() {
        int nLinesClearedcleared = checkCompletedLines(currentTetromino);
        scoreManager.onLock(nLinesClearedcleared);
        lockTask.cancel(false);
        spawn();
        // notifyObservers();
    }

    /**
     * check if the tetromino collide with other objects on the board
     * @param m
     * @return
     */
    boolean hasCollision(Piece t) {
        for (Piece other : allTetrominos) {
            if (other == t || other == currentTetromino)
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

    boolean hasWallCollision(Piece t) {
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
     * clear specified lines, update board
     * @param toClear: heights of lines to clear, e.g to clear bot 4 lines, to clear = [16, 17, 18, 19]
     * @param relatedTetrominos: all possible tetrominos to clear
     * 
     * @ret int: lines cleared
     */
    int clearLines(ArrayList<Integer> toClear, ArrayList<Tetromino> relatedTetrominos) { 
        for (Tetromino t : relatedTetrominos) {
            Iterator<Mino> iterator = t.children.iterator();

            while (iterator.hasNext()) {
                Mino mino = iterator.next();
                int minoHeight = (int) mino.getPosition().getY();
                // remove lines 
                if (toClear.contains(minoHeight)) {
                    iterator.remove();
                }

                // shift after-clear residual position
                else {
                    int numToShift = 0;
                    for (int lineCleared : toClear) {
                        if (minoHeight < lineCleared) {
                            numToShift++;
                        }
                    }
                    mino.getPosition().setY(minoHeight + numToShift);
                }
            }
        }


        return toClear.size();
    }

    ArrayList<Integer> findLinesToClear(ArrayList<Tetromino> relatedTetrominos, HashSet<Double> relatedHeights) {
        ArrayList<Integer> linesToClear = new ArrayList<Integer>();
        boolean[][] related_section = new boolean[this.width][relatedHeights.size()]; // true for mino exist, false for empty
        int minHeight = Collections.min(relatedHeights).intValue();
        for (Tetromino t: relatedTetrominos) {
            for (Mino mino: t.children) {
                int x = (int) mino.getPosition().getX();
                int y = (int) mino.getPosition().getY(); 
                
                int relativeY = y - minHeight;
                if (relativeY < 0 || relativeY >= relatedHeights.size()) {
                    continue;
                }
                related_section[x][relativeY] = true;
            }
        }

        for (int y = 0; y < related_section[0].length; y++) {
            boolean completed = true;

            for (int x = 0; x < related_section.length; x++) {
                if (!related_section[x][y]) {
                    completed = false;
                    break;
                }
            }
            if (completed) {
                linesToClear.add(y + minHeight);
            }
        }
        return linesToClear;
    }
    
    HashSet<Double> calcSetOfRelatedHeights(Tetromino t) {
        HashSet<Double> related_heights = new HashSet<Double>();
        for (Mino mino: t.children) {
            related_heights.add(mino.getPosition().getY());
        }
        return related_heights;
    }

    /**
     * lock tetromino trigger this, check for any lines to clear, if exist then clear.
     * @param lastLocked, the tetromino just locked, possible line clears should related with position of it
     */
    int checkCompletedLines(Tetromino lastLocked) {
  
        //
        // group to lower/around/upper
        HashSet<Double> lastLockedHeights = calcSetOfRelatedHeights(lastLocked);
        ArrayList<Tetromino> lower = new ArrayList<Tetromino>();
        ArrayList<Tetromino> around = new ArrayList<Tetromino>();
        ArrayList<Tetromino> upper = new ArrayList<Tetromino>();
        for (Tetromino t : allTetrominos) {
            if (t.children.isEmpty()) {continue;}
            HashSet<Double> tHeights = calcSetOfRelatedHeights(t);
            if (!Collections.disjoint(lastLockedHeights, tHeights)) {
                around.add(t);
            }
            else if (lastLockedHeights.iterator().next() < tHeights.iterator().next()) {
                lower.add(t);
            }
            else {
                upper.add(t);
            }
        }
        // lower: no change
        // around: clear + move down
        int cleared = clearLines(findLinesToClear(around, lastLockedHeights), around);

        // upper: shift minos
        for (Tetromino tetromino : upper) {
            for (Mino mino : tetromino.children) {
                mino.getPosition().setY(mino.getPosition().getY() + cleared);
            }
        }
        return cleared;
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

    public Point centreTopPoint() {
        return new Point((this.width/2)-1, 1);
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
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

    public List<Tetromino> getAllTetrominos() {
        return List.copyOf(allTetrominos);
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

    public Tetromino getShadowTetromino() {
        return this.shadowTetromino;
    } 

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void dispose() {
        scheduler.shutdown();
        scoreManager.dispose();
    }
}
