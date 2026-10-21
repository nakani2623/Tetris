package tetris.graphics.gameActive;

import java.util.List;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import tetris.engine.Mino;
import tetris.engine.Tetromino;
import tetris.graphics.VisualConfig;

public class BoardPane extends GridPane {

    public BoardPane() { 
        this.setStyle("-fx-border-color: gray;");
    }

    public void render(List<Tetromino> tetrominos, Tetromino shadow, int x, int y) {
        // clear board
        this.getChildren().clear();
        renderEmpty(x, y);

        // render shadow piece (first), so behind current if overlaped
        if (shadow != null)
            renderShadow(shadow);
        // render all placed pieces
        for (Tetromino t : tetrominos) {
            renderPiece(t);
        }

    }

    private void renderEmpty(int x, int y) {
        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++)
                renderMino(i, j, Color.WHITE);
    }

    private void renderShadow(Tetromino tetromino) {
        Color baseColor;
        if (VisualConfig.ColoredShadow) {
            baseColor = TetrominoColor.forType(tetromino.type);
        } else {
            baseColor = Color.GRAY;
        }
        Color blendedColor = baseColor.deriveColor(0, 1, 1, VisualConfig.ShadowOpacity);
        renderPiece(tetromino, blendedColor);
    }

    private void renderPiece(Tetromino tetromino) {
        renderPiece(tetromino, TetrominoColor.forType(tetromino.type));
    }

    private void renderPiece(Tetromino tetromino, Color color) {
        for (Mino m : tetromino.children) {
            double x = m.getPosition().getX();
            double y = m.getPosition().getY();
            renderMino((int)x, (int)y, color);
        }
    }

    private void renderMino(int x, int y, Color color) {
        int MINO_SIZE = 20;
        Rectangle minoRectangle = new Rectangle(MINO_SIZE, MINO_SIZE);
        minoRectangle.setX(x * MINO_SIZE);
        minoRectangle.setY(y * MINO_SIZE);


        minoRectangle.setFill(color);
        minoRectangle.setStroke(Color.LIGHTGRAY);
        minoRectangle.setStrokeType(StrokeType.INSIDE);

        this.add(minoRectangle, x, y);
    }
}
