package tetris.graphics;

import tetris.controller.input.KeyBinds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SinglePlayerPane extends StackPane {
    private Button sprintButton;
    private Button cheeseSprintButton;
    private Button returnButton;
    private Button zenButton;
    public SinglePlayerPane(KeyBinds bindings) {
        Label title = new Label("Single player");
        title.getStyleClass().add("menu-title");
        sprintButton = new Button("Sprint");
        cheeseSprintButton = new Button("Cheese \nsprint");
        returnButton = new Button("Return\n to\n  Menu");
        zenButton = new Button("Zen");

        MenuOptionBar menuBar = new MenuOptionBar(bindings, returnButton::fire, sprintButton, cheeseSprintButton, zenButton, returnButton);
        sprintButton.getStyleClass().addAll("menu-option", "tetromino-j");
        cheeseSprintButton.getStyleClass().addAll("menu-option", "tetromino-o");
        zenButton.getStyleClass().addAll("menu-option", "tetromino-s");
        returnButton.getStyleClass().addAll("menu-option", "tetromino-l");

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

    public void setOnSprint(Runnable action) {
        this.sprintButton.setOnAction(event -> action.run());
    }

    public void setOnCheeseSprint(Runnable action) {
        this.cheeseSprintButton.setOnAction(event -> action.run());
    }

    public void setOnReturnMenu(Runnable action) {
        this.returnButton.setOnAction(event -> action.run());
    }

    public void setOnZen(Runnable action) {
        this.zenButton.setOnAction(event -> action.run());
    }

}
