package tetris.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import tetris.controller.Controllable;
import tetris.engine.type.Direction;
import tetris.engine.type.GameState;
import tetris.engine.type.Rotation;
import tetris.graphics.GameWindow;

/**
 * Handles user inputs, timing
 * GameController
 */
public class GameController implements GameOverListener, AutoOperationListener{
    private Controllable engine;
    private GameWindow window;

    private Timeline gravityTimeline;
    private Timeline arrTimeline;
    private double softDropFactor;
    private GameState gameState;
    private Set<KeyCode> pressedKeys = new HashSet<>(); 
    private Map<Direction, Integer> DASCharges = new HashMap<Direction, Integer>();
    private Map<KeyCode, Timeline> DASChargingKeys = new HashMap<KeyCode, Timeline>();
    private List<KeyCode> DASChargedKeys = new LinkedList<KeyCode>();
    private boolean isSoftDropping = false;
    private double DAS = 5;
    private double ARR = 2;

    public GameController(Controllable engine, GameWindow window, GameState gameState) {
        this.engine = engine;
        this.window = window;
        this.gameState = gameState;

        addKeyboardControl();
        showMenu();
        DASCharges.put(Direction.LEFT, 0);
        DASCharges.put(Direction.RIGHT, 0);
    }

    public void start() {
        gameState = GameState.active;
        engine.start();

        resetGravityTimeline(engine.getGravity());
    }

    public void restart() {
        engine.reset();
        window.onStart();
        start();
    }

    public void showGame() {
        window.showGame();
        start();    
    }

    public void showMenu() {
        window.showMenu();
    }


    /**
     * called when das staus changed or new piece spawned and notified by engine
     */
    private void handleDASEvent(){
        if (ARR != 0 && arrTimeline != null) {
            // stop arr timeline
            arrTimeline.stop();
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
            engine.moveDAS(direction);
        }
        else { // non-zero ARR
            // instantly move one step
            horizontalMove(direction);
            arrTimeline = new Timeline(
                new KeyFrame(
                    Duration.seconds(ARR/60), event -> {
                        horizontalMove(direction);
                    }
                )
            );
            arrTimeline.setCycleCount(Timeline.INDEFINITE);
            arrTimeline.play();
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

    private void startCharging(KeyCode key, Direction direction) {
        Timeline t = new Timeline(
            new KeyFrame(
                Duration.seconds(DAS/60), event -> {
                    DASCharges.put(direction, DASCharges.get(direction) + 1);
                    DASChargedKeys.add(key);
                    handleDASEvent();
                }
            )
        );
        t.setOnFinished(event -> {
            DASChargingKeys.remove(key);
        });
        t.play();
        DASChargingKeys.put(key, t);
    }
    private void endCharging(KeyCode key, Direction direction) {
        if (DASChargingKeys.containsKey(key)) {
            this.DASChargingKeys.get(key).stop();
        }

        if (DASChargedKeys.contains(key)) {
            DASCharges.put(direction, DASCharges.get(direction) - 1);
            DASChargedKeys.remove(key);
        }
        handleDASEvent();
        
    }
    public void resetGravityTimeline(double gravity){
        if (gravityTimeline != null) {
            gravityTimeline.stop();
        }

        gravityTimeline = new Timeline(new KeyFrame(Duration.seconds(1/gravity), event -> engine.move(Direction.DOWN)));
        gravityTimeline.setCycleCount(Timeline.INDEFINITE);
        gravityTimeline.play();
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

    // keyboard controller, triggers tetromino movement functions
    public void addKeyboardControl() {
        window.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
        if (!pressedKeys.add(event.getCode())) { // failed adding to set -> same key not released yet -> is system's auto repeat
            return ;
        }
            if (gameState == GameState.ended) {
                if (event.getCode() == KeyCode.R) {
                    restart();
                }
                return ;
            }

            if (gameState == GameState.active) {              
                switch (event.getCode()) {
                    case J -> horizontalMovePressed(event.getCode(), Direction.LEFT);
                    case L -> horizontalMovePressed(event.getCode(), Direction.RIGHT);
                    case F -> engine.hardDrop();
                    case K -> startSoftDrop();
                    case S -> engine.hold();
                    case A -> engine.rotate(Rotation.COUNTER_CLOCKWISE);
                    case D -> engine.rotate(Rotation.CLOCKWISE);
                    case SEMICOLON -> engine.rotate(Rotation.R_180);
                    
                    case Q -> engine.rotate(Rotation.COUNTER_CLOCKWISE);
                    case W -> engine.hold();
                    case E -> engine.rotate(Rotation.CLOCKWISE);
                    case R -> engine.rotate(Rotation.R_180);
                    // case I -> engine.move(Direction.LEFT);
                    case SPACE -> engine.hardDrop();

                }
                // for ARR = 0, das need to be handled after any input
                handleDASEvent();
                handleSoftDropEvent();
            }

        });

        window.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            pressedKeys.remove(event.getCode());
            if (engine.getGameState() == GameState.ended) {
                return;
            }

            if (gameState == GameState.active) {              
                switch (event.getCode()) {
                    case K -> endSoftDrop();
                    case J -> horizontalMoveReleased(event.getCode(), Direction.LEFT);
                    case L -> horizontalMoveReleased(event.getCode(), Direction.RIGHT);
                }
            }
        });

    }

    public void setSoftDropFactor(double softDropFactor) {
        this.softDropFactor = softDropFactor;
    }
    
    @Override 
    public void onGameOver() {
        gravityTimeline.stop();
        gameState = GameState.ended;
        window.onGameOver();
    }

    /**
     * @param direction only accept LEFT or RIGHT
     * called when binded "<-"/"->" pressed
     * start/end das and arr timeline
     * operate engine by move/moveDAS
     */
    public void horizontalMovePressed(KeyCode key, Direction direction){
        horizontalMove(direction);
        startCharging(key, direction);
    }

    /**
     * @param direction only accept LEFT or RIGHT
     * called when binded "<-"/"->" released
     * end das timeline
     * operate engine by move/moveDAS
     */
    public void horizontalMoveReleased(KeyCode key, Direction direction){
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
}
