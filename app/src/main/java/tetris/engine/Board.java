 package tetris.engine;

import java.util.List;
import javafx.geometry.Dimension2D;

public class Board {
    private Dimension2D size;
    List<Mino> allMinos;

    /**
     * Constructs game bord with specific dimension
     * @param width
     * @param height
     */
    public Board(double width, double height) {
        this.size = new Dimension2D(width, height);
    }

    /**
     * Constructs default game board 10x20 dimension
     */
    public Board() {
        this.size = new Dimension2D(10, 20);
    }

    public double getWidth() {
        return size.getWidth();
    }

    public double getHeight() {
        return size.getHeight();
    }
}
