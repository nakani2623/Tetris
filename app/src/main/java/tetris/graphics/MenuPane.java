package tetris.graphics;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuPane extends VBox{
    public MenuPane() {
        Label title = new Label("Menu");
        Button startButton = new Button("Start game");
        Button exitButton = new Button("Exit");
        this.getChildren().addAll(title, startButton, exitButton);
    }
}
