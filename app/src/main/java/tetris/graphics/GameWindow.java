package tetris.graphics;

import tetris.engine.GameEngine;
import tetris.engine.observers.BoardDisplayManager;
import tetris.engine.observers.ScoreDisplayManager;
import javafx.scene.Scene;
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
        stage.setScene(scene);
        stage.show();
    }

    public Pane getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
