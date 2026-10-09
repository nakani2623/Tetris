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
import tetris.engine.GameEngine;
import tetris.engine.type.Direction;
import tetris.engine.type.GameState;
import tetris.engine.type.Rotation;
import tetris.graphics.GamePane;
import tetris.graphics.GameWindow;
import tetris.graphics.MenuPane;

/**
 * Handles user inputs, timing
 * GameController
 */
public class GameController implements GameOverListener, SpawnListener{
    private Controllable engine;
    private GameWindow window;

    private Timeline gravityTimeline;
    private double softDropFactor;
    private GameState gameState;
    private Set<KeyCode> pressedKeys = new HashSet<>(); 
    private Map<Direction, Integer> DASCharges = new HashMap<Direction, Integer>();
    private Map<KeyCode, Timeline> DASChargingKeys = new HashMap<KeyCode, Timeline>();
    private List<KeyCode> DASChargedKeys = new LinkedList<KeyCode>();
    private double DAS = 5;
    private double ARR = 1;

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

       public void showGame() {
        window.getScene().setRoot(window.getGamePane());
        start();
    }

    public void showMenu() {
        MenuPane menuPane = new MenuPane(this);
        window.getScene().setRoot(menuPane);
    }


    /**
     * called when das staus changed or new piece spawned and notified by engine
     */
    private void handleDASEvent(){
        int leftDAS = DASCharges.get(Direction.LEFT);
        int rightDAS = DASCharges.get(Direction.RIGHT);
        if (leftDAS - rightDAS == 0) {
            return;
        }
        else if (leftDAS - rightDAS > 0) { // left
            engine.moveDAS(Direction.LEFT);
        }
        else {// right
            engine.moveDAS(Direction.RIGHT);
        } 
    }
    /**
     * controller need to handle das for next current piece
     * when engine lock piece by timer, engine call this method onSpawn
     */
    public void onSpawn() {
        handleDASEvent();
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

    public void startSoftDrop() {
        resetGravityTimeline(engine.getGravity() * softDropFactor);
    }

    public void endSoftDrop() {
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
                    engine.reset();
                    start();
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
    }

    /**
     * @param direction only accept LEFT or RIGHT
     * called when binded "<-"/"->" pressed
     * start/end das and arr timeline
     * operate engine by move/moveDAS
     */
    public void horizontalMovePressed(KeyCode key, Direction direction){
        engine.move(direction);
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
}
