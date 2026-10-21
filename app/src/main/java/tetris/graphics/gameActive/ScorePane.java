package tetris.graphics.gameActive;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ScorePane extends VBox {
    private final Label time = new Label("Time: 0");
    private final Label score = new Label("Score: 0");
    private final Label pps = new Label("PPS: 0");

    // private String longestSample = "SCORE: 999999";

    public ScorePane() {
        getChildren().addAll(time, score, pps);

        setPadding(new Insets(8));
        setSpacing(2);
        setAlignment(Pos.BOTTOM_LEFT);
        setMinWidth(90);
        setPrefWidth(90);
        setMaxWidth(90);

        setupLabel(time);
        setupLabel(score);
        setupLabel(pps);

        // Make each label take equal vertical space so we can scale font to height
        VBox.setVgrow(time, Priority.ALWAYS);
        VBox.setVgrow(score, Priority.ALWAYS);
        VBox.setVgrow(pps, Priority.ALWAYS);
    }

    public void updateTime(double time) {
        this.time.setText(
                String.format(Locale.ROOT, "Time: %.2f", time)
        );
    }

    public void updateScore(double score) {
        this.score.setText(
                String.format(Locale.ROOT, "Score: %.0f", score)
        );
    }

    public void updatePPS(double pps) {
        this.pps.setText(
                String.format(Locale.ROOT, "PPS: %.2f", pps)
        );
    }

    private void setupLabel(Label label) {
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(Double.MAX_VALUE);   // allow it to fill panel width
        label.setWrapText(false);              // keep one line
    }
}


