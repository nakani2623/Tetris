package tetris.controller.input;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import tetris.engine.type.GameState;
import tetris.engine.type.UnifiedKey;
import tetris.engine.type.Input.GameKey;

public class KeyBinds {
    public Map<GameState, Map<UnifiedKey, GameKey>> binds;

    public KeyBinds() {
        binds = new HashMap<>();
        for (GameState state : GameState.values()) {
            Map bindOfState = new HashMap<UnifiedKey, GameKey>();
            binds.put(state, bindOfState);
        }
        try {load();} catch (Exception e) {}
        try {save();} catch (Exception e) {}
    }

    public GameKey map(UnifiedKey unifiedKey) {
        return binds.get(GameState.active).get(unifiedKey);
    }

    public void addBind(UnifiedKey unifiedKey, GameKey gameKey, GameState state) {
        binds.get(state).put(unifiedKey, gameKey);
    }

    private static final String FILE_NAME = "keybinds.properties";

    public void save() throws IOException {
        Properties properties = new Properties();

        for (var stateEntry : binds.entrySet()) {
            GameState state = stateEntry.getKey();

            for (var bindEntry : stateEntry.getValue().entrySet()) {
                UnifiedKey key = bindEntry.getKey();
                GameKey action = bindEntry.getValue();

                properties.setProperty(
                    state.name() + "." + key.name(),
                    action.name()
                );
            }
        }

        try (FileOutputStream out = new FileOutputStream(FILE_NAME)) {
            properties.store(out, "Tetris Key Bindings");
        }
    }

    public void load() throws IOException {
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(FILE_NAME)) {
            properties.load(in);
        }

        for (String propertyName : properties.stringPropertyNames()) {
            String[] parts = propertyName.split("\\.", 2);

            if (parts.length != 2) {
                continue;
            }

            try {
                GameState state = GameState.valueOf(parts[0]);
                UnifiedKey key = UnifiedKey.valueOf(parts[1]);
                GameKey action = GameKey.valueOf(properties.getProperty(propertyName));

                binds.get(state).put(key, action);

            } catch (IllegalArgumentException e) {
                System.err.println("Invalid key binding: " + propertyName);
            }
        }
    }
    // public void defaultSetting() {
    //     // gameState == active
    //     // Map activeBinds = binds.
    //     binds.get(GameState.active).put(UnifiedKey.SPACE, GameKey.HARDDROP);
    //     binds.get(GameState.active).put(UnifiedKey.Q, GameKey.ROTATE_CCW);
    //     binds.get(GameState.active).put(UnifiedKey.W, GameKey.HOLD);
    //     binds.get(GameState.active).put(UnifiedKey.E, GameKey.ROTATE_CW);
    //     binds.get(GameState.active).put(UnifiedKey.R, GameKey.ROTATE_180);

    //     binds.get(GameState.active).put(UnifiedKey.I, GameKey.MOVE_LEFT);
    //     binds.get(GameState.active).put(UnifiedKey.P, GameKey.MOVE_RIGHT);
    //     binds.get(GameState.active).put(UnifiedKey.O, GameKey.SOFTDROP);
    //     // binds.get(GameState.active).put(UnifiedKey.T, GameKey.RESTART);

    //     // NAKANI's
    //     binds.get(GameState.active).put(UnifiedKey.F, GameKey.HARDDROP);
    //     binds.get(GameState.active).put(UnifiedKey.A, GameKey.ROTATE_CCW);
    //     binds.get(GameState.active).put(UnifiedKey.S, GameKey.HOLD);
    //     binds.get(GameState.active).put(UnifiedKey.D, GameKey.ROTATE_CW);
    //     binds.get(GameState.active).put(UnifiedKey.SEMICOLON, GameKey.ROTATE_180);

    //     binds.get(GameState.active).put(UnifiedKey.J, GameKey.MOVE_LEFT);
    //     binds.get(GameState.active).put(UnifiedKey.L, GameKey.MOVE_RIGHT);
    //     binds.get(GameState.active).put(UnifiedKey.K, GameKey.SOFTDROP);
    //     binds.get(GameState.ended).put(UnifiedKey.R, GameKey.RESTART);
    // }
}

