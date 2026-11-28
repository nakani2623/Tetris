package tetris.engine;

import java.util.Random;

import tetris.engine.piece.Garbage;

public class GarbageGenerator {
    private static final int NO_HOLE = -1;
    private final Random random = new Random();
    private int lastCheeseHole = NO_HOLE;

    public Garbage generateAvoidSameHole(int nLines, int width) {
        int hole;
        do {
            hole = random.nextInt(width);
        } while (width > 1 && hole == lastCheeseHole);

        lastCheeseHole = hole;
        return new Garbage(width, nLines, hole);
    }

    void resetHoleHistory() {
        lastCheeseHole = NO_HOLE;
    }

    public Garbage generate(int nLines, int width) {
        int hole = random.nextInt(width);
        return new Garbage(width, nLines, hole);
    }

    public void setSeed(long seed) {
        random.setSeed(seed);
    }
}
