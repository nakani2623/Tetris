package tetris.graphics;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuPane extends VBox {
    private Button startButton;
    private Button exitButton;
    public MenuPane() {
        Label title = new Label("Menu");
        startButton = new Button("Start game");
        exitButton = new Button("Exit");
        this.getChildren().addAll(title, startButton, exitButton);
    }

    public void setOnStart(Runnable action) {
        this.startButton.setOnAction(event -> action.run());
    }

    public void setOnExit(Runnable action) {
        this.exitButton.setOnAction(event -> action.run());
    }

}
