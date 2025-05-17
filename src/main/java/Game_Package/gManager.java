package Game_Package;

import Enums.GameMode;
import Helpers.BoardSpot;
import Enums.BackgroundPiece;
import Enums.Move;
import Enums.Piece;


public class gManager {
    /**The manager in the MVC model
     @param board: the board that the game is played on
     @param ao: the AI player
     @param pieceA: the piece of the first player
     @param pieceB: the piece of the second player
     @param TurnPiece: The piece that can make be moved now, pieceA/pieceB
     @param gm: the game mode selected: AI vs Player/Player vs Player...
     @param Aout: the pieces of the pieceA player, that were knocked out of the board
     @param Bout: the pieces of the pieceB player, that were knocked out of the board
     @param selR: the row selected to make the next move(if the value is -1, no row was selected)
     @param selC: the column selected to make the next move(if the value is -1, no column was selected)
    */
    private BoardSpot[][] board;
    private AiPlayer ai;
    private Piece pieceA;
    private Piece pieceB;
    private Piece TurnPiece;
    private GameMode gm;
    private int AOut;
    private int BOut;
    private int selR;
    private int selC;
    private gController ctrl;


    public gManager(BoardSpot[][] board, Piece pieceA, Piece pieceB, GameMode gm, gController ctrl) {
        NewGame(board, pieceA, pieceB, gm, ctrl);
    }

    public void NewGame(BoardSpot[][] board, Piece pieceA, Piece pieceB, GameMode gm, gController ctrl){
        //initializing all values for a new game
        this.board = board;
        this.pieceA = pieceA;
        this.pieceB = pieceB;
        this.AOut = 0;
        this.BOut = 0;
        this.TurnPiece = this.pieceA;
        this.selR = -1;
        this.selC = -1;
        this.gm = gm;
        this.ctrl = ctrl;
        if(gm == GameMode.AI_VS_PLAYER) {//if the ai moves first
            this.ai = new AiPlayer(this.pieceA, this.pieceB, this, true);
            ai.MakeAMove();
            ctrl.RefreshBoard();
            this.TurnPiece = this.pieceB;
        }
        else if(gm == GameMode.PLAYER_VS_AI)//if the ai moves second
            this.ai = new AiPlayer(this.pieceA, this.pieceB, this, false);
        else this.ai = null;
    }

    public boolean chkValidPosition(int row, int col){
        //is the position in the borders of the board
        if( row< 0 || col < 0)
            return false;
        if(row >= board.length || col >= board[row].length )
            return false;
        return true;
    }

