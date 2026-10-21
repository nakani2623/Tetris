package tetris.engine;

import java.util.ArrayList;
import java.util.List;
import tetris.utils.Point;


public class Piece {
    public List<Mino> children = new ArrayList<>();

    public Piece() {} // Tetromino need this for java's implicity call
    public Piece(Mino mino) {
        children.add(mino);
    }

    public void moveLeft() {
        for (Mino m : children) {
            double x = m.getPosition().getX();
            m.getPosition().setX(x-1);
        }
    }

    public void moveRight() {
        for (Mino m : children) {
            double x = m.getPosition().getX();
            m.getPosition().setX(x+1);
        }
    }

    public void moveDown() {
        for (Mino m : children) {
            double y = m.getPosition().getY();
            m.getPosition().setY(y + 1);
        }
    }

    public void moveUp() {
        for (Mino m : children) {
            double y = m.getPosition().getY();
            m.getPosition().setY(y - 1);
        }
    }

    public boolean collidesWith(Piece t) {
        for (Mino thisM : children) {
            for (Mino tM : t.children) {
                if (thisM.getPosition().equals(tM.getPosition())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void rotateClockwise(Point centre) {
        for (Mino mino : children) {
            Point relative = new Point(
                mino.getPosition().getX() - centre.getX(),
                mino.getPosition().getY() - centre.getY()
            );
            mino.setPosition(
                Point.combine(
                    centre, 
                    new Point(-relative.getY(), relative.getX())
                )
            );
        }
    }
}
