package tetris.engine.piece;

public class Garbage extends Piece {
    private int height;
    public Garbage(int width, int height, int hole) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == hole) continue;
                this.children.add(new Mino(x, y));
            }
        }
    }
    public int getHeight() {
        return height;
    }
}
