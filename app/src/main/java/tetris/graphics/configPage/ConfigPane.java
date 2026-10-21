package tetris.graphics.configPage;


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
        HBox moveLeft = new HBox(moveLeftLabel, moveLeftButton);

        Label moveRightLabel = new Label("Move Right");
        KeyBindButton moveRightButton = new KeyBindButton(GameKey.MOVE_RIGHT, GameState.active, keyBinds);
        HBox moveRight = new HBox(moveRightLabel, moveRightButton);

        Label rotateCWLabel = new Label("Rotate Clockwise");
        KeyBindButton rotateCWButton = new KeyBindButton(GameKey.ROTATE_CW, GameState.active, keyBinds);
        HBox rotateCW = new HBox(rotateCWLabel, rotateCWButton);

        Label rotateCCWLabel = new Label("Rotate Counter-Clockwise");
        KeyBindButton rotateCCWButton = new KeyBindButton(GameKey.ROTATE_CCW, GameState.active, keyBinds);
        HBox rotateCCW = new HBox(rotateCCWLabel, rotateCCWButton);
    
        Label rotate180Label = new Label("Rotate 180");
        KeyBindButton rotate180Button = new KeyBindButton(GameKey.ROTATE_180, GameState.active, keyBinds);
        HBox rotate180 = new HBox(rotate180Label, rotate180Button);

        Label holdLabel = new Label("Hold");
        KeyBindButton holdButton = new KeyBindButton(GameKey.HOLD, GameState.active, keyBinds);
        HBox hold = new HBox(holdLabel, holdButton);

        Label softDropLabel = new Label("Soft Drop");
        KeyBindButton softDropButton = new KeyBindButton(GameKey.SOFTDROP, GameState.active, keyBinds);
        HBox softDrop = new HBox(softDropLabel, softDropButton);

        Label hardDropLabel = new Label("Hard Drop");
        KeyBindButton hardDropButton = new KeyBindButton(GameKey.HARDDROP, GameState.active, keyBinds);
        HBox hardDrop = new HBox(hardDropLabel, hardDropButton);

        VBox content = new VBox(title, moveLeft, moveRight, rotateCW, rotateCCW, rotate180, hold, softDrop, hardDrop, returnMenu);
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

