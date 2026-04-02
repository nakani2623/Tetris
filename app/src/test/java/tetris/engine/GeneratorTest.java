// package tetris.engine;

// import org.junit.jupiter.api.Test;
// import tetris.engine.type.TetrominoType;

// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// public class GeneratorTest {
//     Generator generator = new Generator();

//     /**
//      * Checks whether bag7 creates right number of Tetromino objects
//      */
//     @Test
//     void bag7Shape() {
//         assertEquals(7, generator.bag7().size());
//     }

//     /**
//      * Checks whether bag7 creates each type of Tetromino exactly once
//      */
//     @Test
//     void bag7IsReal() {
//         List<Tetromino> bag = generator.bag7();
//         Set<TetrominoType> presentingTypes = new HashSet<TetrominoType>();
//         for (Tetromino tetromino : bag)
//             presentingTypes.add(tetromino.type);

//         assertEquals(7, presentingTypes.size());
//     }

// //    /**
// //     * Checks whether the order of Tetrominos is random
// //     */
// //    @Test
// //    void bag7RandOrder() {
// //        //TODO
// //    }
// }
