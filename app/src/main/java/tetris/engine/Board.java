package tetris.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import tetris.engine.operator.MoveDownOperator;
import tetris.engine.operator.MoveLeftOperator;
import tetris.engine.operator.MoveRightOperator;
import tetris.engine.operator.Operator;
import tetris.engine.type.Direction;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class Board {
    // board
    private int width;
    private int height;
    private List<Tetromino> allTetrominos;
    private Tetromino currentTetromino;
    private Tetromino shadowTetromino;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        allTetrominos = new ArrayList<Tetromino>();

    }

    public void reset() {
        allTetrominos = new ArrayList<Tetromino>();
        currentTetromino = null;
    }
    
    /**
     * 
     * @return bool: success or not
     */
    public boolean spawn() {
        TetrominoType currentTetrominoType = nextQueue.remove();
        Tetromino t = new Tetromino(centreTopPoint(), currentTetrominoType); // duplicate_1 1
        currentTetromino = null;
        if (hasCollision(t)) {
            return false;
        }
        currentTetromino = t;
        allTetrominos.add(currentTetromino);
        return true;
    }

    public Point centreTopPoint() {
        return new Point((this.width/2)-1, 1);
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
    
    /*
    ------------
    -LINE CLEAR-
    ------------
    */
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

    public Tetromino getcurrentTetromino() {
        return currentTetromino;
    }

    public List<Tetromino> getAllTetrominos() {
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
}
