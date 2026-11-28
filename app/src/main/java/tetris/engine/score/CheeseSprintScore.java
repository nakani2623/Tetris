package tetris.engine.score;

import tetris.engine.type.LineClearResult;

public class CheeseSprintScore extends SprintScore {
    public CheeseSprintScore(int goalLines) {
        super(goalLines);
    }

    @Override
    public void onLock(LineClearResult result) {
        totalLinesCleared += result.garbageLinesCleared();
        piecesPlaced++;
    }
}
