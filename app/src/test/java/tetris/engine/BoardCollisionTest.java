package tetris.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class BoardCollisionTest {
    /**
     * Testing a collision between
     * T with centre (5, 4), initial rotation
     * A 10x20 Board
     *                  | xxxxxx
     *                  | xxxxxx
     *              *---* xxxxxx                    
     *              |9,3| xxxxxx             
     *          *---*---*---* xx 
     *          |8,4|9,4|col| xx  <- Wall 
     *          *---*---*---* xx
     *                  | xxxxxx
     *                  | xxxxxx
     *                  | xxxxxx
     * expect: collision true
     */
    @Test 
    void collisionWithLeftWall() {
        // Arrange
        Board b = new Board(10, 20);
        Tetromino t = new Tetromino(new Point(9, 4), TetrominoType.T);

        // Act
        boolean actual = b.hasWallCollision(t);

        // Assert
        assertEquals(true, actual);
    }

        /**
     * Testing a collision between
     * T with centre (5, 9), CCW rotated
     * A 10x10 Board
     * 
     *              *---*                
     *              |5,8|          
     *          *---*---*
     *          |4,9|5,9|
     * -------- *---*---* ------------  <- Wall
     * xxxxxxxxxxxx |col| xxxxxxxxxxx
     * xxxxxxxxxxxx *---* xxxxxxxxxxx
     * xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx   
     *          
     * expect: collision true
     */
    @Test 
    void collisionWithBottomWall() {
        // Arrange
        Board b = new Board(10, 10);
        Tetromino t = new Tetromino(new Point(5, 9), TetrominoType.T);
        t.rotateCounterClockwise();

        // Act
        boolean actual = b.hasWallCollision(t);

        // Assert
        assertEquals(true, actual);
    }
}
