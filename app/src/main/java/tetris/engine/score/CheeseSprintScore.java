package tetris.engine.score;

public class CheeseSprintScore extends SprintScore {
    public CheeseSprintScore(int goalLines) {
        super(goalLines);
    }
    public void onLock(int nLinesCleared, int nGarbageLinesCleared) {
        this.totalLinesCleared += nGarbageLinesCleared;
        this.piecesPlaced++;
    }
}
