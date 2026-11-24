package tetris.engine;

import tetris.engine.type.LineClearResult;

public final class CheeseSprintGarbagePolicy implements GarbageRisePolicy {
    private final int totalGarbageLines;
    private final int maxGarbageHeight;
    private int remainingLines;
    private int pendingLines;

    public CheeseSprintGarbagePolicy(int totalGarbageLines, int maxGarbageHeight) {
        if (totalGarbageLines < 0 || maxGarbageHeight <= 0)
            throw new IllegalArgumentException("Garbage total must be nonnegative and height limit must be positive");

        this.totalGarbageLines = totalGarbageLines;
        this.maxGarbageHeight = maxGarbageHeight;
    }

    @Override
    public int onRoundStart() {
        int initialLines = Math.min(totalGarbageLines, maxGarbageHeight);
        remainingLines = totalGarbageLines - initialLines;
        pendingLines = 0;
        return initialLines;
    }

    @Override
    public int onLock(LineClearResult result, int garbageHeight) {
        int availableRows = Math.max(0, maxGarbageHeight - garbageHeight);
        if (result.totalLinesCleared() == 0) {
            int linesToInsert = Math.min(pendingLines, availableRows);
            pendingLines -= linesToInsert;
            return linesToInsert;
        }

        int availableToReserve = Math.max(0, availableRows - pendingLines);
        int replacementLines = Math.min(result.garbageLinesCleared(), Math.min(remainingLines, availableToReserve));
        pendingLines += replacementLines;
        remainingLines -= replacementLines;
        return 0;
    }
}
