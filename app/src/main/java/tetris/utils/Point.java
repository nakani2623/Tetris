package tetris.utils;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int val) {
        x = val;
    }

    public void setY(int val) {
        y = val;
    }
}