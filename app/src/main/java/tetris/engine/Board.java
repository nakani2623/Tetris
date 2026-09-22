package tetris.engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.operator.MoveDownOperator;
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
    public GameEngine engine;
    
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

    public void hardDrop() {
        op = new MoveDownOperator();

        Tetromino clone = new Tetromino(currentTetromino);
        while (true) {
            op.operate(clone);

            if (!hasCollision(clone)) {
                op.operate(currentTetromino);
            }

            else {
                break;
            }
        }
        // because hard drop LOCK a piece, check for clear
        checkCompletedLines(currentTetromino);

        //spawn a new tetromino
        engine.spawn();
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

    private int clearLines(ArrayList<Integer> toClear, ArrayList<Tetromino> relatedTetrominos) { 
        /**
         * clear specified lines, update board
         * @param toClear: heights of lines to clear, e.g to clear bot 4 lines, to clear = [16, 17, 18, 19]
         * @param relatedTetrominos: all possible tetrominos to clear
         * 
         * @ret int: lines cleared
         */

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
    private String relatedSectionToString(boolean[][] relatedSection) {
    if (relatedSection == null || relatedSection.length == 0) {
            return "";
        }

        int width = relatedSection.length;
        int height = relatedSection[0].length;

        StringBuilder result = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.append(relatedSection[x][y] ? "■ " : "· ");
            }
            result.append('\n');
        }

        return result.toString();
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
        // System.out.println(relatedSectionToString(related_section));
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
    public void checkCompletedLines(Tetromino lastLocked) {
        /**
         * lock tetromino trigger this, check for any lines to clear, if exist then clear.
         * @param lastLocked, the tetromino just locked, possible line clears should related with position of it
         */
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
