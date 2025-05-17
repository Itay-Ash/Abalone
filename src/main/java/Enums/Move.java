package Enums;

public enum Move {
    /**
     Built on the concept of a neighbour matrix,
     each value is a different neighbour
     */
    BOTTOM (-1,0),
    BOTTOM_RIGHT (-1,1),
    BOTTOM_LEFT (-1,-1),
    RIGHT(0,1),
    LEFT(0,-1),
    TOP_RIGHT(1,1),
    TOP_LEFT(1,-1),
    TOP(1,0);

    private final int row;
    private final int col;


    Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Move findMove(int Row, int Col) { //O(1) returns the move value base on row and col values, default is top
        for(Move move : Move.values()) {
            if(move.getRow() == Row && move.getCol() == Col) {
                return move;
            }
        }
        return Move.TOP;
    }
}
