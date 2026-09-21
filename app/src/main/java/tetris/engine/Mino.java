package tetris.engine;

import tetris.utils.Point;

/*
 * square component of Polyominoes
 */
public class Mino {
    private Point position;

    public Mino(int x, int y) {
        this.position = new Point(x, y);
    }

    /**
     * duplicates a Mino (deep copy)
     * @param position
     */
    public Mino(Mino m) {
        this.position = new Point(m.position);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }
}
