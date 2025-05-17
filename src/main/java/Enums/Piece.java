package Enums;

import static Helpers.GlobalVars.srcPath;

public enum Piece {
    /**
     Holds all the pictures needed for a game piece.
     @param gamePath: The path to the game piece shown in the turn bar.
     @param selectedPath: The path to the game piece shown in the board.
     @param countPath: The path to the counter of the pieces that were knocked out of the board.
     */
    NONE(0,null,null, null),
    BLUE(1,"file:" + srcPath + "\\Pictures\\Game_Blue_Ball.png", "file:" + srcPath + "\\Pictures\\Blue_Ball.png", "file:" + srcPath + "\\Pictures\\Blue_Count.png"),
    RED(2,"file:" + srcPath + "\\Pictures\\Game_Red_Ball.png", "file:" + srcPath + "\\Pictures\\Red_Ball.png",  "file:" + srcPath + "\\Pictures\\Red_Count.png"),
    YELLOW(3,"file:" + srcPath + "\\Pictures\\Game_Yellow_Ball.png", "file:" + srcPath + "\\Pictures\\Yellow_Ball.png", "file:" + srcPath + "\\Pictures\\Yellow_Count.png"),
    GREEN(4,"file:" + srcPath + "\\Pictures\\Game_Green_Ball.png", "file:" + srcPath + "\\Pictures\\Green_Ball.png", "file:" + srcPath + "\\Pictures\\Green_Count.png");

    private final int val;
    private final String gamePath;
    private final String designPath;
    private final String countPath;

    Piece(int val, String gamePath, String designPath, String countPath) {
        this.val = val;
        this.gamePath = gamePath;
        this.designPath = designPath;
        this.countPath = countPath;
    }

    public Piece PieceByString(String s) {
        //returns a piece based on a string
        s=s.toLowerCase();
        switch (s) {
            case "blue":
                return Piece.BLUE;
            case "red":
                return Piece.RED;
            case "yellow":
                return Piece.YELLOW;
            case "green":
                return Piece.GREEN;
            default:
                return Piece.NONE;
        }
    }

    public int getVal() {
        return this.val;
    }

    public String getGamePath() {
        return this.gamePath;
    }

    public String getDesignPath() {
        return this.designPath;
    }

    public String getCountPath() {
        return this.countPath;
    }

}
