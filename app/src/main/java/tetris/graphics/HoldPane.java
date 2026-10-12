package tetris.graphics;


import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import tetris.engine.Mino;
import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class HoldPane extends VBox {
    private final StackPane preview = new StackPane();

    public HoldPane() {
        Label title = new Label("HOLD");
        preview.setMinSize(90, 60);
        preview.setPrefSize(90, 60);
        preview.setMaxSize(90, 60);

        this.setStyle("-fx-border-color: gray;");

        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);

        this.getChildren().addAll(title, preview);
    }

    public void showPiece(TetrominoType type) {
        preview.getChildren().clear();

        if (type == null) {
            return;
        }

        Tetromino piece = new Tetromino(new Point(4, 4), type);
        Group shape = new Group();

        for (Mino mino : piece.children) {
            double x = mino.getPosition().getX();
            double y = mino.getPosition().getY();

            shape.getChildren().add(renderMino((int)x, (int)y));
        }

        preview.getChildren().add(shape);
    }

    private Rectangle renderMino(int x, int y) {
        int MINO_SIZE = 20;
        Rectangle minoRectangle = new Rectangle(MINO_SIZE, MINO_SIZE);
        minoRectangle.setX(x * MINO_SIZE);
        minoRectangle.setY(y * MINO_SIZE);

        minoRectangle.setFill(Color.GRAY);
        minoRectangle.setStroke(Color.LIGHTGRAY);
        minoRectangle.setStrokeType(StrokeType.INSIDE);

        return minoRectangle;
    }
}
