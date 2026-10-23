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
import tetris.engine.observers.Observer;
import tetris.engine.type.GameState;
import tetris.engine.type.Input.GameKey;

public class ConfigPane extends StackPane implements Observer {
    private final KeyBinds keyBinds;

    Label title = new Label("Config");
    GridPane configTable = new GridPane();

    Label moveLeftLabel = new Label("Move Left");
    KeyBindButton moveLeftButton;

    Label moveRightLabel = new Label("Move Right");
    KeyBindButton moveRightButton;

    Label rotateCWLabel = new Label("Rotate Clockwise");
    KeyBindButton rotateCWButton;

    Label rotateCCWLabel = new Label("Rotate Counter-Clockwise");
    KeyBindButton rotateCCWButton;

    Label rotate180Label = new Label("Rotate 180");
    KeyBindButton rotate180Button;

    Label holdLabel = new Label("Hold");
    KeyBindButton holdButton;

    Label softDropLabel = new Label("Soft Drop");
    KeyBindButton softDropButton;

    Label hardDropLabel = new Label("Hard Drop");
    KeyBindButton hardDropButton;

    private Button returnMenu = new Button("Back");

    public ConfigPane(KeyBinds keyBinds) {
        this.keyBinds = keyBinds;

        moveLeftButton = new KeyBindButton(GameKey.MOVE_LEFT, GameState.active, this.keyBinds);
        moveRightButton = new KeyBindButton(GameKey.MOVE_RIGHT, GameState.active, this.keyBinds);
        rotateCWButton = new KeyBindButton(GameKey.ROTATE_CW, GameState.active, this.keyBinds);
        rotateCCWButton = new KeyBindButton(GameKey.ROTATE_CCW, GameState.active, this.keyBinds);
        rotate180Button = new KeyBindButton(GameKey.ROTATE_180, GameState.active, this.keyBinds);
        holdButton = new KeyBindButton(GameKey.HOLD, GameState.active, this.keyBinds);
        softDropButton = new KeyBindButton(GameKey.SOFTDROP, GameState.active, this.keyBinds);
        hardDropButton = new KeyBindButton(GameKey.HARDDROP, GameState.active, this.keyBinds);

        configTable.addColumn(0, moveLeftLabel, moveRightLabel, rotateCWLabel, rotateCCWLabel, rotate180Label, holdLabel, softDropLabel, hardDropLabel);
        configTable.addColumn(1, moveLeftButton, moveRightButton, rotateCWButton, rotateCCWButton, rotate180Button, holdButton, softDropButton, hardDropButton);

        VBox content = new VBox(title, configTable, returnMenu);
        this.getChildren().add(content);

        // Bind observers
        moveLeftButton.addObserver(this);
        moveRightButton.addObserver(this);
        rotateCWButton.addObserver(this);
        rotateCCWButton.addObserver(this);
        rotate180Button.addObserver(this);
        holdButton.addObserver(this);
        softDropButton.addObserver(this);
        hardDropButton.addObserver(this);

        // Styling
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

    @Override
    public void update() {
        moveLeftButton.updateButtonText();
        moveRightButton.updateButtonText();
        rotateCWButton.updateButtonText();
        rotateCCWButton.updateButtonText();
        rotate180Button.updateButtonText();
        holdButton.updateButtonText();
        softDropButton.updateButtonText();
        hardDropButton.updateButtonText();
    }
}

