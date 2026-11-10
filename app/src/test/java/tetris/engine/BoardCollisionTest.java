package tetris.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import tetris.engine.piece.Tetromino;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class BoardCollisionTest {
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

    @Test
    void collisionWithBottomWall() {
        // Arrange
        Board b = new Board(10, 10);
        Tetromino t = new Tetromino(new Point(5, 0), TetrominoType.T);
        t.rotateCounterClockwise();

        // Act
        boolean actual = b.hasWallCollision(t);

        // Assert
        assertEquals(true, actual);
    }
}
