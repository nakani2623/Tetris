package tetris.graphics;


import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HoldPane extends VBox {
    public HoldPane() {
        Label title = new Label("HOLD");
        StackPane holdBox = new StackPane();

        this.getChildren().addAll(title, holdBox);
    }
}
