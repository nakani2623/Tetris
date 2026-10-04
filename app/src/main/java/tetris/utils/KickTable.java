package tetris.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tetris.engine.type.TetrominoType;

public class KickTable {
    // I Tetromino Wall Kick Data

    private static final ArrayList<Point> I_0_R = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(-2, 0),
        new Point(1, 0),
        new Point(-2, 1),
        new Point(1, -2)
    ));

    private static final ArrayList<Point> I_R_0 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(2, 0),
        new Point(-1, 0),
        new Point(2, -1),
        new Point(-1, 2)
    ));

    private static final ArrayList<Point> I_R_2 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(-1, 0),
        new Point(2, 0),
        new Point(-1, -2),
        new Point(2, 1)
    ));

    private static final ArrayList<Point> I_2_R = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(1, 0),
        new Point(-2, 0),
        new Point(1, 2),
        new Point(-2, -1)
    ));

    private static final ArrayList<Point> I_2_L = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(2, 0),
        new Point(-1, 0),
        new Point(2, -1),
        new Point(-1, 2)
    ));

    private static final ArrayList<Point> I_L_2 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(-2, 0),
        new Point(1, 0),
        new Point(-2, 1),
        new Point(1, -2)
    ));

    private static final ArrayList<Point> I_L_0 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(1, 0),
        new Point(-2, 0),
        new Point(1, 2),
        new Point(-2, -1)
    ));

    private static final ArrayList<Point> I_0_L = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(-1, 0),
        new Point(2, 0),
        new Point(-1, -2),
        new Point(2, 1)
    ));

    private static final ArrayList<ArrayList<Point>> I_0 = new ArrayList<>(Arrays.asList(
        null, // self
        I_0_R,
        null, // 180
        I_0_L
    ));

    private static final ArrayList<ArrayList<Point>> I_R = new ArrayList<>(Arrays.asList(
        I_R_0,
        null, // self
        I_R_2,
        null // 180
    ));

    private static final ArrayList<ArrayList<Point>> I_2 = new ArrayList<>(Arrays.asList(
        null, // 180
        I_2_R,
        null, // self
        I_2_L
    ));

    private static final ArrayList<ArrayList<Point>> I_L = new ArrayList<>(Arrays.asList(
        I_L_0,
        null, // 180
        I_L_2,
        null // self
    ));

    public static final ArrayList<ArrayList<ArrayList<Point>>> I_table =
        new ArrayList<>(List.of(I_0, I_R, I_2, I_L));
    // J, L, S, T, Z Tetromino Wall Kick Data
    private static ArrayList<Point> JLSTZ_0_R = new ArrayList<Point>(List.of(
        new Point(0, 0),
        new Point(-1, 0),
        new Point(-1, -1),
        new Point(0, 2),
        new Point(-1, 2)
    ));
    private static final ArrayList<Point> JLSTZ_0_L = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, -1),
        new Point(0, 2),
        new Point(1, 2)
    ));

    private static final ArrayList<Point> JLSTZ_L_0 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(0, -2),
        new Point(1, -2)
    ));

    private static final ArrayList<Point> JLSTZ_L_2 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(0, -2),
        new Point(1, -2)
    ));

    private static final ArrayList<Point> JLSTZ_R_0 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(-1, 0),
        new Point(-1, -1),
        new Point(0, 2),
        new Point(-1, 2)
    ));

    private static final ArrayList<Point> JLSTZ_R_2 = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(-1, 0),
        new Point(-1, -1),
        new Point(0, 2),
        new Point(-1, 2)
    ));

    private static final ArrayList<Point> JLSTZ_2_L = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(-1, 0),
        new Point(-1, 1),
        new Point(0, -2),
        new Point(-1, -2)
    ));

    private static final ArrayList<Point> JLSTZ_2_R = new ArrayList<>(List.of(
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, -1),
        new Point(0, 2),
        new Point(1, 2)
    ));

    private static final ArrayList<ArrayList<Point>> JLSTZ_0 = new ArrayList<>(Arrays.asList(
        null, // self
        JLSTZ_0_R,
        null, // 180
        JLSTZ_0_L
    ));

    private static final ArrayList<ArrayList<Point>> JLSTZ_R = new ArrayList<>(Arrays.asList(
        JLSTZ_R_0,
        null, // self
        JLSTZ_R_2,
        null // 180
    ));

    private static final ArrayList<ArrayList<Point>> JLSTZ_2 = new ArrayList<>(Arrays.asList(
        null, // 180
        JLSTZ_2_R,
        null, // self
        JLSTZ_2_L
    ));

    private static final ArrayList<ArrayList<Point>> JLSTZ_L = new ArrayList<>(Arrays.asList(
        JLSTZ_L_0,
        null, // 180
        JLSTZ_L_2,
        null // self
    ));

    private static ArrayList<ArrayList<ArrayList<Point>>> JLSTZ_table = new ArrayList<>(List.of(
        JLSTZ_0,
        JLSTZ_R,
        JLSTZ_2,
        JLSTZ_L
    ));


    public static List<Point> getKickList(TetrominoType type, int stateBeforeRotation, int stateAfterRotation) {
        ArrayList<ArrayList<ArrayList<Point>>> kickTable;
        if (type == TetrominoType.I) {
            // I table
            kickTable = I_table;
        }
        else {
            // JLSTZ table
            kickTable = JLSTZ_table;
        }

        return kickTable.get(stateBeforeRotation).get(stateAfterRotation);
        
    }
}
