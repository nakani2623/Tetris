package tetris.graphics.gameActive;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tetris.graphics.GameOverPane;

public class GamePane extends StackPane {
    private final BoardPane boardPane = new BoardPane();
    private final ScorePane scorePane = new ScorePane();
    private final HoldPane holdPane = new HoldPane();
    private final NextQueuePane nextQueuePane = new NextQueuePane();
    private SpinClearPane spinClearPane = new SpinClearPane();


    private GameOverPane gameOverModal;
    public GamePane() {
        VBox leftBox = new VBox(holdPane, scorePane, spinClearPane);

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

    public SpinClearPane getSpinClearPane() {
        return spinClearPane;
    }

    public HoldPane getHoldPane() {
        return holdPane;
    }

    public NextQueuePane getNextQueuePane() {
        return nextQueuePane;
    }
}
