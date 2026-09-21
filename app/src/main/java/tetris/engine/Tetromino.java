package tetris.engine;

import java.util.ArrayList;
import java.util.List;

import tetris.utils.Point;

import tetris.engine.type.TetrominoType;


/**
 * Polyomino that consists of 4 Minoes 
 */
public class Tetromino {
    final int order = 4;
    public Point centre;
    public List<Mino> children;
    public TetrominoType type;
//    Mino[] children;

    public Tetromino(Point centre, TetrominoType type) {
        this(type);
        this.centre = centre;
        children = new ArrayList<>();

        double x = centre.getX();
        double y = centre.getY();
        if (type == TetrominoType.I) {
            x += 0.5;
            y += 0.5;
        } else if (type == TetrominoType.O) {
            x += 0.5;
            y -= 0.5;
        }

        if (type == TetrominoType.T) {
           children.add(new Mino((int)x, (int)y-1));
           children.add(new Mino((int)x-1, (int)y));
           children.add(new Mino((int)x, (int)y));
           children.add(new Mino((int)x+1, (int)y));
        } else if (type == TetrominoType.I) {
            children.add(new Mino((int)(x-1.5), (int)(y-0.5)));
            children.add(new Mino((int)(x-0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x+0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x+1.5), (int)(y-0.5)));
        } else if (type == TetrominoType.O) {
            children.add(new Mino((int)(x-0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x+0.5), (int)(y-0.5)));
            children.add(new Mino((int)(x-0.5), (int)(y+0.5)));
            children.add(new Mino((int)(x+0.5), (int)(y+0.5)));
        } else if (type == TetrominoType.L) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x-1), (int)y));
            children.add(new Mino((int)(x+1), (int)y));
            children.add(new Mino((int)(x+1), (int)(y-1)));
        } else if (type == TetrominoType.J) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x-1), (int)y));
            children.add(new Mino((int)(x+1), (int)y));
            children.add(new Mino((int)(x-1), (int)(y-1)));
        } else if (type == TetrominoType.S) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x-1), (int)y));
            children.add(new Mino((int)x, (int)(y-1)));
            children.add(new Mino((int)(x+1), (int)(y-1)));
        } else if (type == TetrominoType.Z) {
            children.add(new Mino((int)x, (int)y));
            children.add(new Mino((int)(x+1), (int)y));
            children.add(new Mino((int)x, (int)(y-1)));
            children.add(new Mino((int)(x-1), (int)(y-1)));
        }
    }

    public Tetromino(TetrominoType type) {
        this.type = type;
    }

    /**
     * duplicates a Tetromino (deep copy)
     * @param t
     */
    public Tetromino(Tetromino t) {
        this.centre = new Point(t.centre);
        this.children = new ArrayList<Mino>();
        for (Mino m : t.children) {
            this.children.add(new Mino(m));
        }

        this.type = t.type;
    }

    public void moveLeft() {
        double originalX = this.centre.getX();
        this.centre.setX(originalX - 1);

        for (Mino m : children) {
            double x = m.getPosition().getX();
            m.getPosition().setX(x-1);
        }
    }

    public void moveRight() {
        double originalX = this.centre.getX();
        this.centre.setX(originalX + 1);

        for (Mino m : children) {
            double x = m.getPosition().getX();
            m.getPosition().setX(x+1);
        }
    }

    /**
    * rotates the tetromino clockwise / right
    */
    public void rotateCounterClockwise() {
        for (Mino m : children) {
            Point p = m.getPosition();
            double distanceToCentreX = p.getX() - centre.getX();
            double distanceToCentreY = p.getY() - centre.getY();

            double newX = distanceToCentreY + centre.getX();
            double newY = -distanceToCentreX + centre.getY();
            p.setX(newX);
            p.setY(newY);

        }
    }

    /**
    * rotates the tetromino counterclockwise / left
    */
    public void rotateClockwise() {
        for (Mino m : children) {
            Point p = m.getPosition();
            double distanceToCentreX = p.getX() - centre.getX();
            double distanceToCentreY = p.getY() - centre.getY();

            double newX = -distanceToCentreY + centre.getX();
            double newY = distanceToCentreX + centre.getY();
            p.setX(newX);
            p.setY(newY);
        }
    }

    /**
    * rotates the tetromino 180 degree
    */
    public void rotate180() {
        for (Mino m : children) {
            Point p = m.getPosition();
            double distanceToCentreX = p.getX() - centre.getX();
            double distanceToCentreY = p.getY() - centre.getY();

            double newX = -distanceToCentreX + centre.getX();
            double newY = -distanceToCentreY + centre.getY();
            p.setX(newX);
            p.setY(newY);
        }
    }

    public boolean collidesWith(Tetromino t) {
        for (Mino thisM : children) {
            for (Mino tM : t.children) {
                if (thisM.equals(tM)) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
