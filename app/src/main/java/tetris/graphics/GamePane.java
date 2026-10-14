package tetris.graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GamePane extends StackPane {
    private BoardPane boardPane;
    private ScorePane scorePane;
    private HoldPane holdPane;
    private NextQueuePane nextQueuePane;

    private GameOverPane gameOverModal;
    public GamePane() {
        scorePane = new ScorePane();
        holdPane = new HoldPane();
        VBox leftBox = new VBox(holdPane, scorePane);

        boardPane = new BoardPane();
        nextQueuePane = new NextQueuePane();

        HBox content = new HBox(16, leftBox, boardPane, nextQueuePane);
        content.setAlignment(Pos.TOP_CENTER);
        content.setFillHeight(false);

        content.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );

        setPadding(new Insets(20));
        this.getChildren().add(content);

        gameOverModal = new GameOverPane();
        this.getChildren().add(gameOverModal);
        hideGameOver();
    }

    public void showGameOver() {
        gameOverModal.setVisible(true);
    }

    public void hideGameOver() {
        gameOverModal.setVisible(false);
    }


    public void setOnRestart(Runnable action) {
        gameOverModal.setOnRestart(action);
    }

    public void setOnReturn(Runnable action) {
        gameOverModal.setOnReturn(action);
    }

    public BoardPane getBoardPane() {
        return boardPane;
    }

    public ScorePane getScorePane() {
        return scorePane;
    }

    public HoldPane getHoldPane() {
        return holdPane;
    }

    public NextQueuePane getNextQueuePane() {
        return nextQueuePane;
    }
}
