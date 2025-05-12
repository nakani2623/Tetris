 package tetris.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class Board {
    private int width;
    private int height;
    public List<Tetromino> allTetrominos;
    public List<Tetromino> upcomingTetrominos;
    public Tetromino currentTetromino;
    private Mino[][] allMinos;



    
    /**
     * Constructs game bord with specific dimension
     * @param width
     * @param height
     */
    public Board(int width, int height) {
        this.height = height;
        this.width = width;

        allTetrominos = new ArrayList<Tetromino>();
        upcomingTetrominos = new LinkedList<>();
    }

    /**
     * Constructs default game board 10x20 dimension
     */
    public Board() {
        this(10, 20);
    }

    public void populateUpcoming(List<Tetromino> newTetrominos) {
        //TODO: Exceptional whenever upcoming is less than 7
        upcomingTetrominos.addAll(newTetrominos);
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Point2D centreTopPoint() {
        return new Point2D((this.width/2)-1, 1);
    }
}
