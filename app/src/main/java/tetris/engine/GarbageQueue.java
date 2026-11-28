package tetris.engine;

import java.util.LinkedList;
import java.util.List;

import tetris.engine.piece.Garbage;
import tetris.engine.type.LineClearResult;

public class GarbageQueue {
    private final Board board;
    private final GarbageRisePolicy risePolicy;
    private final List<Garbage> garbageQueue = new LinkedList<>();
    private final GarbageGenerator generator = new GarbageGenerator();

    public GarbageQueue(Board board, GarbageRisePolicy risePolicy) {
        this.board = board;
        this.risePolicy = risePolicy;
    }

    public void onRoundStart() {
        garbageQueue.clear();
        generator.resetHoleHistory();
        fill(risePolicy.onRoundStart());
    }

    public void onLock(LineClearResult result) {
        fill(risePolicy.onLock(result, board.getGarbageHeight()));
    }

    private void fill(int lines) {
        for (int i = 0; i < lines; i++) {
            Garbage garbage = generator.generateAvoidSameHole(1, (int)board.getWidth());
            board.insertgarbage(garbage);
        }
    }

    public void setSeed(long seed) {
        generator.setSeed(seed);
    }
}
