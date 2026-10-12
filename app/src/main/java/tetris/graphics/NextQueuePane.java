package tetris.graphics;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tetris.engine.type.TetrominoType;

public class NextQueuePane extends VBox {
    private PreviewPane[] previews;

    public NextQueuePane() {
        Label title = new Label("NEXT");
        this.getChildren().add(title);

        previews = new PreviewPane[5];
        for (int i = 0; i < previews.length; i++) {
            previews[i] = new PreviewPane();
            this.getChildren().add(previews[i]);
        }

        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);
        setStyle("-fx-border-color: gray;");
    }

    public void showNext() {
        previews[0].showPiece(TetrominoType.T);
        previews[1].showPiece(TetrominoType.O);
        previews[2].showPiece(TetrominoType.I);
        previews[3].showPiece(TetrominoType.S);
        previews[4].showPiece(TetrominoType.Z);

    }

}
