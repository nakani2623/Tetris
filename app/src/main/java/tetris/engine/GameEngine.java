package tetris.engine;

public class GameEngine {
    private Board board;

    public GameEngine() {
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }
}
