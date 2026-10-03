package tetris.graphics;

import tetris.engine.GameEngine;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

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
        scene = new Scene(null, 640, 480);
        showGame(scene);
    }

    public void showGame(Scene scene) {
        GamePane gamePane = new GamePane(engine);
        scene.setRoot(gamePane);
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
