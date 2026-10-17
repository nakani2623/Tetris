package tetris.controller.dispatch;

import javafx.application.Platform;

public class JavaFXMainThreadDispatcher implements MainThreadDispatcher {
    public void dispatch(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
