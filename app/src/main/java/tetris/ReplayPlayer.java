package tetris;

import java.util.List;
import java.util.function.Consumer;

import tetris.ReplayRecorder.ReplayEvent;

final class ReplayPlayer {
    private final List<ReplayEvent> events;
    private final long durationNanos;
    private int nextEvent;

    ReplayPlayer(List<ReplayEvent> events, long durationNanos) {
        this.events = events;
        this.durationNanos = durationNanos;
    }

    boolean advanceTo(long elapsedNanos, Consumer<ReplayEvent> applyInput) {
        while (nextEvent < events.size() && events.get(nextEvent).elapsedNanos() <= elapsedNanos) {
            applyInput.accept(events.get(nextEvent++));
        }
        return nextEvent == events.size() && elapsedNanos >= durationNanos;
    }
}
