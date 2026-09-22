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

    /**
     * Z with centre (2,1) rotate clockwise:                  
     *                                    *---*
     *                                    |3,0|
     *    *---*---*                   *---*---*
     *    |1,0|2,0|        ->         |2,1|3,1|
     *    *---*---*---*               *---*---*
     *        |2,1|3,1|               |2,2|
     *        *---*---*               *---*
     * 
     * 2, 1 -> 2, 1
     * 3, 1 -> 2, 2
     * 2, 0 -> 3, 1
     * 1, 0 -> 3, 0
     */
    @Test
    void rotateClockwiseTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(2, 1), TetrominoType.Z);

        // Act
        t.rotateClockwise();

        // Assert
        assertPoint(2, 1, t.children.get(0).getPosition());
        assertPoint(2, 2, t.children.get(1).getPosition());
        assertPoint(3, 1, t.children.get(2).getPosition());
        assertPoint(3, 0, t.children.get(3).getPosition());
    }

    /**
     * O with centre (1,2), after alt (1.5, 1.5) rotate clockwise:                  
     * 
     *      *---*---*                   *---*---*
     *      |1,1|2,1|                   |1,1|2,1|
     *      *---*---*        ->         *---*---*
     *      |1,2|2,2|                   |1,2|2,2|
     *      *---*---*                   *---*---*
     * 
     * 1, 1 -> 2, 1
     * 2, 1 -> 2, 2
     * 1, 2 -> 1, 1
     * 2, 2 -> 1, 2
     */
    @Test
    void rotateClockwiseSRSPlusTestO() {
        // Arrange
        Tetromino t = new Tetromino(new Point(1, 2), TetrominoType.O);

        // Act
        t.rotateClockwise();

        // Assert
        assertPoint(2, 1, t.children.get(0).getPosition());
        assertPoint(2, 2, t.children.get(1).getPosition());
        assertPoint(1, 1, t.children.get(2).getPosition());
        assertPoint(1, 2, t.children.get(3).getPosition());
    }

        /**
     * O with centre (2,1), altered into (2.5, 1.5) rotate clockwise:                  
     * 
     *                                  *---*
     *                                  |3,0|
     *      *---*---*---*---*           *---*
     *      |1,1|2,1|3,1|4,1|    ->     |3,1|
     *      *---*---*---*---*           *---*
     *                                  |3,2|
     *                                  *---*
     *                                  |3,3|
     *                                  *---*
     * 
     * 1, 1 -> 3, 0
     * 2, 1 -> 3, 1
     * 3, 2 -> 3, 2
     * 4, 1 -> 3, 3
     */
    @Test
    void rotateClockwiseSRSPlusTestI() {
        // Arrange
        Tetromino t = new Tetromino(new Point(2, 1), TetrominoType.I);

        // Act
        t.rotateClockwise();

        // Assert
        assertPoint(3, 0, t.children.get(0).getPosition());
        assertPoint(3, 1, t.children.get(1).getPosition());
        assertPoint(3, 2, t.children.get(2).getPosition());
        assertPoint(3, 3, t.children.get(3).getPosition());
    }

    /**
     * J with centre (6, 1) rotate counter-clockwise:
     *                   
     *    *---*                         *---*
     *    |5,0|                         |6,0|
     *    *---*---*---*                 *---*
     *    |5,1|6,1|7,1|    ->           |6,1|
     *    *---*---*---*             *---*---*
     *                              |5,2|6,2|
     *                              *---*---*
     * 6, 1 -> 6, 1
     * 5, 1 -> 6, 2
     * 7, 1 -> 6, 0
     * 5, 0 -> 5, 2
     */
    @Test
    void rotateCounterClockwiseTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(6, 1), TetrominoType.J);

        // Act
        t.rotateCounterClockwise();

        // Assert
        assertPoint(6, 1, t.children.get(0).getPosition());
        assertPoint(6, 2, t.children.get(1).getPosition());
        assertPoint(6, 0, t.children.get(2).getPosition());
        assertPoint(5, 2, t.children.get(3).getPosition());
    }

    /**
     * T with centre (5, 4) rotate counter-clockwise:
     *                   
     *        *---*                    
     *        |5,3|                     
     *    *---*---*---*             *---*---*---*
     *    |4,4|5,4|6,4|    ->       |4,4|5,4|6,4|
     *    *---*---*---*             *---*---*---*
     *                                  |5,5|
     *                                  *---*
     * 5, 3 -> 5, 5
     * 4, 4 -> 6, 4
     * 5, 4 -> 5, 4
     * 6, 4 -> 4, 4
     */
    @Test
    void rotateR180Test() {
        // Arrange
        Tetromino t = new Tetromino(new Point(5, 4), TetrominoType.T);

        // Act
        t.rotate180();

        // Assert
        assertPoint(5, 5, t.children.get(0).getPosition());
        assertPoint(6, 4, t.children.get(1).getPosition());
        assertPoint(5, 4, t.children.get(2).getPosition());
        assertPoint(4, 4, t.children.get(3).getPosition());
    }
    
    /**
     * Testing a collision between
     * T with centre (5, 4), initial rotation
     * Z with centre (7, 4), R180 rotated
     *                 
     *          *---*                    
     *  T ->    |5,3|                   
     *      *---*---*---*---*       
     *      |4,4|5,4|col|7,4|
     *      *---*---*---*---*---*   <- Z     
     *                  |8,4|9,5|
     *                  *---*---*
     * expect: collision true
     */
    @Test 
    void collisionWithTetrominoTrueTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(5, 4), TetrominoType.T);
        Tetromino z = new Tetromino(new Point(7, 4), TetrominoType.Z);
        z.rotate180();

        // Act
        boolean actual = t.collidesWith(z);

        // Assert
        assertEquals(true, actual);
    }

        /**
     * Testing a collision between
     * T with centre (5, 4), initial rotation
     * L with centre (6, 5), initial rotation
     *                 
     *          *---*                    
     *  T ->    |5,3|                   
     *      *---*---*---*---*       
     *      |4,4|5,4|6,4|7,4|
     *      *---*---*---*---*   <- L     
     *          |5,5|6,5|7,5|
     *          *---*---*---*
     * expect: collision true
     */
    @Test 
    void collisionWithTetrominoFalseTest() {
        // Arrange
        Tetromino t = new Tetromino(new Point(5, 4), TetrominoType.T);
        Tetromino l = new Tetromino(new Point(6, 5), TetrominoType.L);

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
