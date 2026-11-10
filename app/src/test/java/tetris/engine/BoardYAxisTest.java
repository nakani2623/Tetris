package tetris.engine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

import tetris.engine.piece.Mino;
import tetris.engine.piece.Piece;
import tetris.engine.piece.Tetromino;
import tetris.engine.rotator.SuperRotator;
import tetris.engine.spinDetecter.SpinDetectUtils;
import tetris.engine.type.Rotation;
import tetris.engine.type.SpinState;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

class BoardYAxisTest {
    @Test
    void spawnAndShadowUseUpwardCoordinatesForEveryPiece() {
        for (TetrominoType type : TetrominoType.values()) {
            Board board = new Board(10, 20);
            assertTrue(board.spawn(type));
            Tetromino piece = board.getcurrentTetromino();
            for (Mino mino : piece.children) {
                assertTrue(mino.getPosition().getY() >= 18);
                assertTrue(mino.getPosition().getY() <= 19);
            }
            Tetromino shadow = board.getShadowTetromino();
            assertEquals(0, shadow.children.stream().mapToDouble(m -> m.getPosition().getY()).min().orElseThrow());
            assertTrue(board.landed(shadow));
            double y = piece.centre.getY();
            piece.moveDown();
            assertEquals(y - 1, piece.centre.getY());
            piece.moveUp();
            assertEquals(y, piece.centre.getY());
        }
    }

    @Test
    void floorBlocksNegativeYAndTopRemainsOpen() {
        Board board = new Board(10, 20);
        assertTrue(board.hasWallCollision(new Piece(new Mino(4, -1))));
        assertFalse(board.hasWallCollision(new Piece(new Mino(4, 0))));
        assertFalse(board.hasWallCollision(new Piece(new Mino(4, 20))));
    }

    @Test
    void clearingTwoRowsDropsOnlyMinosAboveThem() {
        Board board = new Board(4, 20);
        Tetromino locked = new Tetromino(new Point(1.5, 1.5), TetrominoType.O);
        Piece remaining = new Piece(List.of(new Mino(0, 1), new Mino(3, 1),
            new Mino(0, 2), new Mino(3, 2), new Mino(0, 4)));
        Piece above = new Piece(new Mino(2, 8));
        Piece below = new Piece(new Mino(1, 0));
        board.allTetrominos.addAll(List.of(locked, remaining, above, below));
        board.setCurrentTetromino(locked);
        assertEquals(2, board.clearCompleted());
        assertTrue(locked.children.isEmpty());
        assertEquals(1, remaining.children.size());
        assertEquals(2, remaining.children.get(0).getPosition().getY());
        assertEquals(6, above.children.get(0).getPosition().getY());
        assertEquals(0, below.children.get(0).getPosition().getY());
    }

    @Test
    void clockwiseFloorKickMovesTUpwardAndLeft() {
        Board board = new Board(10, 20);
        Tetromino piece = new Tetromino(new Point(4, 0), TetrominoType.T);
        board.allTetrominos.add(piece);
        board.setCurrentTetromino(piece);
        assertTrue(new SuperRotator().rotate(Rotation.CLOCKWISE, board));
        assertEquals(3, piece.centre.getX());
        assertEquals(1, piece.centre.getY());
        assertFalse(board.hasWallCollision(piece));
    }

    @Test
    void tSpinFrontCornersStayAboveSpawnOrientation() {
        Board board = new Board(10, 20);
        Tetromino piece = new Tetromino(new Point(4, 1), TetrominoType.T);
        board.setCurrentTetromino(piece);
        board.allTetrominos.add(piece);
        board.allTetrominos.add(new Piece(List.of(new Mino(3, 2), new Mino(5, 2), new Mino(3, 0))));
        assertEquals(SpinState.PROPER_SPIN, SpinDetectUtils.detectTSpin(board));
    }
}
