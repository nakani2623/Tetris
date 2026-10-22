package tetris.graphics.configPage;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
        GridPane configTable = new GridPane();

        Label moveLeftLabel = new Label("Move Left");
        KeyBindButton moveLeftButton = new KeyBindButton(GameKey.MOVE_LEFT, GameState.active, keyBinds);

        Label moveRightLabel = new Label("Move Right");
        KeyBindButton moveRightButton = new KeyBindButton(GameKey.MOVE_RIGHT, GameState.active, keyBinds);

        Label rotateCWLabel = new Label("Rotate Clockwise");
        KeyBindButton rotateCWButton = new KeyBindButton(GameKey.ROTATE_CW, GameState.active, keyBinds);

        Label rotateCCWLabel = new Label("Rotate Counter-Clockwise");
        KeyBindButton rotateCCWButton = new KeyBindButton(GameKey.ROTATE_CCW, GameState.active, keyBinds);
    
        Label rotate180Label = new Label("Rotate 180");
        KeyBindButton rotate180Button = new KeyBindButton(GameKey.ROTATE_180, GameState.active, keyBinds);

        Label holdLabel = new Label("Hold");
        KeyBindButton holdButton = new KeyBindButton(GameKey.HOLD, GameState.active, keyBinds);

        Label softDropLabel = new Label("Soft Drop");
        KeyBindButton softDropButton = new KeyBindButton(GameKey.SOFTDROP, GameState.active, keyBinds);

        Label hardDropLabel = new Label("Hard Drop");
        KeyBindButton hardDropButton = new KeyBindButton(GameKey.HARDDROP, GameState.active, keyBinds);

        configTable.addColumn(0, moveLeftLabel, moveRightLabel, rotateCWLabel, rotateCCWLabel, rotate180Label, holdLabel, softDropLabel, hardDropLabel);
        configTable.addColumn(1, moveLeftButton, moveRightButton, rotateCWButton, rotateCCWButton, rotate180Button, holdButton, softDropButton, hardDropButton);

        VBox content = new VBox(title, configTable, returnMenu);
        // VBox content = new VBox(title, moveLeft, moveRight, rotateCW, rotateCCW, rotate180, hold, softDrop, hardDrop, returnMenu);
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

