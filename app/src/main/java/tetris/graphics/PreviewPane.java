package tetris.graphics;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import tetris.engine.Mino;
import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class PreviewPane extends StackPane {
    public PreviewPane() {
        setMinSize(90, 60);
        setPrefSize(90, 60);
        setMaxSize(90, 60);
    }

    public void showPiece(TetrominoType type) {
        this.getChildren().clear();

        if (type == null) {
            return;
        }

        Tetromino piece = new Tetromino(new Point(4, 4), type);
        Group shape = new Group();
        Color color = TetrominoColor.forType(type);


        for (Mino mino : piece.children) {
            double x = mino.getPosition().getX();
            double y = mino.getPosition().getY();

            shape.getChildren().add(renderMino((int)x, (int)y, color));
        }

        this.getChildren().add(shape);
    }

    private Rectangle renderMino(int x, int y, Color color) {
        int MINO_SIZE = 20;
        Rectangle minoRectangle = new Rectangle(MINO_SIZE, MINO_SIZE);
        minoRectangle.setX(x * MINO_SIZE);
        minoRectangle.setY(y * MINO_SIZE);


        minoRectangle.setFill(color);
        minoRectangle.setStroke(Color.LIGHTGRAY);
        minoRectangle.setStrokeType(StrokeType.INSIDE);

        return minoRectangle;
    }
}
