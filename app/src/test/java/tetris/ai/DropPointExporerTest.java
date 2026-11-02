package tetris.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import tetris.engine.GameEngine;
import tetris.engine.Tetromino;
import tetris.engine.type.Direction;
import tetris.engine.type.RotationState;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

class DropPointExporerTest {
    @Test
    void findsEveryTPlacementOnAnEmptyTenWideBoard() {
        assertFindsAllTPlacements(10, 5);
    }

    @Test
    void findsEveryTPlacementOnAnEmptySixWideBoard() {
        assertFindsAllTPlacements(6, 3);
    }

    private void assertFindsAllTPlacements(int width, int startX) {
        GameEngine engine = engineWithCurrentTetromino(
            width,
            new Tetromino(new Point(startX, 5), TetrominoType.T)
        );
        Set<DropPoint> dropPoints = new DropPointExporer(engine).get();
        Set<Position> actualPositions = new HashSet<>();
        dropPoints.forEach(dropPoint -> actualPositions.add(positionOf(dropPoint.ghost)));
        Set<Position> expectedPositions = new HashSet<>();
        for (int x = 1; x < width - 1; x++) {
            for (RotationState rotationState : RotationState.values()) {
                expectedPositions.add(new Position(x, landingY(rotationState), rotationState));
            }
        }
        expectedPositions.add(new Position(0, landingY(RotationState.RIGHT), RotationState.RIGHT));
        expectedPositions.add(new Position(
            width - 1,
            landingY(RotationState.LEFT),
            RotationState.LEFT
        ));

        assertEquals(expectedPositions, actualPositions);
        assertEquals(4 * (width - 2) + 2, dropPoints.size());
    }

    private int landingY(RotationState rotationState) {
        return switch (rotationState) {
            case ZERO -> 19;
            case RIGHT, TWO, LEFT -> 18;
        };
    }

    private GameEngine engineWithCurrentTetromino(int width, Tetromino current) {
        GameEngine engine = new GameEngine(width, 20, null);
        boardPieces(engine).add(current);
        engine.setCurrentTetromino(current);
        engine.move(Direction.LEFT);
        engine.move(Direction.RIGHT);
        return engine;
    }

    @SuppressWarnings("unchecked")
    private List<Tetromino> boardPieces(GameEngine engine) {
        try {
            Field field = GameEngine.class.getDeclaredField("allTetrominos");
            field.setAccessible(true);
            return (List<Tetromino>) field.get(engine);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Could not access test engine board", e);
        }
    }

    private Position positionOf(Tetromino tetromino) {
        return new Position(
            tetromino.centre.getX(),
            tetromino.centre.getY(),
            tetromino.rotationState
        );
    }

    private record Position(double x, double y, RotationState rotationState) {}
}
