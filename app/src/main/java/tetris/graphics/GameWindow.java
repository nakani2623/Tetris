package tetris.graphics;

import tetris.engine.GameEngine;
import tetris.engine.observers.BoardDisplayManager;
import tetris.engine.observers.ScoreDisplayManager;
import tetris.engine.type.Direction;
import tetris.engine.type.GameState;
import tetris.engine.type.Rotation;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

/**
 * Manages In-Game contents, including a gameboard
 */
public class GameWindow {
    static final int MINO_SIZE = 20;
    Scene scene;
    Pane root;
    GameEngine engine;
    // Rectangle[][] grid;
    // Set<Rectangle> updatingSquares;

    public GameWindow(GameEngine gameEngine) {
        this.engine = gameEngine;
        root = new HBox();
        scene = new Scene(root, 640, 480);

        BoardPane boardPane = new BoardPane(engine);
        root.getChildren().add(boardPane);
        new BoardDisplayManager(engine, boardPane);

        ScorePane scorePanel = new ScorePane();
        root.getChildren().add(scorePanel);
        new ScoreDisplayManager(engine.getScore(), scorePanel);
    }

    public void execute(Stage stage) {
        // keyboard controller, triggers tetromino movement functions
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (engine.getGameState() == GameState.ended) {
                if (event.getCode() == KeyCode.R) {
                    engine.reset();
                    engine.start();
                }
            }

            if (engine.getGameState() == GameState.active) {              
                switch (event.getCode()) {
                    case J -> engine.moveCurrent(Direction.LEFT);
                    case L -> engine.moveCurrent(Direction.RIGHT);
                    case F -> engine.hardDrop();
                    case A -> engine.rotateCurrent(Rotation.COUNTER_CLOCKWISE);
                    case D -> engine.rotateCurrent(Rotation.CLOCKWISE);
                    case SEMICOLON -> engine.rotateCurrent(Rotation.R_180);
                }
            }

        });
        
        stage.setScene(scene);
        stage.show();
    }

    public Pane getRoot() {
        return root;
    }
}
