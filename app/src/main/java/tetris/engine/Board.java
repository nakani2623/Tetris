package tetris.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.operator.MoveDownOperator;
import tetris.engine.operator.Operator;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

/** Coordinates increase upward: floor y=0, visible top y=height-1. */
public class Board implements Observable{
    // board
    private int width;
    private int height;
    List<Piece> allTetrominos;
    Tetromino currentTetromino;
    private Tetromino shadowTetromino;

    private List<Observer> observers = new ArrayList<>();


    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        allTetrominos = new ArrayList<Piece>();

    }

    public Board(Board source) {
        this.width = source.width;
        this.height = source.height;
        this.allTetrominos = new ArrayList<>(source.allTetrominos.size());
        Map<Piece, Piece> copies = new IdentityHashMap<>();
        for (Piece piece : source.allTetrominos) {
            if (piece == source.currentTetromino) {
                Tetromino currentTetrominoCopy = source.currentTetromino.deepCopy();
                this.currentTetromino = currentTetrominoCopy;
                this.allTetrominos.add(currentTetrominoCopy);
                continue;
            }
            Piece copy = piece.deepCopy();
            this.allTetrominos.add(copy);
            copies.putIfAbsent(piece, copy);
        }
        if (source.shadowTetromino != null)
            this.shadowTetromino = source.shadowTetromino.deepCopy();
    }

    public void reset() {
        allTetrominos = new ArrayList<Piece>();
        currentTetromino = null;
        notifyObservers();
    }
    
    /**
     * operates the current Tetromino with collision detection
     * operations includes: horizontal movements, rotation
     * @param op
     * @return true if operation success
     */
    public boolean tryOperate(Operator op) {
        if (!canOperate(op)) {
            return false;
        }
        op.operate(getcurrentTetromino());
        updateShadow();
        notifyObservers();
        
        return true;
    }
    

    public boolean canOperate(Operator op) {
        Tetromino clone = currentTetromino.deepCopy();
        op.operate(clone);
        if (hasCollision(clone)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return bool: success or not
     */
    public boolean spawn(TetrominoType currentTetrominoType) {
        Point spawnPoint = spawnPoint(currentTetrominoType);
        Tetromino t = new Tetromino(spawnPoint, currentTetrominoType); // duplicate_1 1
        currentTetromino = null;
        if (hasCollision(t)) {
            return false;
        }
        currentTetromino = t;
        allTetrominos.add(currentTetromino);
        updateShadow();
        return true;
    }

    public Point spawnPoint(TetrominoType type) {
        Point centre = new Point((this.width/2)-1, height - 2);
        if (type == TetrominoType.I) {
            centre.setX(centre.getX() + 0.5);
            centre.setY(centre.getY() - 0.5);

        } else if (type == TetrominoType.O) {
            centre.setX(centre.getX() + 0.5);
            centre.setY(centre.getY() + 0.5);
        }

        return centre;
    }
 
    void updateShadow() {
        shadowTetromino = currentTetromino.deepCopy();
        Operator op = new MoveDownOperator();
        while (!hasCollision(shadowTetromino)) {
            shadowTetromino.moveDown();
        }
        shadowTetromino.moveUp();
        notifyObservers();
    }
    /*
    -------------
    - COLLISION -
    -------------
    */
     /**
     * check if the tetromino collide with other objects on the board
     * @param m
     * @return
     */
    public boolean hasCollision(Piece t) {
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
            if (y < 0) {
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
        Tetromino clone  = t.deepCopy();
        clone.moveDown();

        if (hasCollision(clone)) {
            return true;
        }
        return false;
    }
    /*
    ------------
    -LINE CLEAR-
    ------------
    */

    public int clearCompleted() {
        return checkCompletedLines(currentTetromino);
    }
    /**
     * clear specified lines, update board
     * @param toClear: heights of lines to clear, e.g to clear bot 4 lines, to clear = [0, 1, 2, 3]
     * @param relatedTetrominos: all possible tetrominos to clear
     * 
     * @ret int: lines cleared
     */
    int clearLines(ArrayList<Integer> toClear, ArrayList<Piece> relatedTetrominos) { 
        for (Piece t : relatedTetrominos) {
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
                        if (minoHeight > lineCleared) {
                            numToShift++;
                        }
                    }
                    mino.getPosition().setY(minoHeight - numToShift);
                }
            }
        }
        return toClear.size();
    }

    ArrayList<Integer> findLinesToClear(ArrayList<Piece> relatedTetrominos, HashSet<Double> relatedHeights) {
        ArrayList<Integer> linesToClear = new ArrayList<Integer>();
        boolean[][] related_section = new boolean[this.width][relatedHeights.size()]; // true for mino exist, false for empty
        int minHeight = Collections.min(relatedHeights).intValue();
        for (Piece t: relatedTetrominos) {
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
    
    HashSet<Double> calcSetOfRelatedHeights(Piece t) {
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
        ArrayList<Piece> lower = new ArrayList<Piece>();
        ArrayList<Piece> around = new ArrayList<Piece>();
        ArrayList<Piece> upper = new ArrayList<Piece>();
        for (Piece t : allTetrominos) {
            if (t.children.isEmpty()) {continue;}
            HashSet<Double> tHeights = calcSetOfRelatedHeights(t);
            if (!Collections.disjoint(lastLockedHeights, tHeights)) {
                around.add(t);
            }
            else if (lastLockedHeights.iterator().next() > tHeights.iterator().next()) {
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
        for (Piece tetromino : upper) {
            for (Mino mino : tetromino.children) {
                mino.getPosition().setY(mino.getPosition().getY() - cleared);
            }
        }
        return cleared;
    }

    public Tetromino getcurrentTetromino() {
        return currentTetromino;
    }

    public List<Piece> getAllTetrominos() {
        return allTetrominos;
    }

    public void setCurrentTetromino(Tetromino tetromino) {
        currentTetromino =tetromino;
    }
    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Tetromino getShadowTetromino() {
        return this.shadowTetromino;
    }

    public double getGravity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGravity'");
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
