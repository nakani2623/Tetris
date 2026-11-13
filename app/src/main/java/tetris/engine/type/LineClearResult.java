package tetris.engine.type;

public record LineClearResult (
    int totalLinesCleared,
    int garbageLinesCleared
) {}
