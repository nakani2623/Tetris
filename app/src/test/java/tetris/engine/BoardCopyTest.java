package tetris.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tetris.engine.type.TetrominoType;

class BoardCopyTest {
    @Test
    void copyDeepCopiesTetrominoesMinosAndPoints() {
        Board source = new Board(10, 20);
        assertTrue(source.spawn(TetrominoType.T));

        Tetromino sourceCurrent = source.getcurrentTetromino();
        Board copy = new Board(source);
        Tetromino copiedCurrent = copy.getcurrentTetromino();
        Piece copiedBoardFirstTetromino = copy.getAllTetrominos().get(0);

        assertNotSame(source.getAllTetrominos(), copy.getAllTetrominos());
        assertNotSame(sourceCurrent, copiedCurrent);
        assertSame(copiedBoardFirstTetromino, copiedCurrent);
        assertNotSame(sourceCurrent.getCentre(), copiedCurrent.getCentre());
        assertEquals(sourceCurrent.getCentre(), copiedCurrent.getCentre());
        assertEquals(sourceCurrent.children.size(), copiedCurrent.children.size());

        for (int i = 0; i < sourceCurrent.children.size(); i++) {
            Mino sourceMino = sourceCurrent.children.get(i);
            Mino copiedMino = copiedCurrent.children.get(i);
            assertNotSame(sourceMino, copiedMino);
            assertNotSame(sourceMino.getPosition(), copiedMino.getPosition());
            assertEquals(sourceMino.getPosition(), copiedMino.getPosition());
        }

        double sourceCentreX = sourceCurrent.getCentre().getX();
        double sourceMinoX = sourceCurrent.children.get(0).getPosition().getX();
        copiedCurrent.getCentre().setX(sourceCentreX + 1);
        copiedCurrent.children.get(0).getPosition().setX(sourceMinoX + 1);

        assertEquals(sourceCentreX, sourceCurrent.getCentre().getX());
        assertEquals(sourceMinoX, sourceCurrent.children.get(0).getPosition().getX());
    }
}
