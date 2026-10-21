package tetris.controller.input;

import javafx.scene.input.KeyCode;
import tetris.engine.type.UnifiedKey;

public class JavaFXKeyboardAdapter {

    public static UnifiedKey toUnifiedKey(KeyCode key) {
        if (key == null) {
            return UnifiedKey.UNKNOWN;
        }

        return switch (key) {
            // Numbers (top row)
            case DIGIT0 -> UnifiedKey.DIGIT_0;
            case DIGIT1 -> UnifiedKey.DIGIT_1;
            case DIGIT2 -> UnifiedKey.DIGIT_2;
            case DIGIT3 -> UnifiedKey.DIGIT_3;
            case DIGIT4 -> UnifiedKey.DIGIT_4;
            case DIGIT5 -> UnifiedKey.DIGIT_5;
            case DIGIT6 -> UnifiedKey.DIGIT_6;
            case DIGIT7 -> UnifiedKey.DIGIT_7;
            case DIGIT8 -> UnifiedKey.DIGIT_8;
            case DIGIT9 -> UnifiedKey.DIGIT_9;

            // Modifier keys
            // JavaFX does not distinguish left/right modifiers
            case SHIFT -> UnifiedKey.LEFT_SHIFT;
            case CONTROL -> UnifiedKey.LEFT_CTRL;
            case ALT, ALT_GRAPH -> UnifiedKey.LEFT_ALT;
            case META, WINDOWS, COMMAND -> UnifiedKey.LEFT_META;

            // Editing and control
            case BACK_SPACE -> UnifiedKey.BACKSPACE;

            // Punctuation
            case OPEN_BRACKET -> UnifiedKey.LEFT_BRACKET;
            case CLOSE_BRACKET -> UnifiedKey.RIGHT_BRACKET;
            case BACK_SLASH -> UnifiedKey.BACKSLASH;
            case QUOTE -> UnifiedKey.APOSTROPHE;
            case BACK_QUOTE -> UnifiedKey.GRAVE;

            // Numeric keypad
            case NUMPAD0 -> UnifiedKey.NUMPAD_0;
            case NUMPAD1 -> UnifiedKey.NUMPAD_1;
            case NUMPAD2 -> UnifiedKey.NUMPAD_2;
            case NUMPAD3 -> UnifiedKey.NUMPAD_3;
            case NUMPAD4 -> UnifiedKey.NUMPAD_4;
            case NUMPAD5 -> UnifiedKey.NUMPAD_5;
            case NUMPAD6 -> UnifiedKey.NUMPAD_6;
            case NUMPAD7 -> UnifiedKey.NUMPAD_7;
            case NUMPAD8 -> UnifiedKey.NUMPAD_8;
            case NUMPAD9 -> UnifiedKey.NUMPAD_9;

            case ADD -> UnifiedKey.NUMPAD_ADD;
            case SUBTRACT -> UnifiedKey.NUMPAD_SUBTRACT;
            case MULTIPLY -> UnifiedKey.NUMPAD_MULTIPLY;
            case DIVIDE -> UnifiedKey.NUMPAD_DIVIDE;
            case DECIMAL -> UnifiedKey.NUMPAD_DECIMAL;

            // Numeric keypad navigation
            case KP_UP -> UnifiedKey.UP;
            case KP_DOWN -> UnifiedKey.DOWN;
            case KP_LEFT -> UnifiedKey.LEFT;
            case KP_RIGHT -> UnifiedKey.RIGHT;

            // Additional keys
            case PRINTSCREEN -> UnifiedKey.PRINT_SCREEN;
            case CONTEXT_MENU -> UnifiedKey.MENU;
            case MUTE -> UnifiedKey.VOLUME_MUTE;
            case PLAY -> UnifiedKey.MEDIA_PLAY_PAUSE;
            case STOP -> UnifiedKey.MEDIA_STOP;
            case TRACK_NEXT -> UnifiedKey.MEDIA_NEXT;
            case TRACK_PREV -> UnifiedKey.MEDIA_PREVIOUS;

            // Same-name keys
            default -> mapSameName(key);
        };
    }

    private static UnifiedKey mapSameName(KeyCode key) {
        try {
            return UnifiedKey.valueOf(key.name());
        } catch (IllegalArgumentException e) {
            return UnifiedKey.UNKNOWN;
        }
    }
}