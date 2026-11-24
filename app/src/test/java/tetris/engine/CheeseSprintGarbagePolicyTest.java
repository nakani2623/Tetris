package tetris.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import tetris.engine.piece.Mino;
import tetris.engine.type.LineClearResult;

class CheeseSprintGarbagePolicyTest {
    @Test
    void defersReplacementUntilANonClearingLockAndRespectsBothLimits() {
        var policy = new CheeseSprintGarbagePolicy(15, 10);
        var noClear = new LineClearResult(0, 0);

        assertEquals(10, policy.onRoundStart());
        assertEquals(0, policy.onLock(new LineClearResult(2, 2), 8));
        assertEquals(0, policy.onLock(new LineClearResult(1, 0), 8));
        assertEquals(2, policy.onLock(noClear, 8));

        assertEquals(0, policy.onLock(new LineClearResult(4, 4), 6));
        assertEquals(1, policy.onLock(noClear, 9));
        assertEquals(2, policy.onLock(noClear, 8));
        assertEquals(0, policy.onLock(noClear, 8));
    }

    @Test
    void engineFillsOnStartAndAfterANonClearingLock() {
        var engine = new GameEngine(10, 20);
        Board board = engine.getBoard();
        engine.setGarbageQueue(new GarbageQueue(board, new CheeseSprintGarbagePolicy(2, 1)));
        engine.start(7);

        assertEquals(1, board.getGarbageHeight());
        var current = board.getcurrentTetromino();
        current.children.clear();
        current.children.add(new Mino(board.getLastHole(), 0));
        engine.lock();
        assertEquals(0, board.getGarbageHeight());

        engine.hardDrop();
        assertEquals(1, board.getGarbageHeight());

        engine.reset();
        engine.start(7);
        assertEquals(1, board.getGarbageHeight());
    }
}
