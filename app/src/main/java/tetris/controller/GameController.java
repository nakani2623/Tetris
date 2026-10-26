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
import tetris.engine.GameEngine;
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
    private GameEngine engine;
    private GameWindow window;
    private MainThreadDispatcher dispatcher;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Map<UnifiedKey, ScheduledFuture<?>> DASChargingKeys = new HashMap<UnifiedKey, ScheduledFuture<?>>();
    private Set<UnifiedKey> pressedKeys = new HashSet<>(); 
    private List<UnifiedKey> DASChargedKeys = new LinkedList<UnifiedKey>();
    private double DAS = 5;

    private GameState gameState;

    public GameController(GameEngine engine, GameWindow window, MainThreadDispatcher dispatcher) {
        this.engine = engine;
        this.window = window;
        this.dispatcher = dispatcher;
    }

    public void start() {
        gameState = GameState.active;
        engine.start();
        window.showGame();
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

    private void startCharging(UnifiedKey key, Direction direction) {
        long delay = (long)(DAS * 1000 / 60);
        ScheduledFuture chargeDAS = scheduler.schedule(() -> {
         
            dispatcher.dispatch(() -> {
                engine.chargeDAS(direction);
                DASChargedKeys.add(key);
                // handleDASEvent();
                DASChargingKeys.remove(key);
            });
        }, delay, TimeUnit.MILLISECONDS);
        DASChargingKeys.put(key, chargeDAS);

    }
    private void endCharging(UnifiedKey key, Direction direction) {
        if (DASChargingKeys.containsKey(key)) {
            this.DASChargingKeys.get(key).cancel(false);
        }

        if (DASChargedKeys.contains(key)) {
            engine.unchargeDAS(direction);
            DASChargedKeys.remove(key);
        }
        
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
                engine.startSoftDrop();

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
        // handleDASEvent();
        // handleSoftDropEvent();
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
                engine.endSoftDrop();

            case MOVE_LEFT ->
                horizontalMoveReleased(physicalKey, Direction.LEFT);

            case MOVE_RIGHT ->
                horizontalMoveReleased(physicalKey, Direction.RIGHT);

            default -> {}
        }
    }

    // public void setSoftDropFactor(double softDropFactor) {
    //     this.softDropFactor = softDropFactor;
    // }
    
    public void dispose() {
        scheduler.shutdown();
    }

    @Override 
    public void onGameOver() {
        dispose();
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
        // handleSoftDropEvent();
    }

    public GameState getGameState() {
        return gameState;
    }

}
