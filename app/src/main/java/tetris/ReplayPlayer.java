package tetris;

import java.util.List;
import java.util.function.Consumer;

import tetris.Replay.ReplayEvent;

final class ReplayPlayer {
    private final List<ReplayEvent> events;
    private final long durationNanos;
    private int nextEvent;

    ReplayPlayer(Replay replay) {
        this.events = replay.events();
        this.durationNanos = replay.durationNanos();
    }

    boolean advanceTo(long elapsedNanos, Consumer<ReplayEvent> applyInput) {
        while (nextEvent < events.size() && events.get(nextEvent).elapsedNanos() <= elapsedNanos) {
            applyInput.accept(events.get(nextEvent++));
        }
        return nextEvent == events.size() && elapsedNanos >= durationNanos;
    }
}
