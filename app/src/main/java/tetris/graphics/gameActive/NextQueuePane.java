package tetris.graphics.gameActive;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tetris.engine.type.TetrominoType;

public class NextQueuePane extends VBox {
    private final PreviewPane[] previews;

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

    public void showNext(List<TetrominoType> next) {
        // empty leftover display
        for (int i = 0; i < previews.length; i++) {
            previews[i].showPiece(null);
        }

        // show given next
        for (int i = 0; i < previews.length; i++) {
            if (i >= next.size()) {
                break;
            }

            previews[i].showPiece(next.get(i)); 
        }
    }

}
