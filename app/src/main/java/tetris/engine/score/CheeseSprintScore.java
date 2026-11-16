package tetris.engine.score;

import tetris.engine.Board;
import tetris.engine.GarbageGenerator;
import tetris.engine.GarbageQueue;
import tetris.engine.piece.Garbage;

public class CheeseSprintScore extends SprintScore {
    private final int maxGarbageLines = 10;
    private int pendingToFill = goalLines;
    private int linesToFill;
    private GarbageGenerator garbageGenerator = new GarbageGenerator();
    private GarbageQueue garbageQueue;
    private Board board;
    public CheeseSprintScore(int goalLines, Board board, GarbageQueue queue) {
        super(goalLines);
        this.garbageQueue = queue;
        this.board = board;
        // while (pendingToFill > 0 && linesToFill < maxGarbageLines) {
        //     linesToFill++;
        //     pendingToFill--;
        // }
    }

    public void reset() {
        super.reset();
        linesToFill = 0;
        pendingToFill = goalLines;
        while (pendingToFill > 0 && linesToFill < maxGarbageLines) {
            linesToFill++;
            pendingToFill--;
        }
    }

    public void onStart() {
        fillGarbage();
    }
    public void onLock(int nLinesCleared, int nGarbageLinesCleared) {
        if (nLinesCleared == 0) fillGarbage();
        else if (nGarbageLinesCleared != 0) {
            this.totalLinesCleared += nGarbageLinesCleared;
            if (pendingToFill > 0 && linesToFill + board.getGarbageHeight() < maxGarbageLines) {
                linesToFill += nGarbageLinesCleared;
                pendingToFill -= nGarbageLinesCleared;
            }
        }
        this.piecesPlaced++;
    }

    private void fillGarbage() {
        while (linesToFill > 0) {
            Garbage garbage = garbageGenerator.generateAvoidSameHole(1, (int)board.getWidth(), board.getLastHole());
            garbageQueue.queue(garbage);
            garbageQueue.flush();
            linesToFill--;
        }
    }
}
