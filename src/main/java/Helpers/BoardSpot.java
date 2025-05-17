package Helpers;

import Enums.BackgroundPiece;
import Enums.Piece;

public class BoardSpot {
    private Piece piece;
    private BackgroundPiece bcgPiece;
    private int locationValue;

    public BoardSpot(Piece piece, BackgroundPiece bcgPiece, int locationValue) {
        this.piece = piece;
        this.bcgPiece = bcgPiece;
        this.locationValue = locationValue;
    }

    public Piece getPiece(){
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public BackgroundPiece getBcgPiece() {
        return bcgPiece;
    }

    public void setBcgPiece(BackgroundPiece bcgPiece) {
        this.bcgPiece = bcgPiece;
    }

    public int getLocationValue() {
        return locationValue;
    }

    public void setLocationValue(int locationValue) {
        this.locationValue = locationValue;
    }
}
