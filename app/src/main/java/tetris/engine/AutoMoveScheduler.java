package tetris.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tetris.controller.dispatch.MainThreadDispatcher;
import tetris.engine.type.Direction;

public class AutoMoveScheduler {
    private GameEngine engine;
    // private SoftDropHandler softDropHandler;

    // gameplay state & spawn
    public static double INSTANT_MOVE_THRESHOLD = 600.0; // grid per second
    private double gravity; // move down num per sec
    private double softDropFactor;
    private boolean isSoftDropping = false;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> gravityTask;
    private ScheduledFuture<?> arrTask;
    private final MainThreadDispatcher dispatcher;
    private long gameGeneration; // ++when game started/restarted
    private Map<Direction, Integer> DASCharges = new HashMap<>(
        Map.of(Direction.LEFT, 0, Direction.RIGHT, 0)
    );
    // private double ARR = 601;
    private double ARR;
    public AutoMoveScheduler(double gravity, double arr, MainThreadDispatcher dispatcher, GameEngine engine) {
        this.gravity = gravity;
        this.ARR = arr;
        this.engine = engine;
        this.dispatcher = dispatcher;
    }

    public void start() {
        if (scheduler != null && !scheduler.isShutdown()) return;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        resetGravityTimeline(gravity);
    }

    public void onSpawn() {
        applyAutoMoveEffects();
    }
    public void onOperate() {
        applyAutoMoveEffects();
    }
    private void applyAutoMoveEffects() {
        // System.out.println();
        if (softDropFactor * gravity > INSTANT_MOVE_THRESHOLD && ARR > INSTANT_MOVE_THRESHOLD) { 
            // both instant move, choose order
            if (softDropFactor * gravity >= ARR) { // vertical first
                applySoftDropEffect();
                handleDASEvent();
                return ;
            }
            else { // horizontal first
                handleDASEvent();
                applySoftDropEffect();
                return ;
            }
        }
        // one or zero instant move, order doesn't matter
        handleDASEvent();
        applySoftDropEffect();
        // System.out.println();
    }
    private void applySoftDropEffect() {
        // System.out.println("sd");
        if (!isSoftDropping) 
            return ;
        // inf sdf
        if (softDropFactor > INSTANT_MOVE_THRESHOLD) {
            engine.moveDAS(Direction.DOWN);
            return;
        }

        // non inf sdf
        double tempGravity = softDropFactor * gravity;
        if (tempGravity > INSTANT_MOVE_THRESHOLD) {
            engine.moveDAS(Direction.DOWN);
            return;
        }
        resetGravityTimeline(tempGravity);
    }
    private void handleDASEvent(){
        // System.out.println("das");
        if (ARR <= INSTANT_MOVE_THRESHOLD && arrTask != null) {
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

        if (ARR > INSTANT_MOVE_THRESHOLD ) { // ->0 ARR
            try {
                engine.moveDAS(direction);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        else { // non-zero ARR
            // instantly move one step
            engine.move(direction);
            // applySoftDropEffect();
            long periodNanos = (long) (TimeUnit.SECONDS.toNanos(1) / ARR);
            if (scheduler == null || scheduler.isShutdown()) 
                return;
            long ticket = gameGeneration;
            arrTask = scheduler.scheduleAtFixedRate( () -> {
                dispatcher.dispatch(() -> {
                    if (ticket != gameGeneration) // ignore a task from last game before reset
                        return;
                    engine.move(direction);
                });
            }, periodNanos, periodNanos, TimeUnit.NANOSECONDS);
        }
        
    }
    public void startSoftDrop() {
        isSoftDropping = true;
        applySoftDropEffect();
    }

    public void endSoftDrop() {
        isSoftDropping = false;
        if (softDropFactor != Double.MAX_VALUE)
            resetGravityTimeline(engine.getGravity());
    }

    public void setSoftDropFactor(double softDropFactor) {
        this.softDropFactor = softDropFactor;
    }
    public void resetGravityTimeline(double gravity){
        if (scheduler == null || scheduler.isShutdown()) return;
        if (gravityTask != null) {
            gravityTask.cancel(false);
        }
        long ticket = gameGeneration;
        long periodNanos = (long) (TimeUnit.SECONDS.toNanos(1) / gravity);
        gravityTask = scheduler.scheduleAtFixedRate( () -> {
            dispatcher.dispatch(() -> {
                if (ticket != gameGeneration) return; // ignore task from last game
                if (engine.getBoard().getcurrentTetromino() == null) return; // for some forgotten reason, check current piece is not null
                engine.move(Direction.DOWN);
            });
        }, 0, periodNanos, TimeUnit.NANOSECONDS);
    }

    public void chargeDAS(Direction direction) {
        DASCharges.put(direction, DASCharges.get(direction) + 1);
        handleDASEvent();
    }  
    
    public void unchargeDAS(Direction direction) {
        DASCharges.put(direction, DASCharges.get(direction) - 1);
        handleDASEvent();
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getGravity() {
        return gravity;
    }

    public void reset() {
        gameGeneration++;
        isSoftDropping = false;
        DASCharges.replaceAll((direction, charge) -> 0);
        if (arrTask != null) arrTask.cancel(false);
        resetGravityTimeline(gravity);
    }

    public void dispose() {
        gameGeneration++;
        if (scheduler != null) scheduler.shutdownNow();
    }
}
