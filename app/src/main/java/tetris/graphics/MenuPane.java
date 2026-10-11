package tetris.graphics;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuPane extends VBox {
    private Button startButton;
    public MenuPane() {
        Label title = new Label("Menu");
        startButton = new Button("Start game");
        Button exitButton = new Button("Exit");
        this.getChildren().addAll(title, startButton, exitButton);
    }

    public void setOnStart(Runnable action) {
        this.startButton.setOnAction(event -> action.run());
    }
}
