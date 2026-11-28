package tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tetris.Replay.ReplayEvent;
import tetris.engine.type.Input.GameKey;

public class ReplayRecorder {
    private long startedAtNanos;
    private long seed;

    private List<ReplayEvent> activeRecording;
    private Replay lastReplay;

    public void start(long seed) {
        this.seed = seed;
        startedAtNanos = System.nanoTime();
        activeRecording = new ArrayList<>();
    }

    public void stop() {
        if (activeRecording == null)
            return;

        long durationNanos = System.nanoTime() - startedAtNanos;
        lastReplay = new Replay(seed, durationNanos, activeRecording);
        activeRecording = null;
    }

    public void record(GameKey key, boolean pressed) {
        if (activeRecording == null)
            return;

        long elapsedNanos = System.nanoTime() - startedAtNanos;
        activeRecording.add(new ReplayEvent(elapsedNanos, key, pressed));
    }

    public Optional<Replay> export() {
        return Optional.ofNullable(lastReplay);
    }
}


