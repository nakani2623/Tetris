package tetris.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

class TetrominoTest {
    @Test
    void moveLeftMovesAllMinosOneUnitLeft() {
        //Arrange
        Tetromino t = new Tetromino(new Point(3.0, 14), TetrominoType.J);
        List<Double> originalX = new ArrayList<Double>();
        for (Mino m : t.children) {
            Double x = m.getPosition().getX();
            originalX.add(x);
        }

        //Act
        t.moveLeft();

        //Assert
        for (int i = 0; i < originalX.size(); i++) {
            Mino m = t.children.get(i);
            Double x = m.getPosition().getX();
            assertEquals(x, originalX.get(i) - 1);
        }
    };

    @Test
    void moveRightMovesAllMinosOneUnitRight() {
        //Arrange
        Tetromino t = new Tetromino(new Point(3.0, 14), TetrominoType.J);
        List<Double> originalX = new ArrayList<Double>();
        for (Mino m : t.children) {
            Double x = m.getPosition().getX();
            originalX.add(x);
        }

        //Act
        t.moveRight();

        //Assert
        for (int i = 0; i < originalX.size(); i++) {
            Mino m = t.children.get(i);
            Double x = m.getPosition().getX();
            assertEquals(x, originalX.get(i) + 1);
        }
    };

    @Test
    void moveLeftMovesCentreOneUnitLeft() {
        //Arrange
        Tetromino t = new Tetromino(new Point(3.0, 14), TetrominoType.J);
        double originalCentreX = t.centre.getX();
        double originalCentreY = t.centre.getY();

        //Act
        t.moveLeft();

        //Assert
        assertEquals(originalCentreX - 1, t.centre.getX());
        assertEquals(originalCentreY, t.centre.getY());
    };

    @Test
    void moveRightMovesCentreOneUnitRight() {
        //Arrange
        Tetromino t = new Tetromino(new Point(3.0, 14), TetrominoType.J);
        double originalCentreX = t.centre.getX();
        double originalCentreY = t.centre.getY();

        //Act
        t.moveRight();

        //Assert
        assertEquals(originalCentreX + 1, t.centre.getX());
        assertEquals(originalCentreY, t.centre.getY());
    };

    @Test
    void rotateClockwiseTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(2, 18), TetrominoType.Z);

        // Act
        t.rotateClockwise();

        // Assert
        assertPoint(2, 18, t.children.get(0).getPosition());
        assertPoint(2, 17, t.children.get(1).getPosition());
        assertPoint(3, 18, t.children.get(2).getPosition());
        assertPoint(3, 19, t.children.get(3).getPosition());
    }

    @Test
    void rotateClockwiseSRSPlusTestO() {
        // Arrange
        Tetromino t = new Tetromino(new Point(1.5, 17.5), TetrominoType.O);

        // Act
        t.rotateClockwise();

        // Assert
        assertPoint(2, 18, t.children.get(0).getPosition());
        assertPoint(2, 17, t.children.get(1).getPosition());
        assertPoint(1, 18, t.children.get(2).getPosition());
        assertPoint(1, 17, t.children.get(3).getPosition());
    }

    @Test
    void rotateClockwiseSRSPlusTestI() {
        // Arrange
        Tetromino t = new Tetromino(new Point(2.5, 17.5), TetrominoType.I);

        // Act
        t.rotateClockwise();

        // Assert
        assertPoint(3, 19, t.children.get(0).getPosition());
        assertPoint(3, 18, t.children.get(1).getPosition());
        assertPoint(3, 17, t.children.get(2).getPosition());
        assertPoint(3, 16, t.children.get(3).getPosition());
    }

    @Test
    void rotateCounterClockwiseTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(6, 18), TetrominoType.J);

        // Act
        t.rotateCounterClockwise();

        // Assert
        assertPoint(6, 18, t.children.get(0).getPosition());
        assertPoint(6, 17, t.children.get(1).getPosition());
        assertPoint(6, 19, t.children.get(2).getPosition());
        assertPoint(5, 17, t.children.get(3).getPosition());
    }

    @Test
    void rotateR180Test() {
        // Arrange
        Tetromino t = new Tetromino(new Point(5, 15), TetrominoType.T);

        // Act
        t.rotate180();

        // Assert
        assertPoint(5, 14, t.children.get(0).getPosition());
        assertPoint(6, 15, t.children.get(1).getPosition());
        assertPoint(5, 15, t.children.get(2).getPosition());
        assertPoint(4, 15, t.children.get(3).getPosition());
    }

    @Test
    void collisionWithTetrominoTrueTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(5, 15), TetrominoType.T);
        Tetromino z = new Tetromino(new Point(7, 15), TetrominoType.Z);
        z.rotate180();

        // Act
        boolean actual = t.collidesWith(z);

        // Assert
        assertEquals(true, actual);
    }

    @Test
    void collisionWithTetrominoFalseTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(5, 15), TetrominoType.T);
        Tetromino l = new Tetromino(new Point(6, 14), TetrominoType.L);

        // Act
        boolean actual = t.collidesWith(l);

        // Assert
        assertEquals(false, actual);
    }



    private void assertPoint(double expectedX, double expectedY, Point actualPoint) {
        assertEquals(expectedX, actualPoint.getX());
        assertEquals(expectedY, actualPoint.getY());
    }
}
