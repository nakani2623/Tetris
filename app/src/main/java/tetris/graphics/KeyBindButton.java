package tetris.graphics;

import javafx.scene.control.Button;
import tetris.controller.input.JavaFXKeyboardAdapter;
import tetris.controller.input.KeyBinds;
import tetris.engine.type.GameState;
import tetris.engine.type.UnifiedKey;
import tetris.engine.type.Input.GameKey;

public class KeyBindButton extends Button {
    GameKey bindingGameKey; 
    GameState gamestate;

    public KeyBindButton(GameKey bindingGameKey, GameState gamestate) {
        this.bindingGameKey = bindingGameKey;
        this.gamestate = gamestate;

        this.setOnAction(event -> {
            this.setText("Press a key ...");
            startBind();
        });
    }

    public void startBind() {
        this.setOnKeyPressed(keyEvent -> {
            UnifiedKey uKey = JavaFXKeyboardAdapter.toUnifiedKey(keyEvent.getCode());
            KeyBinds.addBind(uKey, bindingGameKey, gamestate);
            this.setText(keyEvent.getCode().toString());
            this.setOnKeyPressed(null);
        });
    }
}
