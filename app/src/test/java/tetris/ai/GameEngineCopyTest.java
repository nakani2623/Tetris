package tetris.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import tetris.engine.GameEngine;
import tetris.engine.Mino;
import tetris.engine.Tetromino;
import tetris.engine.type.RotationState;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

class GameEngineCopyTest {
    @Test
    void copyDeepCopiesBoardPiecesAndKeepsCurrentPieceInCopiedList() {
        GameEngine source = engineWithPieces();
        GameEngine copy = new GameEngine(source);
        List<Tetromino> sourcePieces = source.getAllTetrominos();
        List<Tetromino> copiedPieces = copy.getAllTetrominos();

        assertEquals(sourcePieces.size(), copiedPieces.size());
        for (int i = 0; i < sourcePieces.size(); i++) {
            Tetromino sourcePiece = sourcePieces.get(i);
            Tetromino copiedPiece = copiedPieces.get(i);
            assertNotSame(sourcePiece, copiedPiece);
            assertNotSame(sourcePiece.centre, copiedPiece.centre);
            assertEquals(sourcePiece.children.size(), copiedPiece.children.size());
            for (int j = 0; j < sourcePiece.children.size(); j++) {
                Mino sourceMino = sourcePiece.children.get(j);
                Mino copiedMino = copiedPiece.children.get(j);
                assertNotSame(sourceMino, copiedMino);
                assertNotSame(sourceMino.getPosition(), copiedMino.getPosition());
                assertEquals(sourceMino.getPosition(), copiedMino.getPosition());
            }
        }
        assertSame(copiedPieces.get(1), copy.getCurrentTetromino());

        copiedPieces.get(0).children.get(0).getPosition().setX(99);
        copy.getCurrentTetromino().moveRight();
        assertEquals(2, sourcePieces.get(0).children.get(0).getPosition().getX());
        assertEquals(5, source.getCurrentTetromino().centre.getX());
    }

    @Test
    void exploringCopiedEngineFindsMovesWithoutChangingSourcePieces() {
        GameEngine source = engineWithPieces();
        List<TetrominoSnapshot> before = snapshot(source.getAllTetrominos());

        GameEngine exploredEngine = new GameEngine(source);
        Set<DropPoint> dropPoints = new DropPointExporer(exploredEngine).get();

        assertTrue(dropPoints.stream().anyMatch(
            point -> !point.tetromino.isSamePosition(source.getCurrentTetromino())
        ));
        assertEquals(before, snapshot(source.getAllTetrominos()));
    }

    private GameEngine engineWithPieces() {
        GameEngine engine = new GameEngine(10, 20, null);
        Tetromino lockedPiece = new Tetromino(new Point(2, 17), TetrominoType.J);
        Tetromino currentPiece = new Tetromino(new Point(5, 5), TetrominoType.T);
        engine.getAllTetrominos().add(lockedPiece);
        engine.getAllTetrominos().add(currentPiece);
        engine.setCurrentTetromino(currentPiece);
        return engine;
    }

    private List<Tetromino> boardPieces(GameEngine engine) {
        return engine.getAllTetrominos();
    }

    private List<TetrominoSnapshot> snapshot(List<Tetromino> pieces) {
        List<TetrominoSnapshot> snapshots = new ArrayList<>();
        for (Tetromino piece : pieces) {
            List<Point> minos = piece.children.stream()
                .map(mino -> new Point(mino.getPosition()))
                .toList();
            snapshots.add(new TetrominoSnapshot(
                new Point(piece.centre),
                piece.rotationState,
                minos
            ));
        }
        return snapshots;
    }

    private record TetrominoSnapshot(
        Point centre,
        RotationState rotationState,
        List<Point> minoPositions
    ) {}
}
