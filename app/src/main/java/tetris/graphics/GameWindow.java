package tetris.graphics;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tetris.engine.GameEngine;

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
        Pane initialPane = new Pane();
        scene = new Scene(initialPane, 640, 480);
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
