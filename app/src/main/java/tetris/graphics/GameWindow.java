package tetris.graphics;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Manages In-Game contents, including a gameboard
 */
public class GameWindow {
    private final GamePane gamePane = new GamePane();
    private final MenuPane menuPane = new MenuPane();
    private final Scene scene;

    public GameWindow() {
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

    public Scene getScene() {
        return scene;
    }

    public BoardPane getBoardPane() {
        return gamePane.getBoardPane();
    }

    public ScorePane getScorePane() {
        return gamePane.getScorePane();
    }

    public HoldPane getHoldPane() {
        return gamePane.getHoldPane();
    }

    public NextQueuePane getNextQueuePane() {
        return gamePane.getNextQueuePane();
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
