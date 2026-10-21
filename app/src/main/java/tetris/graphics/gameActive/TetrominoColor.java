package tetris.graphics.gameActive;

import javafx.scene.paint.Color;
import tetris.engine.type.TetrominoType;

public class TetrominoColor {

    public static Color forType(TetrominoType type) {
        return switch (type) {
            case T -> Color.PURPLE;
            case O -> Color.YELLOW;
            case I -> Color.CYAN;
            case S -> Color.GREEN;
            case Z -> Color.RED;
            case J -> Color.BLUE;
            case L -> Color.ORANGE;
        };
    }
}