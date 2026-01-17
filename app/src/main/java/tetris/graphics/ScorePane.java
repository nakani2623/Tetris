package tetris.graphics;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ScorePane extends VBox {
    private Label time;
    private Label score;
    private Label pps;

    // private String longestSample = "SCORE: 999999";

    public ScorePane() {
        time = new Label("Time: 0");
        score = new Label("Score: 0");
        pps = new Label("PPS: 0");

        setPadding(new Insets(8));
        setSpacing(2);
        setAlignment(Pos.BOTTOM_LEFT);

        setupLabel(time);
        setupLabel(score);
        setupLabel(pps);

        // Make each label take equal vertical space so we can scale font to height
        VBox.setVgrow(time, Priority.ALWAYS);
        VBox.setVgrow(score, Priority.ALWAYS);
        VBox.setVgrow(pps, Priority.ALWAYS);

        getChildren().addAll(time, score, pps);
    }

    public void updateTime(double time) {
        this.time.setText("Time: " + time);
    }
    
    public void updateScore(double score) {
        this.score.setText("Score: " + score);
    }

    public void updatePPS(double pps) {
        this.pps.setText("PPS: " + pps);
    }

    private void setupLabel(Label label) {
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(Double.MAX_VALUE);   // allow it to fill panel width
        label.setWrapText(false);              // keep one line
    }
}


