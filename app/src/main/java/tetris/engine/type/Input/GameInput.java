package tetris.engine.type.Input;

import tetris.engine.type.UnifiedKey;

public record GameInput(GameKey gameKey, KeyAction keyAction, UnifiedKey unifiedKey) {
    
}
