package tetris.graphics;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tetris.engine.GameEngine;

public class BoardPane extends GridPane {
    private final int MINO_SIZE = 20;
    private GameEngine engine;

    Rectangle[][] grid; // extra references to each rectangle

    public BoardPane(GameEngine engine) { 
        this.engine = engine;
        grid = new Rectangle[(int)engine.getWidth()][(int)engine.getHeight()];

        for (int i = 0; i < engine.getWidth(); i++) {
            for (int j = 0; j < engine.getHeight(); j++) {
                Rectangle square = new Rectangle(MINO_SIZE, MINO_SIZE, Color.WHITE);
                square.setStroke(Color.LIGHTGRAY);
                this.add(square, i, j);
                grid[i][j] = square;    
            }
        }
    }

    public void reset() {
        for (int i = 0; i < engine.getWidth(); i++) {
            for (int j = 0; j < engine.getHeight(); j++) {
                Rectangle r = grid[i][j];
                r.setFill(Color.WHITE);    
            }
        }
    }

    public void fill(int x, int y, Color color) {
        grid[x][y].setFill(color);
    }
}
