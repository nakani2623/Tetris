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
    private Board board;
    // private SoftDropHandler softDropHandler;

    // gameplay state & spawn
    private double gravity; // move down num per sec
    private double softDropFactor;
    private boolean isSoftDropping = false;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gravityTask;
    private ScheduledFuture<?> arrTask;
    private MainThreadDispatcher dispatcher;
    private Map<Direction, Integer> DASCharges = new HashMap<>(
        Map.of(Direction.LEFT, 0, Direction.RIGHT, 0)
    );
    private double ARR = 0;
    public AutoMoveScheduler(double gravity, double arr) {
        this.gravity = gravity;
        this.ARR = arr;
    }

    public void onStart() {
        resetGravityTimeline(gravity);
    }

    public void onSpawn() {
        applySoftDropEffect();
        handleDASEvent();
    }
    private void applySoftDropEffect() {
        // inf sdf
        if (softDropFactor == Double.MAX_VALUE) {
            board.moveDAS(Direction.DOWN);
            return;
        }

        // non inf sdf
        double tempGravity = softDropFactor * gravity;
        if (tempGravity > 50) {
            board.moveDAS(Direction.DOWN);
            return;
        }
        resetGravityTimeline(tempGravity);
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
                board.moveDAS(direction);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        else { // non-zero ARR
            // instantly move one step
            // horizontalMove(direction);
            board.move(direction);
            applySoftDropEffect();
            long duration = (long)(ARR * 1000 / 60);
            arrTask = scheduler.scheduleAtFixedRate( () -> {
                dispatcher.dispatch(() -> {
                    // horizontalMove(direction);
                    board.move(direction);
                    applySoftDropEffect();
                });
            }, 0, duration, TimeUnit.MILLISECONDS);
        }
        
    }
    public void startSoftDrop() {
        isSoftDropping = true;
        if (softDropFactor != Double.MAX_VALUE) {
            resetGravityTimeline(board.getGravity() * softDropFactor);
        }
        applySoftDropEffect();
    }

    public void endSoftDrop() {
        isSoftDropping = false;
        if (softDropFactor != Double.MAX_VALUE)
            resetGravityTimeline(board.getGravity());
    }

    public void setSoftDropFactor(double softDropFactor) {
        this.softDropFactor = softDropFactor;
    }
    public void resetGravityTimeline(double gravity){
        if (gravityTask != null) {
            gravityTask.cancel(false);
        }
        long duration = (long)(1000 / gravity);
        gravityTask = scheduler.scheduleAtFixedRate( () -> {
            dispatcher.dispatch(() -> {
                try {
                    board.move(Direction.DOWN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }, 0, duration, TimeUnit.MILLISECONDS);
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


}
