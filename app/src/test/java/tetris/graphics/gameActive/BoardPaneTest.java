package tetris.graphics.gameActive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import tetris.engine.Tetromino;
import tetris.engine.Mino;
import tetris.engine.Piece;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

class BoardPaneTest {
    @Test
    void rendersPiecesAbovePlayfieldUsingNonnegativeGridRows() {
        BoardPane boardPane = new BoardPane();
        Tetromino tetromino = new Tetromino(new Point(4, 19), TetrominoType.T);

        boardPane.render(List.of(tetromino), null, 10, 20);

        assertEquals(1, GridPane.getRowIndex(boardPane.getChildren().get(0)));
        assertEquals(0, GridPane.getRowIndex(boardPane.getChildren().get(200)));
        for (Node node : boardPane.getChildren()) {
            assertTrue(GridPane.getRowIndex(node) >= 0);
        }
    }
    @Test
    void mapsFloorAndTopToOriginalScreenRows() {
        BoardPane pane = new BoardPane();
        Piece piece = new Piece(List.of(new Mino(2, 0), new Mino(3, 19)));
        pane.render(List.of(piece), null, 10, 20);
        assertEquals(19, GridPane.getRowIndex(pane.getChildren().get(200)));
        assertEquals(0, GridPane.getRowIndex(pane.getChildren().get(201)));
    }

    @Test
    void previewKeepsOriginalScreenCoordinatesForEveryShape() {
        // Original screen-space coordinates for centre (4, 4), in child order.
        int[][][] expected = {
            {{4,3},{3,4},{4,4},{5,4}}, // T
            {{2,3},{3,3},{4,3},{5,3}}, // I
            {{3,3},{4,3},{3,4},{4,4}}, // O
            {{4,4},{3,4},{4,3},{5,3}}, // S
            {{4,4},{5,4},{4,3},{3,3}}, // Z
            {{4,4},{3,4},{5,4},{3,3}}, // J
            {{4,4},{3,4},{5,4},{5,3}}  // L
        };
        PreviewPane pane = new PreviewPane();
        for (TetrominoType type : TetrominoType.values()) {
            pane.showPiece(type);
            Group shape = (Group) pane.getChildren().get(0);
            for (int i = 0; i < 4; i++) {
                Rectangle mino = (Rectangle) shape.getChildren().get(i);
                assertEquals(expected[type.ordinal()][i][0] * 20, mino.getX());
                assertEquals(expected[type.ordinal()][i][1] * 20, mino.getY());
            }
        }
    }
}
