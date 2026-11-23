package tetris;
import java.util.ArrayList;
import java.util.List;

import tetris.engine.type.Input.GameKey;

public class ReplayRecorder {
    private long startedAtNanos;
    private boolean active;

    private List<ReplayEvent> activeRecording;
    private List<ReplayEvent> lastRecording;

    public ReplayRecorder() {
        lastRecording = List.of();
    }

    public void start() {
        startedAtNanos = System.nanoTime();
        activeRecording = new ArrayList<>();
        active = true;
    }   

    public void stop() {
        active = false;
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
        if (active) {
            System.err.println("WARNING: Recorder suppose to be stopped first before exporting");
        }
        
        return lastRecording;
    }

    record ReplayEvent(long elapsedNanos, GameKey key, boolean pressed) {}
}


