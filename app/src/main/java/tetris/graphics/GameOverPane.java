package tetris.graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GameOverPane extends VBox {
        private Button restartButton;
        private Button returnButton;
    public GameOverPane() {
        Label title = new Label("GAME OVER!");
        restartButton = new Button("Restart");
        returnButton = new Button("Return to Menu"); 

        setAlignment(Pos.TOP_CENTER);
        setFillWidth(false);
        setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );
        setSpacing(16);
        setPadding(new Insets(16));
        setStyle("-fx-border-color: gray;");

        this.getChildren().addAll(title, restartButton, returnButton);
    }

    public void setOnRestart(Runnable action) {
        restartButton.setOnAction(event -> action.run());
    }

    public void setOnReturn(Runnable action) {
        returnButton.setOnAction(event -> action.run());
    }
}
