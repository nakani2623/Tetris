package tetris.graphics.configPage;

import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import tetris.controller.input.JavaFXKeyboardAdapter;
import tetris.controller.input.KeyBinds;
import tetris.engine.observers.Observable;
import tetris.engine.observers.Observer;
import tetris.engine.type.GameState;
import tetris.engine.type.UnifiedKey;
import tetris.engine.type.Input.GameKey;

public class KeyBindButton extends Button implements Observable {
    GameKey bindingGameKey; 
    GameState gamestate;
    KeyBinds keyBinds;
    private boolean binding;

    Observer observer;

    public KeyBindButton(GameKey bindingGameKey, GameState gamestate, KeyBinds keyBinds) {
        this.bindingGameKey = bindingGameKey;
        this.gamestate = gamestate;
        this.keyBinds = keyBinds;
        
        this.setOnAction(event -> {
            this.setText("Press a key ...");
            startBind();
        });

        this.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (!binding) {
                return;
            }

            keyEvent.consume();
            UnifiedKey uKey = JavaFXKeyboardAdapter.toUnifiedKey(keyEvent.getCode());
            keyBinds.addBind(uKey, bindingGameKey, gamestate);
            notifyObservers();
            binding = false;
        });

        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == false) {
                binding = false;
                updateButtonText();
            }
        });

        updateButtonText();
        this.setMinWidth(100);
    }

    public void startBind() {
        binding = true;
    }

    public void updateButtonText() {
        this.setText(keyBinds.getUKeysFor(bindingGameKey, gamestate).toString());
    }

    @Override
    public void addObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observer = null;
    }

    @Override
    public void notifyObservers() {
        observer.update();
    }

}
