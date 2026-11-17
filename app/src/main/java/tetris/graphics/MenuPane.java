package tetris.graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuPane extends StackPane {
    private Button singlePlayerButton;
    private Button exitButton;
    private Button configButton;
    public MenuPane() {
        Label title = new Label("Tetris");
        title.getStyleClass().add("menu-title");
        title.setId("main-title");
        singlePlayerButton = new Button("Single \n player");
        exitButton = new Button("Exit");
        configButton = new Button("Config");

        HBox menuBar = new HBox(singlePlayerButton, configButton, exitButton);
        menuBar.getStyleClass().add("menu-bar");
        singlePlayerButton.getStyleClass().addAll("menu-option", "tetromino-i");
        configButton.getStyleClass().addAll("menu-option", "tetromino-t");
        exitButton.getStyleClass().addAll("menu-option", "tetromino-z");

        VBox content = new VBox(title, menuBar);
        this.getChildren().add(content);

        content.setAlignment(Pos.TOP_CENTER);
        content.setFillWidth(false);
        content.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );
        content.setSpacing(16);
        content.setPadding(new Insets(16));
        content.setStyle("-fx-border-color: gray;");
    }

    public void setOnSinglePlayer(Runnable action) {
        this.singlePlayerButton.setOnAction(event -> action.run());
    }

    public void setOnExit(Runnable action) {
        this.exitButton.setOnAction(event -> action.run());
    }

    public void setOnConfig(Runnable action) {
        this.configButton.setOnAction(event -> action.run());
    }

}
