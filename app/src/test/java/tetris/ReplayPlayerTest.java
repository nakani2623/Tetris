package tetris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import tetris.Replay.ReplayEvent;
import tetris.engine.GameEngine;
import tetris.engine.type.Input.GameKey;
import tetris.engine.type.TetrominoType;

class ReplayPlayerTest {
    @Test
    void replaysInputInOrderAndWaitsForTheRecordedEnd() {
        var press = new ReplayEvent(10, GameKey.MOVE_LEFT, true);
        var release = new ReplayEvent(30, GameKey.MOVE_LEFT, false);
        var drop = new ReplayEvent(30, GameKey.HARDDROP, true);
        var player = new ReplayPlayer(new Replay(0, 50, List.of(press, release, drop)));
        var delivered = new ArrayList<ReplayEvent>();

        assertFalse(player.advanceTo(9, delivered::add));
        assertTrue(delivered.isEmpty());
        assertFalse(player.advanceTo(10, delivered::add));
        assertEquals(List.of(press), delivered);
        assertFalse(player.advanceTo(40, delivered::add));
        assertEquals(List.of(press, release, drop), delivered);
        assertTrue(player.advanceTo(50, delivered::add));
        assertEquals(List.of(press, release, drop), delivered);
    }

    @Test
    void recordedSeedRestartsThePieceSequenceAcrossBags() {
        var engine = new GameEngine(10, 20);
        var recorder = new ReplayRecorder();
        long seed = 0;

        recorder.start(seed);
        engine.start(seed);
        List<TetrominoType> original = readPieceSequence(engine);
        recorder.record(GameKey.HARDDROP, true);
        recorder.stop();

        recorder.start(99);
        engine.reset();
        engine.start(99);
        assertNotEquals(original, readPieceSequence(engine));

        Replay replay = recorder.export().orElseThrow();
        assertEquals(seed, replay.seed());
        assertEquals(GameKey.HARDDROP, replay.events().getFirst().key());
        engine.reset();
        engine.start(replay.seed());

        assertEquals(original, readPieceSequence(engine));
    }

    private List<TetrominoType> readPieceSequence(GameEngine engine) {
        var pieces = new ArrayList<TetrominoType>();
        pieces.add(engine.getBoard().getcurrentTetromino().type);
        for (int i = 1; i < 21; i++) {
            pieces.add(engine.getNextQueueManager().remove());
        }
        return pieces;
    }
}
