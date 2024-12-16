package tetris.engine;

import javafx.geometry.Dimension2D;

public class Board {
    private Dimension2D size;

    public Board(double width, double height) {
        this.size = new Dimension2D(width, height);
    }

    public double getWidth() {
        return size.getWidth();
    }

    public double getHeight() {
        return size.getHeight();
    }
}
