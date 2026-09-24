package tetris.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;
import java.util.LinkedList;

import tetris.engine.generatingStrategy.BagGenerator;
import tetris.engine.generatingStrategy.TetrominoGenerator;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.operator.MoveDownOperator;
import tetris.engine.operator.Operator;
import tetris.engine.rotationStrategy.RotationStrategy;
import tetris.engine.rotationStrategy.classicRotation;
import tetris.engine.type.GameState;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class GameEngine implements Observable{
    private Score score;
    private Queue<TetrominoType> nextQueue;
    private TetrominoGenerator tetrominoGenerator;
    private LockStrategy lockStrategy;
    private RotationStrategy rotationStrategy;
    private GameState gameState;

    private int width;
    private int height;
    public List<Tetromino> allTetrominos;
    public Tetromino currentTetromino;
    List<Observer> observers;

    public Operator op;
    
    /**
     * Constructs game bord with specific dimension
     * @param width
     * @param height
     */
    public GameEngine(int width, int height) {
        nextQueue = new LinkedList<TetrominoType>();
        observers = new ArrayList<>();

        // configure game, todo: create and use initEngine(JSON? configFile) instead below
        tetrominoGenerator = new BagGenerator();
        rotationStrategy = new classicRotation();
        score = new Score(this);


        this.height = height;
        this.width = width;

        allTetrominos = new ArrayList<Tetromino>();
        // upcomingTetrominos = new LinkedList<>();
        observers = new ArrayList<Observer>();

    }

    /**
     * Constructs default game board 10x20 dimension
     */
    public GameEngine() {
        this(10, 20);
    }

    /**
     * Reset the board state into initial
     */
    public void reset() {
        score.reset();
        allTetrominos = new ArrayList<Tetromino>();
        currentTetromino = null;
        op = null;
        nextQueue = new LinkedList<TetrominoType>();
        notifyObservers();
    }

    public void spawn() {
                // populate next queue
        while (nextQueue.size() < 7) {
            nextQueue.addAll(tetrominoGenerator.generateTetrominos());
        }

        //spawn a piece
        TetrominoType currentTetrominoType = nextQueue.remove();
        Tetromino t = new Tetromino(centreTopPoint(), currentTetrominoType);
        currentTetromino = null;
        if (hasCollision(t)) {
            setGameState(GameState.ended);
            return;

        }

        currentTetromino = t;
        allTetrominos.add(currentTetromino);
        notifyObservers();
    }

    /**
     * operates the current Tetromino with collision detection
     * operations includes: horizontal movements, rotation
     * @param op
     */
    public void operate(Operator op) {
        Tetromino clone = new Tetromino(currentTetromino);
        op.operate(clone);

        if (!hasCollision(clone)) {
            op.operate(currentTetromino);
            notifyObservers();
        }
    }

    public void hardDrop() {
        op = new MoveDownOperator();

        Tetromino clone = new Tetromino(currentTetromino);
        op.operate(clone);

        while (!hasCollision(clone)) {
            op.operate(currentTetromino);
            op.operate(clone);
        }

        checkCompletedLines(currentTetromino);
        spawn();
        notifyObservers();
    }

    /**
     * check if the tetromino collide with other objects on the board
     * @param m
     * @return
     */
    public boolean hasCollision(Tetromino t) {
        for (Tetromino other : allTetrominos) {
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
    
    /**
     * clear specified lines, update board
     * @param toClear: heights of lines to clear, e.g to clear bot 4 lines, to clear = [16, 17, 18, 19]
     * @param relatedTetrominos: all possible tetrominos to clear
     * 
     * @ret int: lines cleared
     */
    private int clearLines(ArrayList<Integer> toClear, ArrayList<Tetromino> relatedTetrominos) { 


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

    private ArrayList<Integer> findLinesToClear(ArrayList<Tetromino> relatedTetrominos, HashSet<Double> relatedHeights) {
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
    
    private HashSet<Double> calcSetOfRelatedHeights(Tetromino t) {
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
    public void checkCompletedLines(Tetromino lastLocked) {
  
        //
        // group to lower/around/upper
        HashSet<Double> lastLockedHeights = calcSetOfRelatedHeights(lastLocked);
        //System.out.println("lock heights: "+lastLockedHeights);
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

    public void start() {
        gameState = GameState.active;
        spawn();
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
