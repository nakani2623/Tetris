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
    private static final int MINO_SIZE = 20;
    private Scene scene;
    private Pane root;
    private final GamePane gamePane;
    private final MenuPane menuPane;

    private GameEngine engine;
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

    public void showGame() {
        showContent(gamePane);
    }

    public void showMenu() {
        showContent(menuPane);
    }

    public Pane getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    private void showContent(Parent content) {
        scene.setRoot(content);
    }

    public void setOnStart(Runnable action) {
        menuPane.setOnStart(action);
    }

    public void setOnExit(Runnable action) {
        menuPane.setOnExit(action);
    }

    public void setOnRestart(Runnable action) {
        gamePane.setOnRestart(action);
    }

    public void setOnReturn(Runnable action) {
        gamePane.setOnReturn(action);
    }

    public void onGameOver() {
        gamePane.showGameOver();
    }

    public void onStart() {
        gamePane.hideGameOver();
    }
}
