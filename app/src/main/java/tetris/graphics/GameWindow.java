package tetris.graphics;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tetris.engine.Board;

/**
 * Manages In-Game contents, including a gameboard
 */
public class GameWindow {
    Pane root;
    Board board;

    public GameWindow(Board board) {
        root = new Pane();
        Pane boardGraphics = createBoardGraphics(board);
        root.getChildren().add(boardGraphics);
    }

    public Pane getRoot() {
        return root;
    }

    /**
     * creates graphics for game board, outter most border
     * @param board
     * @return
     */
    private Pane createBoardGraphics(Board board) {
        Pane boardGraphics = new Pane();

        Node boardBorder = createBoardBorder(board);
        Node boardGrid = createBoardGrid(board);
        boardGraphics.getChildren().addAll(boardBorder, boardGrid);

        return boardGraphics;
    }
    
    private Node createBoardBorder(Board board) {
        Rectangle boardBorder = new Rectangle(
            board.getWidth(), 
            board.getHeight()
        );
        boardBorder.setFill(Color.TRANSPARENT);
        boardBorder.setStroke(Color.BLACK);

        return boardBorder;
    }
    /**
     * creates graphics for game board, grid 
     */
    private Node createBoardGrid(Board board) {
        // TODO: implementation incomplete
        return new Rectangle();
    }
}
