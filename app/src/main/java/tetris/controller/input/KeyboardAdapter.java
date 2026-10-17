package tetris.controller.input;

import tetris.engine.type.UnifiedKey;

public interface KeyboardAdapter<T> {
    UnifiedKey mapUnified(T nativeKey);
}
