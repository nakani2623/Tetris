package tetris.graphics;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import tetris.engine.type.SinglePlayGameMode;
import javafx.stage.Stage;
import tetris.controller.input.KeyBinds;
import tetris.graphics.configPage.ConfigPane;
import tetris.graphics.gameActive.BoardPane;
import tetris.graphics.gameActive.GamePane;
import tetris.graphics.gameActive.HoldPane;
import tetris.graphics.gameActive.NextQueuePane;
import tetris.graphics.gameActive.ScorePane;
import tetris.graphics.gameActive.SpinClearPane;

/**
 * Manages In-Game contents, including a gameboard
 */
public class GameWindow {
    private final GamePane gamePane = new GamePane();
    private final MenuPane menuPane;
    private final SinglePlayerPane singlePlayerPane;
    private final ConfigPane configPane ;
    private final Scene scene;
    private final VideoBackground background = new VideoBackground();
    private final StackPane root = new StackPane(background);
    private SinglePlayGameMode gameMode = SinglePlayGameMode.SPRINT;

    public GameWindow(KeyBinds keyBinds) {
        menuPane = new MenuPane(keyBinds);
        singlePlayerPane = new SinglePlayerPane(keyBinds);
        scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(
                getClass().getResource("/styles/theme.css").toExternalForm()
        );
        menuPane.setOnConfig(this::showConfig);
        menuPane.setOnSinglePlayer(this::showSinglePlayer);
        singlePlayerPane.setOnReturnMenu(this::showMenu);
        configPane = new ConfigPane(keyBinds);
        configPane.setOnReturnMenu(this::showMenu);
    }

    public void execute(Stage stage) {
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public void showGame() {
        showContent(gamePane, switch (gameMode) {
            case SPRINT -> "/video/sprint_bg.mp4";
            case CHEESE_SPRINT -> "/video/cheese_sprint_bg.mp4";
            case ZEN -> "/video/zen_bg.mp4";
        });
    }

    public void showMenu() {
        showContent(menuPane, "/video/main_menu_bg.mp4");
    }

    public void showSinglePlayer() {
        showContent(singlePlayerPane, "/video/mode_select_bg.mp4");
    }

    public void showConfig() {
        showContent(configPane, null);
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

    public SpinClearPane getSpinClearPane() {
        return gamePane.getSpinClearPane();
    }

    public HoldPane getHoldPane() {
        return gamePane.getHoldPane();
    }

    public NextQueuePane getNextQueuePane() {
        return gamePane.getNextQueuePane();
    }

    private void showContent(Parent content, String video) {
        background.showVideo(video);
        root.getChildren().setAll(background, content);
    }

    public void setGameMode(SinglePlayGameMode mode) {
        gameMode = mode;
    }

    public void dispose() {
        background.dispose();
    }

    public void setOnSprint(Runnable action) {
        singlePlayerPane.setOnSprint(action);
    }

    public void setOnCheeseSprint(Runnable action) {
        singlePlayerPane.setOnCheeseSprint(action);
    }

    public void setOnZen(Runnable action) {
        singlePlayerPane.setOnZen(action);
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
