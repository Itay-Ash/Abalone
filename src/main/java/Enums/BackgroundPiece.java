package Enums;

import static Helpers.GlobalVars.srcPath;

public enum BackgroundPiece {
     /**
     Background images used for the board in the gui
     */
    SQUARE( 0,"file:" + srcPath + "\\Pictures\\Square.jpg"),
    SQUARE_WITH_A_HOLE(1, "file:" + srcPath + "\\Pictures\\Square_With_Hole.png"),
    SQUARE_WITH_A_HOLE_GLOW(2, "file:" + srcPath + "\\Pictures\\Square_With_Hole_Glow.png"),
    EMPTY(3, null);

    private final int val;
    private final String path;


    BackgroundPiece(int val, String path) {
        this.val = val;
        this.path = path;
    }

    public int getVal() {
        return val;
    }

    public String getPath() {
        return path;
    }
}
