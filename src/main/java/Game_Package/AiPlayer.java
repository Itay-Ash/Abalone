package Game_Package;

import Helpers.AiCheckFunction;
import Helpers.BoardSpot;
import Enums.Move;
import Enums.BackgroundPiece;
import Enums.Piece;
import java.util.ArrayList;
import static java.lang.Integer.MAX_VALUE;

public class AiPlayer {
    /**
     The class for the Abalone AI player
     @param THREE_TOGETHER_VALUE: the value used for measurement in the function ThreeTogetherCheck()
     @param SAFE_KNOCK_VALUE: the value used for measurement in the function SafeKnockCheck()
     @param AROUND_COUNT_VALUE: the value used for measurement in the function AroundCountCheck()
     @param LOCATION_WORTH: The param used to multiply the result of the function LocationCheck(), in the grading process
     @param OUT_WORTH: The param that is used to multiply the difference of the amount of knocked pieces of each player, in the grading process
     @param L_OUT_WORTH: The param that is used to multiply the amount of enemy knocked pieces, in the grading process
     @param MAX_DEPTH: The param used to set the max depth of the alphaBeta algorithm
     @param wPiece: The piece used by the AI player
     @param lPiece: The piece used by the opponent player
     @param bestRow: The row of the best move, found by the alphaBeta function
     @param bestCol: The column of the best move, found by the alphaBeta function
     @param bestMove: The best move, found by the alphaBet" function
     @param checkBestMove: Used to help find the best move, by the alphaBeta function, and maxMoves function
     @param board: The board gained from the gManager
     */
    private final int THREE_TOGETHER_VALUE = 1;
    private final int SAFE_KNOCK_VALUE = 3;
    private final int AROUND_COUNT_VALUE = 1;
    private final int LOCATION_WORTH =1;
    private final int OUT_WORTH = 10;
    private final int L_OUT_WORTH = 15;
    private final int MAX_DEPTH = 3;
    private Piece wPiece;
    private Piece lPiece;
    private int bestRow;
    private int bestCol;
    private Move bestMove;
    private Move checkBestMove;
    private gManager mng;
    private BoardSpot[][] board;


    public AiPlayer(Piece pieceA, Piece pieceB, gManager mng, boolean first) {
        this.bestRow = 0;
        this.bestCol = 0;
        this.bestMove = Move.BOTTOM_RIGHT;
        this.checkBestMove = Move.BOTTOM_RIGHT;
        if(first) {//if the AI is the first player to make a move
            this.wPiece = pieceA;
            this.lPiece = pieceB;
        }
        else{
            this.wPiece = pieceB;
            this.lPiece = pieceA;
        }
        this.mng = mng;
        this.board = mng.getBoard();
    }

    public void MakeAMove() {
        AlphaBeta(MAX_DEPTH, -MAX_VALUE, MAX_VALUE, wPiece);
        //giving value to bestMove, bestRow, bestCol
        mng.MakeAMove(bestMove, bestRow, bestCol);
    }

