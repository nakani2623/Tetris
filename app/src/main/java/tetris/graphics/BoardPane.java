package tetris.graphics;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tetris.engine.Board;
import javafx.scene.shape.Rectangle;

public class BoardPane extends GridPane {
    private final int MINO_SIZE = 20;
    private Board board;

    Rectangle[][] grid; // extra references to each rectangle

    public BoardPane(Board board) { 
        this.board = board;
        grid = new Rectangle[(int)board.getWidth()][(int)board.getHeight()];

        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                Rectangle square = new Rectangle(MINO_SIZE, MINO_SIZE, Color.WHITE);
                square.setStroke(Color.LIGHTGRAY);
                this.add(square, i, j);
                grid[i][j] = square;    
            }
        }
    }
}
