package tetris.graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GameOverPane extends VBox {
    private final Label title = new Label();
    private final Button restartButton = new Button("Restart");
    private final Button replayButton = new Button("Play Last Replay");
    private final Button returnButton = new Button("Return to Menu");

    public GameOverPane() {
        setAlignment(Pos.TOP_CENTER);
        setFillWidth(false);
        setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );
        setSpacing(16);
        setPadding(new Insets(16));
        
        setStyle("-fx-border-color: gray; -fx-background-color: -fx-background;");

        this.getChildren().addAll(title, restartButton, replayButton, returnButton);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setOnReplay(Runnable action) {
        replayButton.setOnAction(event -> action.run());
    }

    public void setOnRestart(Runnable action) {
        restartButton.setOnAction(event -> action.run());
    }

    public void setOnReturn(Runnable action) {
        returnButton.setOnAction(event -> action.run());
    }
}
