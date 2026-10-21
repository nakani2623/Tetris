package tetris.graphics;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tetris.controller.input.KeyBinds;
import tetris.engine.type.GameState;
import tetris.engine.type.Input.GameKey;

public class ConfigPane extends StackPane {
    private Button returnMenu = new Button("Back");

    public ConfigPane(KeyBinds keyBinds) {
        Label title = new Label("Config");

        Label moveLeftLabel = new Label("Move Left");
        KeyBindButton moveLeftButton = new KeyBindButton(GameKey.MOVE_LEFT, GameState.active, keyBinds);
        HBox moveLeftConfig = new HBox(moveLeftLabel, moveLeftButton);

        Label moveRightLabel = new Label("Move Right");
        Button moveRightButton = new Button();
        HBox moveRightConfig = new HBox(moveRightLabel, moveRightButton);

        VBox content = new VBox(title, moveLeftConfig, moveRightConfig, returnMenu);
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

    public void setOnReturnMenu(Runnable action) {
        returnMenu.setOnAction(event -> action.run());
    }
}

