package tetris.graphics;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tetris.engine.type.TetrominoType;

public class NextQueuePane extends VBox {
    private PreviewPane[] nextQueue;

    public NextQueuePane() {
        Label title = new Label("NEXT");
        this.getChildren().add(title);

        nextQueue = new PreviewPane[5];
        for (int i = 0; i < 5; i++) {
            nextQueue[i] = new PreviewPane();
            this.getChildren().add(nextQueue[i]);
        }

        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);
        setStyle("-fx-border-color: gray;");
    }

    public void showNext() {
        nextQueue[0].showPiece(TetrominoType.T);
        nextQueue[1].showPiece(TetrominoType.O);
        nextQueue[2].showPiece(TetrominoType.I);
        nextQueue[3].showPiece(TetrominoType.S);
        nextQueue[4].showPiece(TetrominoType.Z);

    }

}
