package tetris.graphics.gameActive;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tetris.engine.score.scoreEvent.SpinClearEvent;
import tetris.engine.type.SpinState;

public class SpinClearPane extends VBox {
    private final Label spin = new Label("? - spin");
    private final Label mini = new Label("mini");
    private final Label clear = new Label("unknown");

    private PauseTransition hideTimer = new PauseTransition(
        Duration.seconds(2)
    );
    // private String longestSample = "SCORE: 999999";

    public SpinClearPane() {
        getChildren().addAll(spin, mini, clear);
        this.setVisible(false);
    }
    public void update(SpinClearEvent event) {
        // display spin info
        // System.out.printf("%s spin %s, clear: %d\n", event.tetrominoType, event.spinState, event.linesClear);        
        if (event.spinState != SpinState.NOT_SPIN)
            spin.setText(event.tetrominoType + "-spin");
        else 
            spin.setText("");

        String miniText;
        if (event.spinState == SpinState.MINI_SPIN) 
            miniText = "mini";
        else miniText = "";
        mini.setText(miniText);

        String clearText;
        switch (event.linesClear) {
            case 0: clearText = ""; break;
            case 1: clearText = "SINGLE"; break;
            case 2: clearText = "DOUBLE"; break;
            case 3: clearText = "TRIPLE"; break;
            case 4: clearText = "QUAD"; break;
            default: clearText = "unknown"; break;
        }
        clear.setText(clearText);

        this.setVisible(true);
        
        hideTimer.setOnFinished(e-> this.setVisible(false));
        hideTimer.playFromStart();
    }
}
