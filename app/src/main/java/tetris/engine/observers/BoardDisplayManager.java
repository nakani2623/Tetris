package tetris.engine.observers;

import javafx.scene.paint.Color;
import tetris.engine.GameEngine;
import tetris.engine.Mino;
import tetris.engine.Tetromino;
import tetris.graphics.BoardPane;
import tetris.graphics.TetrominoColor;

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
        if (!(observable instanceof GameEngine)) {
            return;
        }
        GameEngine engine = (GameEngine) observable;
        // todo: render the board and tetrominos
        boardPane.reset();
        for (Tetromino t : engine.allTetrominos) {
            Color fillColor = TetrominoColor.forType(t.type);

            for (Mino m : t.children) {
                int x = (int)m.getPosition().getX();
                int y = (int)m.getPosition().getY();

                boardPane.fill(x, y, fillColor);
            } 
        }


    }
}

