package tetris.engine;

public interface LockStrategy {
    /**
     * this method is called when the location of controlling tetromino changes,
     * it checks whether the controlling tetromino should be locked
     * @param tetromino
     * @param board
     */
    void checkForLock(Tetromino tetromino, GameEngine engine);


    /**
     * lock the tetromino
     * @param tetromino
     * @param board
     */
    void lock(Tetromino tetromino, GameEngine engine);
}
