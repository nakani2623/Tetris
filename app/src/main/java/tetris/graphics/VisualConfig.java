package tetris.graphics;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.paint.Color;

public class VisualConfig {
    public static final Color PANEL_BACKGROUND = Color.rgb(0, 0, 0, 0.7);
    public static final boolean ColoredShadow = true;
    public static final double ShadowOpacity = 0.3;
    public static final Rectangle2D screen_bounds = Screen.getPrimary().getVisualBounds();
}
