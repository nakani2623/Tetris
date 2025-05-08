package tetris.graphics;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tetris.engine.Board;
import javafx.beans.binding.Bindings;
import tetris.engine.Mino;
import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;

/**
 * Manages In-Game contents, including a gameboard
 */
public class GameWindow {
    static final int MINO_SIZE = 20;
    Pane root;
    Board board;
    Rectangle[][] grid;
    Set<Rectangle> updatingSquares;

    public GameWindow(Board board) {
        this.board = board;
        grid = new Rectangle[(int)board.getWidth()][(int)board.getHeight()];
        root = new Pane();
        Pane boardGraphics = createBoardGraphics(board);
        root.getChildren().add(boardGraphics);
    }

    public void execute(Stage stage) {
        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
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
        GridPane boardGrid = createBoardGrid(board);
        boardGraphics.getChildren().addAll(boardBorder, boardGrid);

        return boardGraphics;
    }
    
    private Node createBoardBorder(Board board) {
        Rectangle boardBorder = new Rectangle(
            board.getWidth() * MINO_SIZE, 
            board.getHeight() * MINO_SIZE
        );
        boardBorder.setFill(Color.TRANSPARENT);
        boardBorder.setStroke(Color.BLACK);
        centerRectangle(boardBorder, root);

        return boardBorder;
    }
    /**
     * creates graphics for grid of squares
     */
    private GridPane createBoardGrid(Board board) {
        // TODO: implementation incomplete

        // init a grid with Rectangles (Width * Height)
        GridPane gridPane = new GridPane();

        // grid border style
        gridPane.setStyle("-fx-border-color: black; -fx-border-width: 3px; -fx-background-color: white;");

        for (int i = 0; i < board.getWidth(); i++)
            for (int j = 0; j < board.getHeight(); j++) {
                Rectangle square = new Rectangle(MINO_SIZE, MINO_SIZE, Color.LIGHTGRAY);
                square.setStroke(Color.BLACK);
                gridPane.add(square, i, j);
                grid[i][j] = square;
            }
        centerGridPane(gridPane, root);
        return gridPane;
    }

//    public void drawT(Point2D centre) {
//        grid[0][0].setFill(Color.PURPLE);
//        grid[0][1].setFill(Color.PURPLE);
//        grid[0][2].setFill(Color.PURPLE);
//        grid[1][1].setFill(Color.PURPLE);
//    }
//
//    public void drawI() {
//        grid[0][0].setFill(Color.AQUA);
//        grid[0][1].setFill(Color.AQUA);
//        grid[0][2].setFill(Color.AQUA);
//        grid[0][3].setFill(Color.AQUA);
//    }

    public void drawCurrent() {
        Color c = switch (board.currentTetromino.type) {
            case O -> Color.YELLOW;
            case I -> Color.AQUA;
            case T -> Color.PURPLE;
            case S -> Color.GREEN;
            case Z -> Color.RED;
            case J -> Color.BLUE;
            case L -> Color.ORANGE;
            default -> Color.GREY;
        };

        int x = (int)board.currentTetromino.centre.getX();
        int y = (int)board.currentTetromino.centre.getY();
        for (Mino m : board.currentTetromino.children) {
            grid
                    [(int)m.getPosition().getX()]
                    [(int)m.getPosition().getY()]
                        .setFill(c);
        }
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

    /**
     * centering GridPane
     * @param gridPane
     * @param root
     */
    private void centerGridPane(GridPane gridPane, Pane root) {
        gridPane.layoutXProperty().bind(
            Bindings.subtract(root.widthProperty().divide(2), gridPane.widthProperty().divide(2))
        );
        gridPane.layoutYProperty().bind(
            Bindings.subtract(root.heightProperty().divide(2), gridPane.heightProperty().divide(2))
        );
    }
    

}
