package tetris.controller;

import tetris.engine.type.Direction;
import tetris.engine.type.GameState;
import tetris.engine.type.Rotation;

public interface Controllable {
    void move(Direction direction);
    void moveDAS(Direction direction);
    
    void rotate(Rotation rotation);
    void hardDrop();
    void hold();
    double getGravity();
    void start();
    void reset();
    GameState getGameState();
}
