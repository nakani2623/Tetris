package tetris.graphics;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tetris.engine.Board;

import javafx.beans.binding.Bindings;

/**
 * Manages In-Game contents, including a gameboard
 */
public class GameWindow {
    static final int minoLength = 20;
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
            board.getWidth() * minoLength, 
            board.getHeight() * minoLength
        );
        boardBorder.setFill(Color.TRANSPARENT);
        boardBorder.setStroke(Color.BLACK);
        centerRectangle(boardBorder, root);

        return boardBorder;
    }
    /**
     * creates graphics for game board, grid 
     */
    private Node createBoardGrid(Board board) {
        // TODO: implementation incomplete
        return new Rectangle();
    }

    /**
     *  Bind the rectangle x and y properties to center it within the root
     * Copilot implementation
     * @param rectangle
     * @param root
     */
    private void centerRectangle(Rectangle rectangle, Pane root) {
        rectangle.xProperty().bind(Bindings.subtract(root.widthProperty().divide(2), rectangle.widthProperty().divide(2)));
        rectangle.yProperty().bind(Bindings.subtract(root.heightProperty().divide(2), rectangle.heightProperty().divide(2)));
    }

}
