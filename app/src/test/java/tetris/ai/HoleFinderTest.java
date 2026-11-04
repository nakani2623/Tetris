package tetris.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;

import tetris.engine.GameEngine;
import tetris.engine.Tetromino;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class HoleFinderTest {
    @Test 
    void emptyBoardHasOneHole() {
        // Arrange
        GameEngine engine = new GameEngine(10, 20, null);
        HoleFinder holeFinder = new HoleFinder(engine);

        // Act
        int holes = holeFinder.run();

        // Assert
        assertEquals(1, holes);
    }

    @Test 
    void currentTetrominoIgnored() {
        // Arrange
        GameEngine engine = new GameEngine(4, 20, null);
        Tetromino current = new Tetromino(new Point(1, 10), TetrominoType.I);
        addTetromino(engine, current);
        engine.setCurrentTetromino(current);
        HoleFinder holeFinder = new HoleFinder(engine);

        // Act
        int holes = holeFinder.run();

        // Assert
        assertEquals(1, holes);
    }
    
    @Test
    void singleTetrominoCreatesHoles() {
        // Arrange
        GameEngine engine = new GameEngine(4, 20, null);
        addTetromino(engine, new Tetromino(new Point(1, 10), TetrominoType.I));
        HoleFinder holeFinder = new HoleFinder(engine);

        // Act
        int holes = holeFinder.run();

        // Assert
        assertEquals(2, holes);
    }

    @Test
    void multipleTetrominosCreateHoles() {
        // Arrange
        GameEngine engine = new GameEngine(4, 20, null);
        addTetromino(engine, new Tetromino(new Point(1, 6), TetrominoType.I));
        addTetromino(engine, new Tetromino(new Point(1, 12), TetrominoType.I));
        HoleFinder holeFinder = new HoleFinder(engine);

        // Act
        int holes = holeFinder.run();

        // Assert
        assertEquals(3, holes);
    }

    @SuppressWarnings("unchecked")
    private void addTetromino(GameEngine engine, Tetromino tetromino) {
        try {
            Field field = GameEngine.class.getDeclaredField("allTetrominos");
            field.setAccessible(true);
            ((List<Tetromino>) field.get(engine)).add(tetromino);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Could not access test engine board", e);
        }
    }
}