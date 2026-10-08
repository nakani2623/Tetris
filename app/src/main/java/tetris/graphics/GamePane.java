package tetris.graphics;

import javafx.scene.layout.HBox;
import tetris.engine.GameEngine;
import tetris.engine.observers.BoardDisplayManager;
import tetris.engine.observers.ScoreDisplayManager;

public class GamePane extends HBox {
    public GamePane(GameEngine engine) {
        BoardPane boardPane = new BoardPane(engine);
        this.getChildren().add(boardPane);
        new BoardDisplayManager(engine, boardPane);

        ScorePane scorePanel = new ScorePane();
        this.getChildren().add(scorePanel);
        new ScoreDisplayManager(engine.getScore(), scorePanel);
    }
}
