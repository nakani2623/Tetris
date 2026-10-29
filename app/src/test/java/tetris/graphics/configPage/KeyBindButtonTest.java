package tetris.graphics.configPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tetris.controller.input.JavaFXKeyboardAdapter;
import tetris.controller.input.KeyBinds;
import tetris.engine.type.GameState;
import tetris.engine.type.Input.GameKey;

class KeyBindButtonTest {
    @BeforeAll
    static void startJavaFx() throws InterruptedException {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(started::countDown);
        assertTrue(started.await(10, TimeUnit.SECONDS));
    }

    @AfterAll
    static void stopJavaFx() {
        Platform.exit();
    }

    @Test
    void capturesDownArrowWithoutAllowingDefaultButtonNavigation() throws Exception {
        FutureTask<Void> test = new FutureTask<>(() -> {
            KeyBinds keyBinds = new KeyBinds();
            keyBinds.binds.get(GameState.active).clear();

            KeyBindButton button = new KeyBindButton(GameKey.SOFTDROP, GameState.active, keyBinds);
            button.addObserver(() -> {});
            button.startBind();

            KeyEvent event = new KeyEvent(
                    button, button, KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN,
                    false, false, false, false);
            Event.fireEvent(button, event);

            assertEquals(
                    GameKey.SOFTDROP,
                    keyBinds.map(JavaFXKeyboardAdapter.toUnifiedKey(KeyCode.DOWN), GameState.active));
            assertTrue(event.isConsumed());
            return null;
        });

        Platform.runLater(test);
        test.get(10, TimeUnit.SECONDS);
    }
}
