package tetris.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import tetris.engine.type.Direction;

public class AutoMoveScheduler {
    // gameplay state & spawn
    private double gravity; // move down num per sec
    private double softDropFactor;
    private boolean isSoftDropping = false;
    private ScheduledFuture<?> gravityTask;
    private ScheduledFuture<?> arrTask;
    private Map<Direction, Integer> DASCharges = new HashMap<>(
        Map.of(Direction.LEFT, 0, Direction.RIGHT, 0)
    );
    private double ARR = 0;
}
