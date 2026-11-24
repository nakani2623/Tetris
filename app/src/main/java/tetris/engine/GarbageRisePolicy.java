package tetris.engine;

import tetris.engine.type.LineClearResult;

public interface GarbageRisePolicy {
    /** Starts a new round and returns the number of garbage rows to insert immediately. */
    int onRoundStart();

    /** Returns the number of garbage rows to insert after this lock and line clear. */
    int onLock(LineClearResult result, int garbageHeight);
}
