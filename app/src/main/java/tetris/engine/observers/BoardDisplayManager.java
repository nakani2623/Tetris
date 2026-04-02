package tetris.engine.observers;

import javafx.scene.paint.Color;
import tetris.engine.Board;
import tetris.engine.Mino;
import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;
import tetris.graphics.BoardPane;

public class BoardDisplayManager implements Observer {
    private Observable observable = null;
    private BoardPane boardPane;

    public BoardDisplayManager(Observable observable, BoardPane boardPane) {
        this.observable = observable;
        this.boardPane = boardPane;
        observable.addObserver(this);
    }

    @Override
    public void update() {
        System.out.println("Board mngr notified");
        if (!(observable instanceof Board)) {
            return;
        }
        Board board = (Board) observable;
        // todo: render the board and tetrominos
        boardPane.reset();
        for (Tetromino t : board.allTetrominos) {
            for (Mino m : t.children) {
                int x = (int)m.getPosition().getX();
                int y = (int)m.getPosition().getY();
                
                Color fillColor = null;
                if (t.type == TetrominoType.T)
                    fillColor = Color.PURPLE;
                else if (t.type == TetrominoType.O) {
                    fillColor = Color.YELLOW;
                } else if (t.type == TetrominoType.I) {
                    fillColor = Color.CYAN;
                } else if (t.type == TetrominoType.S) {
                    fillColor = Color.GREEN;
                } else if (t.type == TetrominoType.Z) {
                    fillColor = Color.RED;
                } else if (t.type == TetrominoType.J) {
                    fillColor = Color.BLUE;
                } else if (t.type == TetrominoType.L) {
                    fillColor = Color.ORANGE;
                }

                boardPane.fill(x, y, fillColor);
            } 
        }


    }
}

