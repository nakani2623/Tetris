package tetris.graphics;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HoldPane extends VBox {
    public HoldPane() {
        Label title = new Label("HOLD");
        StackPane preview = new StackPane();
        preview.setMinSize(90, 60);
        preview.setPrefSize(90, 60);
        preview.setMaxSize(90, 60);

        this.setStyle("-fx-border-color: gray;");

        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);

        this.getChildren().addAll(title, preview);
    }
}
