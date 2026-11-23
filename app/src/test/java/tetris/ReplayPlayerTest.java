package tetris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import tetris.ReplayRecorder.ReplayEvent;
import tetris.engine.type.Input.GameKey;

class ReplayPlayerTest {
    @Test
    void replaysInputInOrderAndWaitsForTheRecordedEnd() {
        var press = new ReplayEvent(10, GameKey.MOVE_LEFT, true);
        var release = new ReplayEvent(30, GameKey.MOVE_LEFT, false);
        var drop = new ReplayEvent(30, GameKey.HARDDROP, true);
        var player = new ReplayPlayer(List.of(press, release, drop), 50);
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
}
