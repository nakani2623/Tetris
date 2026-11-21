package tetris.graphics.gameActive;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import tetris.graphics.VisualConfig;
import tetris.engine.type.TetrominoType;

public class HoldPane extends VBox {
    private final PreviewPane preview = new PreviewPane();

    public HoldPane() {
        Label title = new Label("HOLD");

        setBackground(new Background(new BackgroundFill(
                VisualConfig.PANEL_BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY)));
        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);
        setStyle("-fx-border-color: gray;");

        this.getChildren().addAll(title, preview);
    }

    public void showPiece(TetrominoType type) {
        preview.showPiece(type);
    }
}
