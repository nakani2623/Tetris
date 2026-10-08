package tetris.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import tetris.engine.GameEngine;
import tetris.engine.operator.MoveDownOperator;
import tetris.engine.operator.MoveLeftOperator;
import tetris.engine.operator.MoveRightOperator;
import tetris.engine.operator.RotateClockwiseOperator;
import tetris.engine.operator.RotateCounterClockwiseOperator;
import tetris.engine.operator.RotateR180Operator;
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
    private GameEngine engine;
    private GameWindow window;

    private Timeline gravityTimeline;
    private double softDropFactor;
    private GameState gameState;

    public GameController(GameEngine engine, GameWindow window, GameState gameState) {
        this.engine = engine;
        this.window = window;
        this.gameState = gameState;

        addKeyboardControl();
        showMenu();
    }

    public void start() {
        gameState = GameState.active;
        resetGravityTimeline(engine.getGravity());
        engine.spawn();
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

        gravityTimeline = new Timeline(new KeyFrame(Duration.seconds(1/gravity), event -> engine.tryOperate(new MoveDownOperator())));
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
            if (gameState == GameState.ended) {
                if (event.getCode() == KeyCode.R) {
                    engine.reset();
                    start();
                }
            }

            if (gameState == GameState.active) {              
                switch (event.getCode()) {
                    case J -> engine.tryOperate(new MoveLeftOperator());
                    case L -> engine.tryOperate(new MoveRightOperator());
                    case F -> engine.hardDrop();
                    case K -> startSoftDrop();
                    case S -> engine.hold();
                    case A -> engine.tryOperate(new RotateCounterClockwiseOperator());
                    case D -> engine.tryOperate(new RotateClockwiseOperator());
                    case SEMICOLON -> engine.tryOperate(new RotateR180Operator());
                    
                    case Q -> engine.superRotate(Rotation.COUNTER_CLOCKWISE);
                    case W -> engine.hold();
                    case E -> engine.superRotate(Rotation.CLOCKWISE);
                    case R -> engine.superRotate(Rotation.R_180);
                    case SPACE -> engine.hardDrop();
                }
            }

        });

        window.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (gameState == GameState.ended) {
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
