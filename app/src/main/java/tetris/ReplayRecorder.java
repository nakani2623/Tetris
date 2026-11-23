package tetris;
import java.util.ArrayList;
import java.util.List;

import tetris.engine.type.Input.GameKey;

public class ReplayRecorder {
    private long startedAtNanos;
    private boolean active;

    private List<ReplayEvent> activeRecording;
    private List<ReplayEvent> lastRecording;
    private long lastDurationNanos;

    public ReplayRecorder() {
        lastRecording = List.of();
    }

    public void start() {
        startedAtNanos = System.nanoTime();
        activeRecording = new ArrayList<>();
        active = true;
    }   

    public void stop() {
        if (!active)
            return;

        active = false;
        lastDurationNanos = System.nanoTime() - startedAtNanos;
        lastRecording = List.copyOf(activeRecording);
        activeRecording = null;
    }

    public void record(GameKey key, boolean pressed) {
        if (!active) 
            return;

        long elapsedNanos = System.nanoTime() - startedAtNanos;
        ReplayEvent re = new ReplayEvent(elapsedNanos, key, pressed);
        activeRecording.add(re);
    }

    public List<ReplayEvent> export() {
        return lastRecording;
    }

    public long getLastDurationNanos() {
        return lastDurationNanos;
    }

    record ReplayEvent(long elapsedNanos, GameKey key, boolean pressed) {}
}


