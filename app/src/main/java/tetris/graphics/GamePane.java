package tetris.graphics;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tetris.engine.GameEngine;
import tetris.engine.observers.BoardDisplayManager;
import tetris.engine.observers.HoldDisplayManager;
import tetris.engine.observers.ScoreDisplayManager;
import tetris.engine.type.TetrominoType;

public class GamePane extends StackPane {
    public GamePane(GameEngine engine) {
        ScorePane scorePane = new ScorePane();
        HoldPane holdPane = new HoldPane();
        VBox leftBox = new VBox(holdPane, scorePane);

        BoardPane boardPane = new BoardPane(engine);
        NextQueuePane nextQueuePane = new NextQueuePane();

        HBox content = new HBox(16, leftBox, boardPane, nextQueuePane);
        content.setAlignment(Pos.TOP_CENTER);
        content.setFillHeight(false);

        content.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );

        setPadding(new Insets(20));
        this.getChildren().add(content);

        new BoardDisplayManager(engine, boardPane);
        new ScoreDisplayManager(engine.getScore(), scorePane);
        new HoldDisplayManager(engine, holdPane);

        TetrominoType next_array[] = new TetrominoType[] {TetrominoType.T, TetrominoType.I, TetrominoType.O, TetrominoType.O, TetrominoType.S, TetrominoType.I};
        List<TetrominoType> next = Arrays.asList(next_array);
        nextQueuePane.showNext(next);

    }
}
