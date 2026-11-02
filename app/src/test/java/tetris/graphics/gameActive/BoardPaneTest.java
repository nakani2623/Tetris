package tetris.graphics.gameActive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

class BoardPaneTest {
    @Test
    void rendersPiecesAbovePlayfieldUsingNonnegativeGridRows() {
        BoardPane boardPane = new BoardPane();
        Tetromino tetromino = new Tetromino(new Point(4, 0), TetrominoType.T);

        boardPane.render(List.of(tetromino), null, 10, 20);

        assertEquals(1, GridPane.getRowIndex(boardPane.getChildren().get(0)));
        assertEquals(0, GridPane.getRowIndex(boardPane.getChildren().get(200)));
        for (Node node : boardPane.getChildren()) {
            assertTrue(GridPane.getRowIndex(node) >= 0);
        }
    }
}
