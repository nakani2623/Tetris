package tetris.graphics;

import tetris.engine.GameEngine;
import tetris.engine.observers.BoardDisplayManager;
import tetris.engine.observers.ScoreDisplayManager;
import tetris.engine.type.Direction;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import tetris.engine.Board;

/**
 * Manages In-Game contents, including a gameboard
 */
public class GameWindow {
    static final int MINO_SIZE = 20;
    Pane root;
    GameEngine engine;
    Board board;
    // Rectangle[][] grid;
    // Set<Rectangle> updatingSquares;

    public GameWindow(GameEngine gameEngine) {
        this.engine = gameEngine;
        this.board = engine.getBoard();
        root = new HBox();

        BoardPane boardPane = new BoardPane(engine.getBoard());
        root.getChildren().add(boardPane);
        new BoardDisplayManager(engine.getBoard(), boardPane);

        ScorePane scorePanel = new ScorePane();
        root.getChildren().add(scorePanel);
        new ScoreDisplayManager(engine.getScore(), scorePanel);
    }

    public void execute(Stage stage) {
        Scene scene = new Scene(root, 640, 480);

        // keyboard controller, triggers tetromino movement functions
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT -> board.moveCurrent(Direction.LEFT);
                case RIGHT -> board.moveCurrent(Direction.RIGHT);
            }
        });
        
        stage.setScene(scene);
        stage.show();
    }

    public Pane getRoot() {
        return root;
    }
}
