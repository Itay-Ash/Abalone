package Helpers;

import Enums.Move;
import Enums.Piece;

public interface AiCheckFunction {
    //an interface used by the AI functions
    public int check(Piece piece, Move mv, int row, int col);
}
