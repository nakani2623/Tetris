package tetris.utils;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * duplicates a Point
     * @param val
     */
    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double val) {
        x = val;
    }

    public void setY(double val) {
        y = val;
    }
}