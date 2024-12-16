package tetris.engine;

import java.util.List;

import javafx.geometry.Point2D;

/*
 * Polyomino that consists of 4 Minoes 
 */
public class Tetromino {
    final int order = 4;
    Point2D centre;
    List<Mino> components;
    Mino[] children;

    public Tetromino() {
        children = new Mino[order];
    }
}
