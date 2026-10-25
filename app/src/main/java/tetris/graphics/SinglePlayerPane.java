package tetris.graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SinglePlayerPane extends StackPane {
    private Button sprintButton;
    private Button returnButton;
    private Button zenButton;
    public SinglePlayerPane() {
        Label title = new Label("Single player");
        sprintButton = new Button("Sprint mode");
        returnButton = new Button("Return to Menu");
        zenButton = new Button("Zen mode");

        VBox content = new VBox(title, sprintButton, zenButton, returnButton);
        this.getChildren().add(content);

        content.setAlignment(Pos.TOP_CENTER);
        content.setFillWidth(false);
        content.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );
        content.setSpacing(16);
        content.setPadding(new Insets(16));
        content.setStyle("-fx-border-color: gray;");
    }

    public void setOnSprint(Runnable action) {
        this.sprintButton.setOnAction(event -> action.run());
    }

    public void setOnReturnMenu(Runnable action) {
        this.returnButton.setOnAction(event -> action.run());
    }

    public void setOnZen(Runnable action) {
        this.zenButton.setOnAction(event -> action.run());
    }

}
