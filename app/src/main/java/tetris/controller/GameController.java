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
import tetris.graphics.GameWindow;

/**
 * Handles user inputs, timing
 * GameController
 */
public class GameController {
    private GameEngine engine;
    private GameWindow window;

    private Timeline gravityTimeline;
    private double softDropFactor;

    public GameController(GameEngine engine, GameWindow window) {
        this.engine = engine;
        this.window = window;

        addKeyboardControl();
        resetGravityTimeline(engine.getGravity());
    }

    public void resetGravityTimeline(double gravity){
        if (gravityTimeline != null) {
            gravityTimeline.stop();
        }

        gravityTimeline = new Timeline(new KeyFrame(Duration.seconds(1/gravity), event -> engine.operate(new MoveDownOperator())));
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
            if (engine.getGameState() == GameState.ended) {
                if (event.getCode() == KeyCode.R) {
                    engine.reset();
                    engine.start();
                }
            }

            if (engine.getGameState() == GameState.active) {              
                switch (event.getCode()) {
                    case J -> engine.operate(new MoveLeftOperator());
                    case L -> engine.operate(new MoveRightOperator());
                    case F -> engine.hardDrop();
                    case K -> startSoftDrop();
                    case A -> engine.operate(new RotateCounterClockwiseOperator());
                    case D -> engine.operate(new RotateClockwiseOperator());
                    case SEMICOLON -> engine.operate(new RotateR180Operator());
                }
            }

        });

        window.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (engine.getGameState() == GameState.ended) {
                return;
            }

            if (engine.getGameState() == GameState.active) {              
                switch (event.getCode()) {
                    case K -> endSoftDrop();
                }
            }
        });

    }

    public void setSoftDropFactor(double softDropFactor) {
        this.softDropFactor = softDropFactor;
    }
}
