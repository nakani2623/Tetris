package tetris.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tetris.engine.GameEngine;
import tetris.engine.Tetromino;

/**
 * PointExporer
 * find all possible drop points for a tetromino given a board state.
 */
public class DropPointExporer {
    GameEngine engine;
    private Set<AIAction> actions = new HashSet<>();
    private Set<DropPoint> dropPoints = new HashSet<>();

    public DropPointExporer(GameEngine engine) {
        this.engine = engine;
        actions.add(AIAction.MOVE_LEFT);
        actions.add(AIAction.MOVE_RIGHT);
        actions.add(AIAction.ROTATE_CLOCKWISE);
        actions.add(AIAction.ROTATE_COUNTER_CLOCKWISE);
    }

    private void exploreFrom(DropPoint baseline) {
        if (isKnown(baseline)) {
            return;
        }

        dropPoints.add(baseline);

        for (AIAction action : actions) {
            engine.resetCurrentTetromino(baseline.tetromino);
            action.execute(engine);

            DropPoint newDropPoint = new DropPoint();
            List<AIAction> executionOrder = new ArrayList<>(baseline.executionOrder);
            executionOrder.add(action);
            newDropPoint.executionOrder = executionOrder;
            newDropPoint.tetromino = new Tetromino(engine.getCurrentTetromino());
            newDropPoint.ghost = new Tetromino(engine.getShadowTetromino());

            exploreFrom(newDropPoint);
        }
    }

    private boolean isKnown(DropPoint newPoint) {
        for (DropPoint known : dropPoints) {
            if (newPoint.ghost.isSamePosition(known.ghost))
                return true;
        }
        return false;
    }

    public Set<DropPoint> get() {
        DropPoint origin = new DropPoint();
        origin.executionOrder = new ArrayList<>();
        origin.tetromino = new Tetromino(engine.getCurrentTetromino());
        origin.ghost = new Tetromino(engine.getShadowTetromino());

        exploreFrom(origin);

        for (DropPoint dp : dropPoints) {
            dp.executionOrder.add(AIAction.HARD_DROP);
        }

        return dropPoints;
    }

    public static void main() {
        GameEngine engine = new GameEngine(10, 20, null);
        DropPointExporer explorer = new DropPointExporer(engine);
        engine.start();

        for (DropPoint dp : explorer.get()) {
            System.out.println(dp);
        }

        System.out.println("Total drop points: " + explorer.get().size());
    }
}