package tetris.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tetris.controller.dispatch.MainThreadDispatcher;
import tetris.engine.type.Direction;
import tetris.engine.type.GameState;
import tetris.engine.type.Rotation;
import tetris.engine.type.UnifiedKey;
import tetris.engine.type.Input.GameKey;
import tetris.graphics.GameWindow;

/**
 * Handles user inputs, timing
 * GameController
 */
public class GameController implements GameOverListener {
    private Controllable engine;
    private GameWindow window;
    private MainThreadDispatcher dispatcher;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gravityTask;
    private ScheduledFuture<?> arrTask;

    // private Timeline arrTimeline;
    private double softDropFactor;
    private GameState gameState;
    private Set<UnifiedKey> pressedKeys = new HashSet<>(); 
    private Map<Direction, Integer> DASCharges = new HashMap<Direction, Integer>();
    private Map<UnifiedKey, ScheduledFuture<?>> DASChargingKeys = new HashMap<UnifiedKey, ScheduledFuture<?>>();
    private List<UnifiedKey> DASChargedKeys = new LinkedList<UnifiedKey>();
    private boolean isSoftDropping = false;
    private double DAS = 5;
    private double ARR = 0;

    public GameController(Controllable engine, GameWindow window, MainThreadDispatcher dispatcher) {
        this.engine = engine;
        this.window = window;
        this.dispatcher = dispatcher;

        DASCharges.put(Direction.LEFT, 0);
        DASCharges.put(Direction.RIGHT, 0);
    }

    public void start() {
        gameState = GameState.active;
        engine.start();
        window.showGame();
        resetGravityTimeline(engine.getGravity());
    }

    public void restart() {
        reset();
        start();
    }
    
    public void showMenu() {
        reset();
        gameState = GameState.inMenu;
        window.showMenu();
    }

    private void reset() {
        pressedKeys.clear();
        engine.reset();
        window.onStart();
    }


    /**
     * called when das staus changed or new piece spawned and notified by engine
     */
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
                engine.moveDAS(direction);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        else { // non-zero ARR
            // instantly move one step
            horizontalMove(direction);
            long duration = (long)(ARR * 1000 / 60);
            arrTask = scheduler.scheduleAtFixedRate( () -> {
                dispatcher.dispatch(() -> {
                    horizontalMove(direction);
                });
            }, 0, duration, TimeUnit.MILLISECONDS);
        }
        
    }
    /**
     * controller need to handle das for next current piece
     * when engine lock piece by timer, engine call this method onSpawn
     */
    public void onAutoSpawn() {
        handleDASEvent();
        handleSoftDropEvent();
    }
    
    public void onAutoHorizontalMove() {
        handleSoftDropEvent();
    }

    private void startCharging(UnifiedKey key, Direction direction) {
        long delay = (long)(DAS * 1000 / 60);
        // long delay = (long)(DAS * 1000000000 / 60);
        ScheduledFuture chargeDAS = scheduler.schedule(() -> {
            dispatcher.dispatch(() -> {            
                DASCharges.put(direction, DASCharges.get(direction) + 1);
                DASChargedKeys.add(key);
                handleDASEvent();
                DASChargingKeys.remove(key);});
        }, delay, TimeUnit.MILLISECONDS);
        DASChargingKeys.put(key, chargeDAS);

    }
    private void endCharging(UnifiedKey key, Direction direction) {
        if (DASChargingKeys.containsKey(key)) {
            this.DASChargingKeys.get(key).cancel(false);
        }

        if (DASChargedKeys.contains(key)) {
            DASCharges.put(direction, DASCharges.get(direction) - 1);
            DASChargedKeys.remove(key);
        }
        handleDASEvent();
        
    }
    public void resetGravityTimeline(double gravity){
        if (gravityTask != null) {
            gravityTask.cancel(false);
        }
        long duration = (long)(1000 / gravity);
        gravityTask = scheduler.scheduleAtFixedRate( () -> {
            dispatcher.dispatch(() -> {
                try {
                    engine.move(Direction.DOWN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }, 0, duration, TimeUnit.MILLISECONDS);
    }

    private void handleSoftDropEvent() {
        if (isSoftDropping && softDropFactor == Double.MAX_VALUE) {
            engine.moveDAS(Direction.DOWN);
        }
    }
    private void startSoftDrop() {
        isSoftDropping = true;
        if (softDropFactor != Double.MAX_VALUE) {
            resetGravityTimeline(engine.getGravity() * softDropFactor);
        }
        
    }

    private void endSoftDrop() {
        isSoftDropping = false;
        if (softDropFactor != Double.MAX_VALUE)
            resetGravityTimeline(engine.getGravity());
    }

    public void handleKeyPressed(GameKey key, UnifiedKey physicalKey) {
        if (!pressedKeys.add(physicalKey)) { // Ignore repeated KEY_DOWN events
            return;
        }

        if (key == null)
            return;

        if (gameState == GameState.ended) {
            if (key == GameKey.RESTART) {
                restart();
            }
            return;
        }

        if (gameState != GameState.active) {
            return;
        }

        switch (key) {
            case MOVE_LEFT ->
                horizontalMovePressed(physicalKey, Direction.LEFT);

            case MOVE_RIGHT ->
                horizontalMovePressed(physicalKey, Direction.RIGHT);

            case HARDDROP ->
                engine.hardDrop();

            case SOFTDROP ->
                startSoftDrop();

            case HOLD ->
                engine.hold();

            case ROTATE_CCW ->
                engine.rotate(Rotation.COUNTER_CLOCKWISE);

            case ROTATE_CW ->
                engine.rotate(Rotation.CLOCKWISE);

            case ROTATE_180 ->
                engine.rotate(Rotation.R_180);
        }

        // Must execute after each newly pressed key
        handleDASEvent();
        handleSoftDropEvent();
    }

    public void handleKeyReleased(GameKey key, UnifiedKey physicalKey) {
        pressedKeys.remove(physicalKey);

        if (gameState != GameState.active) {
            return;
        }

        if (key == null)
            return;

        switch (key) {
            case SOFTDROP ->
                endSoftDrop();

            case MOVE_LEFT ->
                horizontalMoveReleased(physicalKey, Direction.LEFT);

            case MOVE_RIGHT ->
                horizontalMoveReleased(physicalKey, Direction.RIGHT);

            default -> {}
        }
    }

    public void setSoftDropFactor(double softDropFactor) {
        this.softDropFactor = softDropFactor;
    }
    
    @Override 
    public void onGameOver() {
        scheduler.shutdown();
        gameState = GameState.ended;
        window.onGameOver();
    }

    /**
     * @param direction only accept LEFT or RIGHT
     * called when binded "<-"/"->" pressed
     * start/end das and arr timeline
     * operate engine by move/moveDAS
     */
    public void horizontalMovePressed(UnifiedKey key, Direction direction){
        horizontalMove(direction);
        startCharging(key, direction);
    }

    /**
     * @param direction only accept LEFT or RIGHT
     * called when binded "<-"/"->" released
     * end das timeline
     * operate engine by move/moveDAS
     */
    public void horizontalMoveReleased(UnifiedKey key, Direction direction){
        // end charging/ remove charged
        endCharging(key, direction);
    }

    /** 
     * controller call this function to move tetromino,
     * it let engine move than check softdrop event
    */
    private void horizontalMove(Direction direction) {
        engine.move(direction);
        // check if needed to repeat softdrop event
        handleSoftDropEvent();
    }

    public GameState getGameState() {
        return gameState;
    }
    
}
