package tetris.graphics;

import javafx.scene.Parent;
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
    final GamePane gamePane;
    final MenuPane menuPane;

    GameEngine engine;
    // Rectangle[][] grid;
    // Set<Rectangle> updatingSquares;

    public GameWindow(GameEngine gameEngine) {
        this.engine = gameEngine;

        gamePane = new GamePane(engine);
        menuPane = new MenuPane();
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

    public GamePane getGamePane() {
        return this.gamePane;
    }

    public MenuPane getMenuPane() {
        return this.menuPane;
    }

    public void showContent(Parent content) {
        scene.setRoot(content);
    }

    public void setOnStart(Runnable action) {
        menuPane.setOnStart(action);
    }
}
