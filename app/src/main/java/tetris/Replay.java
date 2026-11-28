package tetris;

import java.util.List;

import tetris.engine.type.Input.GameKey;

public record Replay(long seed, long durationNanos, List<ReplayEvent> events) {
    public Replay {
        events = List.copyOf(events);
    }

    public record ReplayEvent(long elapsedNanos, GameKey key, boolean pressed) {}
}
