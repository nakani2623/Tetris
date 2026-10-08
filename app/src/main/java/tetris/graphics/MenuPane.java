package tetris.graphics;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tetris.controller.GameController;

public class MenuPane extends VBox {
    private GameController controller;
    public MenuPane(GameController controller) {
        Label title = new Label("Menu");
        Button startButton = new Button("Start game");
        Button exitButton = new Button("Exit");

        startButton.setOnAction(event -> controller.showGame());
        this.getChildren().addAll(title, startButton, exitButton);
    }
}
