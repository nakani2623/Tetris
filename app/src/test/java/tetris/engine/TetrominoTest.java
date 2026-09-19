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
        Tetromino t = new Tetromino(new Point(3.0, 5.0), TetrominoType.J);
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
        Tetromino t = new Tetromino(new Point(3.0, 5.0), TetrominoType.J);
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
        Tetromino t = new Tetromino(new Point(3.0, 5.0), TetrominoType.J);
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
        Tetromino t = new Tetromino(new Point(3.0, 5.0), TetrominoType.J);
        double originalCentreX = t.centre.getX();
        double originalCentreY = t.centre.getY();

        //Act
        t.moveRight();

        //Assert
        assertEquals(originalCentreX + 1, t.centre.getX());
        assertEquals(originalCentreY, t.centre.getY());
    };
}
