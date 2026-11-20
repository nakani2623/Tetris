package tetris.graphics;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tetris.controller.input.JavaFXKeyboardAdapter;
import tetris.controller.input.KeyBinds;
import tetris.engine.type.GameState;
import tetris.engine.type.Input.GameKey;

final class MenuOptionBar extends HBox {
    MenuOptionBar(KeyBinds bindings, Runnable back, Button... buttons) {
        super(buttons);
        List<Button> options = List.of(buttons);
        getStyleClass().add("menu-bar");
        for (Button button : options) {
            button.setOnMouseEntered(event -> button.requestFocus());
            button.setOnMouseMoved(event -> button.requestFocus());
        }
        sceneProperty().addListener((observable, previous, scene) -> {
            if (scene != null) {
                options.get((options.size() - 1) / 2).requestFocus();
            }
        });
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            int index = options.indexOf(getScene().getFocusOwner());
            if (index < 0) return;
            GameKey action = bindings.map(JavaFXKeyboardAdapter.toUnifiedKey(event.getCode()), GameState.active);
            if (action == GameKey.ROTATE_CW || action == GameKey.HARDDROP) {
                event.consume();
                options.get(index).fire();
            } else if (action == GameKey.ROTATE_CCW) {
                event.consume();
                if (back != null) back.run();
            } else {
                int step;
                if (action == GameKey.MOVE_LEFT || (action == null && event.getCode() == KeyCode.LEFT)) {
                    step = -1;
                } else if (action == GameKey.MOVE_RIGHT || (action == null && event.getCode() == KeyCode.RIGHT)) {
                    step = 1;
                } else {
                    return;
                }
                event.consume();
                options.get(Math.floorMod(index + step, options.size())).requestFocus();
            }
        });
    }
}
