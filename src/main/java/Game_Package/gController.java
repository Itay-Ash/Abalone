package Game_Package;

import Enums.GameMode;
import Helpers.BoardSpot;
import Enums.BackgroundPiece;
import Enums.Piece;
import Helpers.MainMenu;
import javafx.stage.Stage;


public class gController {
    //the controller in the MVC pattern

    private gViewer view;
    private gManager mng;


    public gController(MainMenu mm, Stage stage, Piece pieceA, Piece pieceB, GameMode gm){
        view = new gViewer(initBoard(pieceA, pieceB),stage, pieceA, pieceB, gm == GameMode.AI_VS_PLAYER ? pieceB :pieceA,this,mm);
        mng = new gManager(initBoard(pieceA, pieceB), pieceA, pieceB, gm,this);
    }

    public void StartGame(Stage stage, Piece pieceA, Piece pieceB, GameMode gm){
        view.NewGame(initBoard(pieceA, pieceB),stage, pieceA, pieceB, gm == GameMode.AI_VS_PLAYER ? pieceB :pieceA);
        mng.NewGame(initBoard(pieceA, pieceB), pieceA, pieceB, gm,this);
    }

    public BoardSpot[][] initBoard(Piece pieceA, Piece pieceB){
        /*The board is always the same, and not all of the rows are the same length,
        so the board is manually by this function
        */
        BoardSpot[][] board = {
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0),new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE,0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0),new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE,0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0),new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 1), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 1), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 2), new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 2),new BoardSpot(pieceA, BackgroundPiece.SQUARE_WITH_A_HOLE, 2),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0) },
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 2),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 3),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 3),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 2),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE,0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 2),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 3),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 4),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 3),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 2),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE,2),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE,3),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE,3),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE,2),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE,1),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 0) ,  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),  new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE, 2),  new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE,2),   new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE,2),   new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE,1),  new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE_WITH_A_HOLE,0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0),  new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE, 1),   new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE,1),  new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE,1),   new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE,1),   new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE,0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE, 0),  new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE,0),  new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE, 0),   new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE, 0),   new BoardSpot(pieceB, BackgroundPiece.SQUARE_WITH_A_HOLE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)},
                {new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0), new BoardSpot(Piece.NONE, BackgroundPiece.SQUARE, 0)}};
        return board;
    }

    public void RefreshViewer(){
        //updating all the data in the viewer, causing it to update the gui
        view.setLblPieceACount(String.valueOf(mng.getAOut()));
        view.setLblPieceBCount(String.valueOf(mng.getBOut()));
        view.setTurnPiece(mng.getTurnPiece());
        view.FlipPiece();
        Piece WinP = mng.CheckWin();
        if (WinP != Piece.NONE)
            view.Win(WinP);
    }

    public void RefreshBoard(){
        //making the viewer look for changes in the board and change them in the gui
        view.ChangeBoard(mng.getBoard());
    }

    public void pieceClick(Piece pce, BackgroundPiece bcgp, int row, int col) {
        boolean moveMade = mng.AttemptToMakeAMove(pce, bcgp, row, col);
        //Even if a move wasn't made the glows shown on the board were cleared so we have to change the board
        RefreshBoard();
        if (moveMade) {
            RefreshViewer();
        }
    }
}
