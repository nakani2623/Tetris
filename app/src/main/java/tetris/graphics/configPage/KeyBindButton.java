package tetris.graphics.configPage;

import javafx.scene.control.Button;
import tetris.controller.input.JavaFXKeyboardAdapter;
import tetris.controller.input.KeyBinds;
import tetris.engine.type.GameState;
import tetris.engine.type.UnifiedKey;
import tetris.engine.type.Input.GameKey;

public class KeyBindButton extends Button {
    GameKey bindingGameKey; 
    GameState gamestate;
    KeyBinds keyBinds;

    public KeyBindButton(GameKey bindingGameKey, GameState gamestate, KeyBinds keyBinds) {
        this.bindingGameKey = bindingGameKey;
        this.gamestate = gamestate;
        this.keyBinds = keyBinds;
        
        this.setOnAction(event -> {
            this.setText("Press a key ...");
            startBind();
        });

        updateButtonText();
        this.setMinWidth(100);
    }

    public void startBind() {
        this.setOnKeyPressed(keyEvent -> {
            UnifiedKey uKey = JavaFXKeyboardAdapter.toUnifiedKey(keyEvent.getCode());
            keyBinds.addBind(uKey, bindingGameKey, gamestate);
            updateButtonText();
            this.setOnKeyPressed(null);
        });
    }

    public void updateButtonText() {
        this.setText(keyBinds.getUKeysFor(bindingGameKey, gamestate).toString());
    }
}
