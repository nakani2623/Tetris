package tetris.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import tetris.engine.type.Direction;
import tetris.engine.type.Rotation;
import tetris.engine.type.TetrominoType;
import tetris.utils.Point;

public class AutoMoveSchedulerTest {

    private GameEngine createEngine(double gravity, double SDF, double ARR, double INSTANT_THRESHOLD) {
        Board board = new Board(10, 20);
        GameEngine engine = new GameEngine(board);
        AutoMoveScheduler autoMoveScheduler = new AutoMoveScheduler(gravity, ARR, null, engine);
        engine.setAutoMoveScheduler(autoMoveScheduler);
        engine.setSoftDropFactor(SDF);
        AutoMoveScheduler.INSTANT_MOVE_THRESHOLD = INSTANT_THRESHOLD;
        return engine;
    }

    private void setDASCharge(Direction direction, GameEngine engine) {
        Map<Direction, Integer> charges = new HashMap<>(Map.of(Direction.LEFT, 0, Direction.RIGHT, 0));
        charges.put(direction, 1);
        engine.getAutoMoveScheduler().setDASCharges(charges);
    }
    // SDF * gravity > INSTANT_THRESHOLD
    //           AND
    // ARR > INSTANT_THRESHOLD
    // SDF*gravity>>>ARR, test: (SDF*gravity)/ARR >= 20
    @Test 
    void InstantHoriAndVert_1() {
        // Arrange
        GameEngine engine = createEngine(1, 100000, 10000, 600);
        Board b = engine.getBoard();
        Piece initialGround = new Piece(List.of(new Mino(0, 18), new Mino(1, 18), new Mino(3, 19)));
        b.allTetrominos.add(initialGround);
        b.spawn(TetrominoType.I);
        engine.rotate(Rotation.CLOCKWISE);
        engine.moveDAS(Direction.LEFT);
        engine.moveDAS(Direction.DOWN);
        // Act
        engine.startSoftDrop();
        setDASCharge(Direction.RIGHT, engine);
        engine.getAutoMoveScheduler().onOperate();
        // Assert
        Tetromino expacted = new Tetromino(new Point(1.5, 17.5), TetrominoType.I);
        expacted.rotateClockwise(new Point(1.5, 17.5));

        assertTrue(b.currentTetromino.isSamePosition(expacted));
        
    }

    // SDF * gravity > INSTANT_THRESHOLD
    //           AND
    // ARR > INSTANT_THRESHOLD
    // SDF*gravity<<<ARR, test: (SDF*gravity)/ARR <= 1/20
    @Test 
    void InstantHoriAndVert_2() {
        // Arrange
        GameEngine engine = createEngine(1, 1000, 10000, 600);
        Board b = engine.getBoard();
        Piece initialGround = new Piece(List.of(new Mino(0, 18), new Mino(1, 18), new Mino(3, 19)));
        b.allTetrominos.add(initialGround);
        b.spawn(TetrominoType.I);
        engine.rotate(Rotation.CLOCKWISE);
        engine.moveDAS(Direction.LEFT);
        engine.moveDAS(Direction.DOWN);
        // Act
        engine.startSoftDrop();
        setDASCharge(Direction.RIGHT, engine);
        engine.getAutoMoveScheduler().onOperate();
        // Assert
        Tetromino expacted = new Tetromino(new Point(7.5, 17.5), TetrominoType.I);
        expacted.rotateClockwise();
        System.out.println("expected: "+expacted);
        System.out.println(b.currentTetromino);
        assertTrue(b.currentTetromino.isSamePosition(expacted));
    }


}
