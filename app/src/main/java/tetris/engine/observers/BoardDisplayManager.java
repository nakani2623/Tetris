package tetris.engine.observers;

import tetris.engine.Board;
import tetris.graphics.gameActive.BoardPane;

public class BoardDisplayManager implements Observer {
    private Board board;
    private BoardPane boardPane;

    public BoardDisplayManager(Board board, BoardPane boardPane) {
        this.board = board;
        this.boardPane = boardPane;
        board.addObserver(this);
    }

    @Override
    public void update() {
        boardPane.render(
            board.getAllTetrominos(),
            board.getShadowTetromino(),
            (int)board.getWidth(),
            (int)board.getHeight());
    }
}

