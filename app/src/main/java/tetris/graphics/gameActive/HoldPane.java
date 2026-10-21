package tetris.graphics.gameActive;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tetris.engine.type.TetrominoType;

public class HoldPane extends VBox {
    private final PreviewPane preview = new PreviewPane();

    public HoldPane() {
        Label title = new Label("HOLD");

        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);
        setStyle("-fx-border-color: gray;");

        this.getChildren().addAll(title, preview);
    }

    public void showPiece(TetrominoType type) {
        preview.showPiece(type);
    }
}