     public boolean canMoveBeMade(Move mv, int row, int col) {
     //because of the way the board is built, after passing row 5 into another row, using the move the same way
     // will lead to the wrong place, which is why the col is decreased by 1 every time, and the variables:
     // isBreakMove and passedRow5 (both boolean) are created
     if(!chkValidPosition(col,row) && board[row][col].getPiece() == Piece.NONE && (!board[row][col].getBcgPiece().equals(BackgroundPiece.SQUARE_WITH_A_HOLE) || !board[row][col].getBcgPiece().equals(BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW)))
         //if the starting spot is not valid
         return false;
     int chkR,chkC;
     int rBuffer = mv.getRow(), cBuffer = mv.getCol();
     int ops = 0, wth=0;

     boolean isBreakMove = mv == Move.BOTTOM || mv == Move.BOTTOM_RIGHT || mv == Move.BOTTOM_LEFT || mv == Move.TOP || mv == Move.TOP_RIGHT || mv == Move.TOP_LEFT;
     //does the move change your row
     boolean passedRow5 = false;//if we have passed the fifth row, we have to change our col value accordingly each turn
     Piece wPce = board[row][col].getPiece();// the selcted piece
     Piece oPce = wPce == pieceA ? pieceB:pieceA;// the opponent piece

    /**Checking the amount of friendly pieces**/
     chkR = row;
     chkC = col;
         while(chkValidPosition(chkR,chkC) && board[chkR][chkC].getPiece().getVal() == (wPce.getVal()) && wth < 4){
     wth++;
     chkR += rBuffer;
     chkC += cBuffer;
     if(passedRow5) chkC--;//if we have passed the fifth row, we have to change our col value accordingly each turn
     if(chkR == 5 && isBreakMove) passedRow5= true;
     }
     if(wth >3 || (chkValidPosition(chkR,chkC) && board[chkR][chkC].getBcgPiece() != BackgroundPiece.SQUARE_WITH_A_HOLE) || !chkValidPosition(chkR,chkC)) return false;
     //if the player is trying to move more than 3 pieces or knock his own pieces off the map

    /**Checking the amount of enemy pieces**/
    while(chkValidPosition(chkR,chkC) && board[chkR][chkC].getPiece().getVal() == oPce.getVal() && ops <3){
     ops++;
     chkR += rBuffer;
     chkC += cBuffer;
        if(passedRow5) chkC--;//if we have passed the fifth row, we have to change our col value accordingly each turn
        if(chkR == 5 && isBreakMove) passedRow5= true;
     }
    if(chkValidPosition(chkR,chkC) && board[chkR][chkC].getPiece() != Piece.NONE) return false;
    //if there is a piece after the counted pieces
     if(wth <= ops && ops!=0) return false;
     //if the amount ratio isnt: 3:1 or 3:2 or 2:1 the move cannot be made
     return true;
     }

