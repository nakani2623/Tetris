 package tetris.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.geometry.Dimension2D;

public class Board {
    private Dimension2D size;
    public List<Tetromino> allTetrominos;
    public List<Tetromino> upcomingTetrominos;
    public Tetromino currentTetromino;
    private Mino[][] allMinos;



    
    /**
     * Constructs game bord with specific dimension
     * @param width
     * @param height
     */
    public Board(double width, double height) {
        this();
        this.size = new Dimension2D(width, height);
    }

    /**
     * Constructs default game board 10x20 dimension
     */
    public Board() {
        this.size = new Dimension2D(10, 20);
        allTetrominos = new ArrayList<Tetromino>();
        upcomingTetrominos = new LinkedList<>();
    }

    public void populateUpcoming(List<Tetromino> newTetrominos) {
        //TODO: Exceptional whenever upcoming is less than 7
        upcomingTetrominos.addAll(newTetrominos);
    }

    public double getWidth() {
        return size.getWidth();
    }

    public double getHeight() {
        return size.getHeight();
    }
}
