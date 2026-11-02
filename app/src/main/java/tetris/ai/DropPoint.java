package tetris.ai;

import java.util.ArrayList;
import java.util.List;

import tetris.engine.Tetromino;

/**
 * A Tetromino represents a drop point and how to get to this point
 * DropPoint
 */
public class DropPoint {
    Tetromino tetromino;
    Tetromino ghost;
    List<AIAction> executionOrder;

    public DropPoint() {
    }

    public DropPoint(DropPoint dp) {
        this.tetromino = new Tetromino(dp.tetromino);
        this.ghost = new Tetromino(dp.ghost);
        this.executionOrder = new ArrayList<>(dp.executionOrder);
    }

    @Override
    public String toString() {
        return ghost.toString() + ", execution size: " + executionOrder.size();
    }
}
