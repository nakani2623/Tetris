package tetris.engine;

import java.util.LinkedList;
import java.util.List;

import tetris.engine.piece.Garbage;

public class GarbageQueue { 

    private Board board;
    private List<Garbage> garbageQueue = new LinkedList<>();

    public GarbageQueue(Board board) {
        this.board = board;
    }
    public void queue(Garbage garbage) {garbageQueue.addLast(garbage);}
    public void flush() {
        for (Garbage garbage : garbageQueue) {
            board.insertgarbage(garbage);
        }
        garbageQueue.clear();
    }
}
