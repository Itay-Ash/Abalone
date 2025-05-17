package Enums;

import static Helpers.GlobalVars.srcPath;

public enum GameMode {
    /**
     Set's the game's game mode: Ai vs player/Player vs player...
     @param path: the game mode icon path
     */
    PLAYER_VS_PLAYER(0,"file:" + srcPath + "\\Pictures\\Player_Vs_Player.png"),
    PLAYER_VS_AI(1,"file:" + srcPath + "\\Pictures\\Player_Vs_Bot.png"),
    AI_VS_PLAYER(2,"file:" + srcPath + "\\Pictures\\Ai_Vs_Player.png");

    private int val;
    private String path;


    GameMode(int val, String path) {
        this.val = val;
        this.path = path;
    }

    public int getVal() {
        return val;
    }

    public String getPath() {
        return path;
    }

    public GameMode GameModeByString(String gameMode) {
        switch (gameMode.toLowerCase()) {
            case "player vs player":
            case "player_vs_player":
                return PLAYER_VS_PLAYER;
            case "player vs ai":
            case "player_vs_ai":
                return PLAYER_VS_AI;
            case "ai vs player":
            case "ai_vs_player":
                return AI_VS_PLAYER;
            default:
                return null;
        }
    }
}
