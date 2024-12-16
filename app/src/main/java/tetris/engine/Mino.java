package tetris.engine;

import javafx.geometry.Point2D;

/*
 * square component of Polyominoes
 */
public class Mino {
    private Point2D position;

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }
}
