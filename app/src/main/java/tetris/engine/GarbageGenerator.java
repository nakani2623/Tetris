package tetris.engine;

import java.util.Random;

import tetris.engine.piece.Garbage;

public class GarbageGenerator {
    private Random random = new Random();
    private int lastHole;
    public Garbage generate(int nLines, int width) {
        int hole;
        while ((hole = random.nextInt(width)) == lastHole);
        return new Garbage(width, nLines, hole);
    }
    public void setLastHole(int lastHole) {
        this.lastHole = lastHole;
    }
}