    public void possibleMoves(int row, int col) {
        ClearGlows();
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
                if(canMoveBeMade(mv,row,col))
                    //if the move can be made
                    board[row + mv.getRow()][col + mv.getCol()].setBcgPiece(BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW);
            //showing the player the move is possible
        }
    }

    public boolean AttemptToMakeAMove(Piece pce, BackgroundPiece bcgp, int row, int col){
        ClearGlows();
        if(bcgp == BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW) {
            //if a move that has been checked as possible is selected
            Move mv = Move.TOP;
            MakeAMove(mv.findMove( row - selR, col - selC), selR, selC);
            this.TurnPiece = this.TurnPiece == pieceA ? pieceB : pieceA;//changing the turn piece
            selR = -1;
            selC = -1;
            //unselecting the selected piece
            if(gm == GameMode.AI_VS_PLAYER || gm == GameMode.PLAYER_VS_AI) {
                //if needed, the AI will make a move
                ctrl.RefreshViewer();
                ai.MakeAMove();
                this.TurnPiece = this.TurnPiece == pieceA ? pieceB : pieceA;
            }
            return true;
        }
        else if(pce == TurnPiece && !(row == selR && col == selC)) {
            //if a piece that is not the previously selected piece is clicked
            possibleMoves(row,col);
            selR = row;
            selC = col;
        }else{
            //unselecting the previously selected piece
            selR = -1;
            selC = -1;
        }
        return false;
    }

    public int MakeAMove(Move mv, int row, int col) {
        //the need for a passRow5 flag, and to decrease the col value are explained in the function: canMoveBeMade
        boolean passedRow5 = false;
        boolean isBreakMove = mv == Move.BOTTOM || mv == Move.BOTTOM_RIGHT || mv == Move.BOTTOM_LEFT || mv == Move.TOP || mv == Move.TOP_RIGHT || mv == Move.TOP_LEFT;
        Piece currPiece = Piece.NONE, temp;
        int chkRow= row, chkCol= col;
        int amount =0,count;
        /**Checking how many enemy pieces need to be moved a spot forward**/
        while(chkValidPosition(chkRow,chkCol) && board[chkRow][chkCol].getPiece() != Piece.NONE){
            amount++;
            chkRow = chkRow + mv.getRow();
            chkCol = chkCol + mv.getCol();
            if(passedRow5) chkCol--;
            if(chkRow == 5 && isBreakMove) passedRow5= true;
        }
        count = amount;
        passedRow5 = false;
        /**Moving all of the pieces forward**/
        while(chkValidPosition(row,col) &&  board[row][col].getBcgPiece().getVal() == BackgroundPiece.SQUARE_WITH_A_HOLE.getVal() && amount > 0){
            amount--;
            temp = board[row][col].getPiece();
            board[row][col].setPiece(currPiece);
            currPiece = temp;
            row = row + mv.getRow();
            col = col + mv.getCol();
            if(passedRow5) col--;
            if(row == 5 && isBreakMove) passedRow5= true;
        }
        if(chkValidPosition(row,col) && board[row][col].getBcgPiece().getVal() == BackgroundPiece.SQUARE_WITH_A_HOLE.getVal())
            //if the piece was not knocked out of the board
            board[row][col].setPiece(currPiece);
        else{
            if(currPiece == pieceA) AOut++;
            else if(currPiece == pieceB) BOut++;
        }
        return count;
    }

    public void UndoMove(Move mv, int row, int col, int count){
        //the need for a passRow5 flag, and to decrease the col value are explained in the function: canMoveBeMade
        boolean passedRow5 = false;
        boolean isBreakMove = mv == Move.BOTTOM || mv == Move.BOTTOM_RIGHT || mv == Move.BOTTOM_LEFT || mv == Move.TOP || mv == Move.TOP_RIGHT || mv == Move.TOP_LEFT;
        boolean first = false;
        Piece currPiece = Piece.NONE, lastPiece = Piece.NONE, firstPiece = Piece.NONE;
        int lastRow = row, lastCol = col;
        while (count > 0) {
            if (lastPiece != currPiece) {//if the piece is not the same as the last piece
                if(!first) {
                    //saving the first piece in case we will need to bring back a knocked piece
                    firstPiece = currPiece;
                    first = true;
                }
                //if the pieces are diffrent, we need to brig the current piece one spot backwards
                board[lastRow][lastCol].setPiece(currPiece);
            }
            lastPiece = currPiece;
            lastRow = row;
            lastCol = col;
            row += mv.getRow();
            col += mv.getCol();
            if (passedRow5) col--;
            if (row == 5 && isBreakMove) passedRow5 = true;
            if(chkValidPosition(row,col)) currPiece = board[row][col].getPiece();
            else currPiece = Piece.NONE;
            count--;
        }
        if (!chkValidPosition(row, col) || board[row][col].getBcgPiece() == BackgroundPiece.SQUARE) {
            //if we need to place a piece that was knocked out of the board
            board[lastRow][lastCol].setPiece(firstPiece == pieceA ? pieceB : pieceA);
            if (firstPiece == pieceA) BOut--;
            else AOut--;
        } else {
            if (lastPiece != currPiece) {
                //if there is no need to bring back a knoced piece, we will place a piece based on currPiece value
                board[lastRow][lastCol].setPiece(currPiece);
            }
            //removing the extra piece left
            board[row][col].setPiece(Piece.NONE);
        }

    }

     public void ClearGlows(){ // O(n)
        //clearing all the glows on the board(Background.SQUARE_WITH_A_HOLE_GLOW
        for(int i=0; i< board.length; i++){
            for(int j=0; j< board[i].length; j++)
                if(board[i][j].getBcgPiece().getVal() == BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW.getVal())
                    board[i][j].setBcgPiece(BackgroundPiece.SQUARE_WITH_A_HOLE);
        }
     }

    public Piece CheckWin(){
        //the goal of the game is to knock 6 of the oppent's pieces out of the board
        if(AOut == 6) return pieceB;
        else if (BOut == 6) return pieceA;
        else return Piece.NONE;
    }

    public int getAOut() {
        return AOut;
    }

    public int getBOut() {
        return BOut;
    }

    public BoardSpot[][] getBoard() {
        return board;
    }

    public Piece getTurnPiece() {
        return TurnPiece;
    }

    public Piece getPieceA() {
        return pieceA;
    }

    public Piece getPieceB() {
        return pieceB;
    }
}
