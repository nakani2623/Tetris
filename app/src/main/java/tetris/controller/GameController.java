package tetris.controller;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
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
public class GameController implements GameOverListener {
    private Controllable engine;
    private GameWindow window;

    private Timeline gravityTimeline;
    private double softDropFactor;
    private GameState gameState;
    private Set<KeyCode> pressedKeys = new HashSet<>(); 

    public GameController(Controllable engine, GameWindow window, GameState gameState) {
        this.engine = engine;
        this.window = window;
        this.gameState = gameState;

        addKeyboardControl();
        showMenu();
    }

    public void start() {
        gameState = GameState.active;
        resetGravityTimeline(engine.getGravity());
        engine.start();
    }

       public void showGame() {
        GamePane gamePane = new GamePane(engine);
        window.getScene().setRoot(gamePane);
        start();
    }

    public void showMenu() {
        MenuPane menuPane = new MenuPane(this);
        window.getScene().setRoot(menuPane);
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
            if (engine.getGameState() == GameState.ended) {
                if (event.getCode() == KeyCode.R) {
                    engine.reset();
                    start();
                }
            }

            if (gameState == GameState.active) {              
                switch (event.getCode()) {
                    case J -> engine.move(Direction.LEFT);
                    case L -> engine.move(Direction.RIGHT);
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
                    case SPACE -> engine.hardDrop();

                }
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
}