    public int AlphaBeta(int depth, int alpha, int beta, Piece TurnPiece) {
        if (depth == 0 || mng.getBOut() == 6 || mng.getBOut() == 6) {
            //if we reached the "end of the tree" or someone has won
            return Grade();
        }
        int Val;
        if (TurnPiece == wPiece) {
            int best = -MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j].getPiece() == wPiece) {//if the piece is the AI player's piece
                        Val = MaxMoves(i, j, depth, alpha, beta, TurnPiece);
                        alpha = Math.max(alpha, Val);
                        if (Val > best && depth == MAX_DEPTH) {//if a new best move was found
                            this.bestRow = i;
                            this.bestCol = j;
                            this.bestMove = checkBestMove;
                        }
                        best = Math.max(best, Val);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return best;
            }
        else{
            int worst = MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j].getPiece() == lPiece) {
                  //if the piece is the AI player's opponent piece
                        Val = MinMoves(i, j, depth, alpha, beta, TurnPiece);
                        beta = Math.min(beta, Val);
                        worst = Math.min(worst, Val);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return worst;
        }
    }

    public int MinMoves(int row, int col, int depth, int alpha,int beta ,Piece TurnPiece) {
        //looking at all the possible moves to find the best move for the opponent
        int minEval = MAX_VALUE;
        mng.possibleMoves(row, col);//indicator to all the moves possible
        ArrayList<Move> moves =new ArrayList<Move>();
        for(Move mv: Move.values()) {
            if(chkValidPosition(row  + mv.getRow() ,col + mv.getCol()) && board[row + mv.getRow()][col + mv.getCol()].getBcgPiece() == BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW) {
          //if the position was glowed by the possible moves function
                moves.add(mv);
                board[row + mv.getRow()][col + mv.getCol()].setBcgPiece(BackgroundPiece.SQUARE_WITH_A_HOLE);
          //setting the spot back to normal
            }
        }
        for(Move mv: moves) {//all the found moves
            int count = mng.MakeAMove(mv, row, col),eval;
                eval = AlphaBeta(depth - 1, alpha, beta, TurnPiece == wPiece ? lPiece : wPiece);
              //rerunning alpha beta to continue the tree
                mng.UndoMove(mv, row, col, count);
              //undoing the previous move, to keep the board the same
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
            }
        }
        return minEval;
    }

    public int MaxMoves(int row, int col, int depth, int alpha,int beta, Piece TurnPiece) {
        //looking at all the possible moves to find the best move for the opponent
        int maxEval = -MAX_VALUE;
        mng.possibleMoves(row, col);//indicator to all the moves possible
        ArrayList<Move> moves =new ArrayList<Move>();
        for(Move mv: Move.values()) {
            if(chkValidPosition(row  + mv.getRow() ,col + mv.getCol()) && board[row + mv.getRow()][col + mv.getCol()].getBcgPiece() == BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW) {
          //if the position was glowed by the possible moves function
                moves.add(mv);
                board[row + mv.getRow()][col + mv.getCol()].setBcgPiece(BackgroundPiece.SQUARE_WITH_A_HOLE);
          //setting the spot back to normal
            }
        }
        for(Move mv: moves) {//all the found moves
            int count = mng.MakeAMove(mv, row, col), eval;
                eval = AlphaBeta(depth - 1, alpha, beta, TurnPiece == wPiece ? lPiece : wPiece);
            //rerunning alpha beta to continue the tree
            mng.UndoMove(mv, row, col, count);
            //undoing the previous move, to keep the board the same
            alpha = Math.max(alpha, eval);
                if (eval > maxEval && depth == MAX_DEPTH)
                    checkBestMove = mv;
                maxEval = Math.max(maxEval, eval);
                if (beta <= alpha)
                    break;
        }
        return maxEval;
    }

    public int Grade() {
      //this function returns the grade of the board
        int wOut, lOut, grade =0;
      //the number of pieces that are out of the board, wOut means it's the AI's knocked pieces
      // lOut means it's the opponent's knocked pieces
        if(mng.getPieceA() == wPiece) {
            wOut = mng.getAOut();
            lOut = mng.getBOut();
        }
        else{
            wOut = mng.getBOut();
            lOut = mng.getAOut();
        }
        if(lOut == 6) grade= 10000;
        if(wOut == 6) grade= -10000;
        grade+= lOut * L_OUT_WORTH + (lOut - wOut) * OUT_WORTH + locationCheck() * LOCATION_WORTH +  AroundCountCheck() + safeKnockCheck()  + ThreeTogetherCheck();
        return grade;
    }

    public int locationCheck() {//O(nm)
        //giving a grade based on the location of the board's piece
        int Grade = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getBcgPiece() != BackgroundPiece.SQUARE && board[i][j].getPiece() != Piece.NONE) {
                    if(board[i][j].getPiece() == wPiece) Grade+= board[i][j].getLocationValue();//the piece belongs to the AI
                    else Grade-= board[i][j].getLocationValue();//the piece belongs to the opponent
                }
            }
        }
        return Grade;
    }

    public int ThreeTogetherCheck() {
        //giving a grade based on the amount of pieces that are three in a row
        AiCheckFunction aiCheckFunction = (piece, mv, row, col) -> {
          //the function used by the possible moves function

          //banning all moves that can cause reCounting of the pieces
            //the banes are based around the banned moves in the possibleMoves function
            if (row <= 4) {
                if (mv == Move.BOTTOM_RIGHT || mv == Move.BOTTOM || mv == Move.LEFT)
                    return 0;
            }
            if (row == 5){
                if (mv == Move.BOTTOM_LEFT || mv == Move.BOTTOM_RIGHT || mv == Move.BOTTOM || mv == Move.LEFT)
                    return 0;
            }
            else{
                if (mv == Move.BOTTOM_LEFT  || mv == Move.BOTTOM || mv == Move.LEFT)
                    return 0;
            }

            //repeating the first step in the process of the function canMoveBeMade in gManager
            int chkR,chkC;
             int rBuffer = mv.getRow(), cBuffer = mv.getCol();
            int wth=0;

            boolean isBreakMove = mv == Move.TOP || mv == Move.TOP_RIGHT || mv == Move.TOP_LEFT;
            boolean flag = false;
            Piece wPce = board[row][col].getPiece();

            /**Checking the amount of friendly pieces**/
            chkR = row;
            chkC = col;
            while(chkValidPosition(chkR,chkC) && board[chkR][chkC].getPiece().getVal() == (wPce.getVal())){
                wth++;
                chkR += rBuffer;
                chkC += cBuffer;
                if(flag) chkC--;
                if(chkR == 5 && isBreakMove) flag= true;
            }
            if(wth != 3) return 0;
            return THREE_TOGETHER_VALUE;
        };

        int Grade = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getBcgPiece() != BackgroundPiece.SQUARE && board[i][j].getPiece() != Piece.NONE) {
              //if a Piece was found
                    Piece p = board[i][j].getPiece();
                    int count = PossibleMovesCheck(p, i, j, aiCheckFunction);
                    if(p == wPiece) Grade+= count;//if the piece belongs to the AI
                    else Grade-= count;//if the piece belongs to the opponent
                }
            }
        }
        return Grade;
    }

    public int AroundCountCheck() {
        //checking the amount of friendly pieces around each piece
        AiCheckFunction aiCheckFunction = ( piece, mv, row, col) -> {
            if(chkValidPosition(row + mv.getRow() ,col + mv.getCol()) && board[row + mv.getRow()][col + mv.getCol()].getPiece() == piece)
          //if the same piece is present
                return 1;
            return 0;
        };
        int Grade = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getBcgPiece() != BackgroundPiece.SQUARE && board[i][j].getPiece() != Piece.NONE) {
                    Piece p= board[i][j].getPiece();
                    int count = PossibleMovesCheck(p, i, j, aiCheckFunction) * AROUND_COUNT_VALUE;
                    if(p == wPiece) Grade += count;//the piece belongs to the AI
                    else Grade -= count;//the piece belongs to the opponent
                }
            }
        }
        return Grade;
    }

    public int safeKnockCheck() {
        int Grade = 0;
        Piece p;
        AiCheckFunction aiCheckFunction = ( piece, mv, row, col) -> {
            if(chkValidPosition(row  + mv.getRow(),col + mv.getCol()) && board[row + mv.getRow()][col + mv.getCol()].getPiece() == piece)
                return 1;
            return 0;
        };
        //looking at the edges of the board
        for(int i=1; i<board[1].length -1; i++) {
            if(board[1][i].getPiece() != Piece.NONE) {
                p = board[1][i].getPiece();
                if(PossibleMovesCheck( p, 1, i, aiCheckFunction) == 0){
                    if(p == wPiece) Grade -= SAFE_KNOCK_VALUE;//the piece belongs to the AI
                    else Grade += SAFE_KNOCK_VALUE;//the piece belongs to the opponent
                }
            }
        }
        for (int i = 2; i < board.length -2; i++) {
            if(board[i][1].getPiece() != Piece.NONE){
                 p = board[i][1].getPiece();
                if(PossibleMovesCheck( p, i, 1, aiCheckFunction) == 0){
               //no friendly piece was found
                    if(p == wPiece) Grade -= SAFE_KNOCK_VALUE;//the piece belongs to the AI
                    else Grade += SAFE_KNOCK_VALUE;//the piece belongs to the opponent
                }
            }
            if(board[i][board[i].length - 2].getPiece() != Piece.NONE){
                p = board[i][board[i].length - 2].getPiece();
                if(PossibleMovesCheck( p, i, board[i].length - 2, aiCheckFunction) == 0){
                    //no friendly piece was found
                    if(p == wPiece) Grade -= SAFE_KNOCK_VALUE;//the piece belongs to the AI
                    else Grade += SAFE_KNOCK_VALUE;//the piece belongs to the opponent
                }
            }
        }
        for(int i=1; i<board[board.length - 2].length - 1; i++) {
            if(board[board.length - 2][i].getPiece() != Piece.NONE) {
                p = board[board.length - 2][i].getPiece();
                if(PossibleMovesCheck( p, board.length - 2, i, aiCheckFunction) == 0){
                    //no friendly piece was found
                    if(p == wPiece) Grade -= SAFE_KNOCK_VALUE;//the piece belongs to the AI
                    else Grade += SAFE_KNOCK_VALUE;//the piece belongs to the opponent
                }
            }
        }
        return Grade;
    }

    public int PossibleMovesCheck(Piece piece, int row, int col, AiCheckFunction func) {
        int count = 0;
        Move bannedMove1, bannedMove2;
        //in each part of the board, only 6/8 moves can be used
        if(row <= 4){
            bannedMove1 = Move.TOP_LEFT;
            bannedMove2 = Move.BOTTOM_RIGHT;
        }
        else if(row == 5){
            bannedMove1 = Move.TOP_RIGHT;
            bannedMove2 = Move.BOTTOM_RIGHT;
        }
        else{
            bannedMove1 = Move.TOP_RIGHT;
            bannedMove2 = Move.BOTTOM_LEFT;
        }
        for(Move mv: Move.values()){
            if(mv != bannedMove1 && mv != bannedMove2)
                count += func.check(piece, mv ,row , col);//calculate the sum of running the AI check function
        }
        return count;
    }

    public boolean chkValidPosition(int row, int col){
      //If the position is valid
        if( row< 0 || col < 0)
            return false;
        if(row >= board.length || col >= board[row].length )
            return false;
        return true;
    }
}
