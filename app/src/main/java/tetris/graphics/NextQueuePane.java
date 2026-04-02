package tetris.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.layout.VBox;

public class NextQueuePane extends VBox {
    private List<Object> nextQueue;
    public NextQueuePane() {
        nextQueue = new LinkedList<>();
    }

}
