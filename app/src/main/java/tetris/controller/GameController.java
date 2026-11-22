package tetris.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tetris.controller.dispatch.MainThreadDispatcher;
import tetris.engine.GameEngine;
import tetris.engine.type.Direction;
import tetris.engine.type.GameState;
import tetris.engine.type.Rotation;
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
    private Map<GameKey, ScheduledFuture<?>> scheduledDASMoves = new HashMap<GameKey, ScheduledFuture<?>>();
    private List<GameKey> DASChargedKeys = new LinkedList<GameKey>();
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
        engine.reset();
        window.onStart();
    }

    private void startCharging(GameKey key, Direction direction) {
        long delayNanos = (long) (DAS * TimeUnit.SECONDS.toNanos(1) / 60.0);
        ScheduledFuture<?> chargeDAS = scheduler.schedule(() -> {
            dispatcher.dispatch(() -> {
                engine.chargeDAS(direction);
                DASChargedKeys.add(key);
                scheduledDASMoves.remove(key);
            });
        }, delayNanos, TimeUnit.NANOSECONDS);

        scheduledDASMoves.put(key, chargeDAS);
    }
    private void endCharging(GameKey key, Direction direction) {
        if (scheduledDASMoves.containsKey(key)) {
            this.scheduledDASMoves.get(key).cancel(false);
        }

        if (DASChargedKeys.contains(key)) {
            engine.unchargeDAS(direction);
            DASChargedKeys.remove(key);
        }
        
    }

    public void recordKeyEvent() {

    }


    public void handleKeyPressed(GameKey key) {
        if (key == null || gameState != GameState.active)
            return;

        if (gameState == GameState.ended) {
            if (key == GameKey.RESTART) {
                restart();
            }
            return;
        }

        switch (key) {    
            case MOVE_LEFT -> {
                engine.move(Direction.LEFT);
                startCharging(key, Direction.LEFT);
            }
            case MOVE_RIGHT -> {
                engine.move(Direction.RIGHT);
                startCharging(key, Direction.RIGHT);
            }

            case HARDDROP -> engine.hardDrop();
            case SOFTDROP -> engine.startSoftDrop();
            case HOLD -> engine.hold();
            case ROTATE_CCW -> engine.rotate(Rotation.COUNTER_CLOCKWISE);
            case ROTATE_CW -> engine.rotate(Rotation.CLOCKWISE);
            case ROTATE_180 -> engine.rotate(Rotation.R_180);         
            case RESTART -> System.out.println("Can't handle this input: NOT IMPLEMENTED");
        }

    }

    public void handleKeyReleased(GameKey key) {
        if (key == null || gameState != GameState.active)
            return;

        switch (key) {
            case SOFTDROP -> engine.endSoftDrop();
            case MOVE_LEFT -> endCharging(key, Direction.LEFT);
            case MOVE_RIGHT -> endCharging(key, Direction.RIGHT);
            default -> {}
        }
    }
    
    public void dispose() {
        scheduler.shutdown();
        engine.dispose();
    }

    @Override 
    public void onGameOver() {
        gameState = GameState.ended;
        window.onGameOver();
    }

    public GameState getGameState() {
        return gameState;
    }
}
