package tetris.graphics.gameActive;

import java.util.List;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import tetris.engine.Mino;
import tetris.engine.Piece;
import tetris.engine.Tetromino;
import tetris.graphics.VisualConfig;

public class BoardPane extends GridPane {

    public BoardPane() { 
        this.setStyle("-fx-border-color: gray;");
    }

    public void render(List<Piece> pieces, Tetromino shadow, int x, int y) {
        int rowOffset = getRowOffset(pieces, shadow);

        // clear board
        this.getChildren().clear();
        renderEmpty(x, y, rowOffset);

        // render shadow piece (first), so behind current if overlaped
        if (shadow != null)
            renderShadow(shadow, rowOffset);
        // render all placed pieces
        for (Piece t : pieces) {
            renderPiece(t, rowOffset);
        }

    }

    private int getRowOffset(List<Piece> tetrominos, Tetromino shadow) {
        int minRow = 0;
        if (shadow != null) {
            minRow = getMinRow(shadow, minRow);
        }
        for (Piece tetromino : tetrominos) {
            minRow = getMinRow(tetromino, minRow);
        }
        return -minRow;
    }

    private int getMinRow(Piece tetromino, int minRow) {
        for (Mino mino : tetromino.children) {
            minRow = Math.min(minRow, (int) mino.getPosition().getY());
        }
        return minRow;
    }

    private void renderEmpty(int x, int y, int rowOffset) {
        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++)
                renderMino(i, j + rowOffset, Color.WHITE);
    }

    private void renderShadow(Tetromino tetromino, int rowOffset) {
        Color baseColor;
        if (VisualConfig.ColoredShadow) {
            baseColor = TetrominoColor.forType(tetromino.type);
        } else {
            baseColor = Color.GRAY;
        }
        Color blendedColor = baseColor.deriveColor(0, 1, 1, VisualConfig.ShadowOpacity);
        renderPiece(tetromino, blendedColor, rowOffset);
    }

    private void renderPiece(Piece piece, int rowOffset) {
        if (piece instanceof Tetromino tetromino) {
            renderPiece(tetromino, TetrominoColor.forType(tetromino.type), rowOffset); 
        }
        else { // default: render gray
            renderPiece(piece, Color.GRAY, rowOffset);
        }
    }

    private void renderPiece(Piece tetromino, Color color, int rowOffset) {
        for (Mino m : tetromino.children) {
            double x = m.getPosition().getX();
            double y = m.getPosition().getY();
            renderMino((int)x, (int)y + rowOffset, color);
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
