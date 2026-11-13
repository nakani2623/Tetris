package tetris.engine;

import java.util.Random;

import tetris.engine.piece.Garbage;

public class GarbageGenerator {
    private Random random = new Random();
    public Garbage generateAvoidSameHole(int nLines, int width, int lastHole) {
        int hole;
        while ((hole = random.nextInt(width)) == lastHole);
        return new Garbage(width, nLines, hole);
    }
    public Garbage generate(int nLines, int width) {
        int hole = random.nextInt(width);
        return new Garbage(width, nLines, hole);
    }
}
