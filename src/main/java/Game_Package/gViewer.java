package Game_Package;

import Helpers.BoardSpot;
import Enums.BackgroundPiece;
import Enums.Piece;
import Helpers.MainMenu;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

import static Helpers.GlobalVars.srcPath;


public class gViewer extends Application {
    /***
     The viewer in the MVC model.
     @param pieceA: the piece of the first player
     @param pieceB: the piece of the second player
     @param TurnPiece: the piece that can make a move right now
     @param boardPane: the board showcased in the viewer, the board is a vBox, filled by hBoxes
     @param vwTurnBall: the piece image that shows which player's turn it is
     @param lblPieceACount: the label that shows the number of knocked pieces of the first player
     @param lblPieceBCount: the label that shows the number of knocked pieces of the second player
     */
    private MainMenu mainMenu;
    private gController ctrl;
    private Stage stage;
    private Scene scene;
    private Piece pieceA;
    private Piece pieceB;
    private Piece TurnPiece;
    private VBox boardPane;
    private ImageView vwTurnBall;
    private Label lblPieceACount;
    private Label lblPieceBCount;

    public gViewer(BoardSpot[][] board, Stage stage , Piece pieceA, Piece pieceB, Piece TurnPiece, gController ctrl ,MainMenu mm) {
        this.boardPane = new VBox();
        this.ctrl = ctrl;
        this.mainMenu = mm;
        NewGame(board, stage, pieceA, pieceB, TurnPiece);
    }

    public void NewGame(BoardSpot[][] board, Stage stage , Piece pieceA, Piece pieceB, Piece TurnPiece){
        this.pieceA = pieceA;
        this.pieceB = pieceB;
        this.TurnPiece = TurnPiece;
        this.stage = stage;
        try {
            start(stage);
        } catch (Exception e) {}
        DrawBoard(board);
    }

    public void DrawBoard(BoardSpot[][] board) {
        //completing deleting the current board and creating a new one
        boardPane.getChildren().clear();
        for (int i = 0; i < board.length; i++) {
            HBox hRow = new HBox();
            for (int j = 0; j < board[i].length; j++) {
                Piece pce = board[i][j].getPiece();
                BackgroundPiece bcgp = board[i][j].getBcgPiece();
                Cell cell = new Cell(pce, bcgp, i, j, scene, ctrl);
                //anonymous class that appears at the end of this one, it's the cell of each spot on the gui board
                hRow.getChildren().add(cell);
            }
            hRow.setAlignment(Pos.CENTER);
            boardPane.getChildren().add(hRow);
        }
    }

    public void ChangeBoard(BoardSpot[][] newBoard) {
        //looking for differences in the current board and newBoard, and changing the current board accordingly
        for (int i = 0; i < newBoard.length; i++) {
            HBox h = (HBox) boardPane.getChildren().get(i);
            for (int j = 0; j < newBoard[i].length; j++) {
                Cell c = (Cell) h.getChildren().get(j);
                if (newBoard[i][j].getPiece() != c.getPiece())
                    c.setPiece(newBoard[i][j].getPiece());
                if (newBoard[i][j].getBcgPiece() != c.getBcgp())
                    c.setBcgp(newBoard[i][j].getBcgPiece());
            }
        }
    }

    public void FlipPiece(){
        //changing the turn piece showcased in the gui
        RotateTransition rt = new RotateTransition(Duration.millis(700), vwTurnBall);
        rt.setCycleCount(1);
       rt.setByAngle(180);
        rt.setAxis(new Point3D(0,5,0));
        rt.play();
        Thread changeBallThread = new Thread(() -> {
            try {
                Thread.sleep(350);//half of the animation duration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Image imgBall = new Image(TurnPiece.getDesignPath());//changing the ball in the middle of the animation
            vwTurnBall.setImage(imgBall);
        });
           changeBallThread.start();
    }

    public void Win(Piece p) {
        //Showing the win window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("We have a winner!");
        alert.setHeaderText(p.toString() + " WINS!");
        alert.setContentText("You may go back to the main menu or exit");
        alert.setGraphic(new ImageView(p.getDesignPath()));//Showing the piece of the winning player
        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();//getting the stage of the dialog
        dialogStage.getIcons().add(new Image("file:" + srcPath + "\\Pictures\\Abalone Icon.png"));
        //setting the dialog's icon to the game's icon
        ButtonType btnMainMenu = new ButtonType("Main Menu");
        ButtonType btnCancel = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnMainMenu, btnCancel);//removing the cancel button
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnMainMenu) {//if the user clicked on "Main Menu"
            Platform.setImplicitExit(false);//making sure java won't close the application during the shift to the main menu
            stage.setMaximized(false);
            //if maximizing won't be turned off, java will look at the new window as maximized
            //without the main menu being maximized by the code/user, and it will cause the maximizing button to bug out
            mainMenu.start(stage);
        } else {
            Platform.exit();//closing the game completely
        }
    }

    public void setLblPieceACount(String st) {
        this.lblPieceACount.setText(st);
    }

    public void setLblPieceBCount(String st) {
        this.lblPieceBCount.setText(st);
    }

    public void setTurnPiece(Piece turnPiece) {
        TurnPiece = turnPiece;
    }

    @Override
    public void start(Stage stage) {
        Platform.setImplicitExit(true);
        /**Main Parts**/
        BorderPane root = new BorderPane();
        root.setCenter(boardPane);
        BorderPane.setAlignment(boardPane, Pos.CENTER);


        Image imgBall = new Image(TurnPiece.getDesignPath());
        vwTurnBall = new ImageView(imgBall);
        vwTurnBall.setFitWidth(50);
        vwTurnBall.setFitHeight(50);
        //showing the ball in the "Your turn" label

        Label lblTurn = new Label();
        String turnBar = "file:" + srcPath + "\\Pictures\\Turn_Bar.png";
        turnBar = turnBar.replace("\\", "/");
        lblTurn.setStyle("-fx-background-image:url(" + turnBar + ");-fx-background-repeat: stretch;  -fx-background-position: center center; -fx-background-size:stretch");
        lblTurn.setGraphic(vwTurnBall);
        lblTurn.setPrefWidth(210);
        lblTurn.setPrefHeight(70);
        root.setLeft(lblTurn);
        //lblTurn shows the "Your turn" background,this way each time we change the vwTurnBall variable, we can change the image of the ball

        HBox CountsBox = new HBox();
        CountsBox.setMaxHeight(100);
        CountsBox.setSpacing(15);
        //contains both of the counting images

        VBox A_Count = new VBox();
        //Showing the amount of pieces of pieceA that were knocked out
        A_Count.setAlignment(Pos.CENTER);
        A_Count.setStyle("-fx-background-image:url(" + pieceA.getCountPath().replace('\\', '/') + ");-fx-background-repeat: stretch;  -fx-background-position: center center; -fx-background-size:stretch");
        lblPieceACount= new Label("0" );
        lblPieceACount.setStyle("-fx-font-size: 30px; -fx-text-fill: White;");
        A_Count.getChildren().add(lblPieceACount);
        A_Count.setPrefWidth(100);
        CountsBox.getChildren().add(A_Count);

        VBox B_Count = new VBox();
        //Showing the amount of pieces of pieceB that were knocked out
        B_Count.setAlignment(Pos.CENTER);
        B_Count.setStyle("-fx-background-image:url(" + pieceB.getCountPath().replace('\\', '/')+ ");-fx-background-repeat: stretch;  -fx-background-position: center center; -fx-background-size:stretch");
        lblPieceBCount = new Label("0" );
        lblPieceBCount.setStyle("-fx-font-size: 30px; -fx-text-fill: White;");
        B_Count.getChildren().add(lblPieceBCount);
        B_Count.setPrefWidth(100);
        CountsBox.getChildren().add(B_Count);

        root.setRight(CountsBox);


        String gameBcgPath = "file:" + srcPath + "\\Pictures\\Game_Background.jpg";
        gameBcgPath = gameBcgPath.replace('\\', '/');
        root.setStyle("-fx-background-image:url(" + gameBcgPath + ");-fx-background-repeat: stretch;  -fx-background-position: center center; -fx-background-size:stretch");
        //setting the background of the game

        scene = new Scene(root, 1200, 600);
        stage.setTitle("Abalone");
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(1200);
        stage.setMaximized(true);
        stage.show();
    }

    class Cell extends HBox {
        /**
         * Representing a spot in the board
         @param label: the only component in the cell
         @param piece: the piece shown in the label
         @param bcgp: the background of the label
         */
        private Label lblPiece;
        private Piece piece;
        private BackgroundPiece bcgp;

        public Cell(Piece pce, BackgroundPiece bcgpic, int row, int col, Scene scene, gController ctrl) {
            this.piece = pce;
            this.bcgp = bcgpic;
            lblPiece = new Label();
            if (bcgp != BackgroundPiece.EMPTY) {
          //if the spot is not meant to be empty
                this.setPrefHeight(50);
                this.setPrefWidth(50);
                this.setAlignment(Pos.CENTER);
                String bcgPath = bcgp.getPath();
                bcgPath = bcgPath.replace('\\', '/');
                this.setStyle("-fx-background-image:url(" + bcgPath + ");-fx-background-repeat: stretch;  -fx-background-position: center center; -fx-background-size:stretch");
                this.setOnMouseClicked(e -> {
                //to make the spot easier to hit, and to be able to clear glows on all board spots, the listener is added to the cell
                    if (piece == TurnPiece || bcgp.getVal() == BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW.getVal())
                  //if the the spot can be used for a move
                        scene.setCursor(Cursor.HAND);
                     ctrl.pieceClick(this.piece, this.bcgp, row, col);
                });
                lblPiece.setOnMouseEntered(e -> {
                    if (piece == TurnPiece || bcgp.getVal() == BackgroundPiece.SQUARE_WITH_A_HOLE_GLOW.getVal())
                  //if the the spot can be used for a move
                        scene.setCursor(Cursor.HAND);
                });
                lblPiece.setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));
            }
            if (piece.getVal() != Piece.NONE.getVal()) {
          //if the spot has a piece in it
                String piecePath = piece.getGamePath();
                Image img = new Image(piecePath);
                ImageView view = new ImageView(img);
                view.setFitWidth(28.75);
                view.setFitHeight(31.25);
                lblPiece.setGraphic(view);
            }
            this.getChildren().add(lblPiece);

        }

        public Label getLblPiece() {
            return lblPiece;
        }

        public void setLblPiece(Label lblPiece) {
            this.lblPiece = lblPiece;
        }

        public Piece getPiece() {
            return piece;
        }

        public void setPiece(Piece piece) {
            this.piece = piece;
            if (piece.getVal() != Piece.NONE.getVal()) {
                String shapePath = piece.getGamePath();
                Image img = new Image(shapePath);
                ImageView view = new ImageView(img);
                view.setFitWidth(28.75);
                view.setFitHeight(31.25);
                lblPiece.setGraphic(view);
            } else lblPiece.setGraphic(null);
        }

        public BackgroundPiece getBcgp() {
            return bcgp;
        }

        public void setBcgp(BackgroundPiece bcgp) {
            this.bcgp = bcgp;
            String bcgPath = bcgp.getPath();
            bcgPath = bcgPath.replace('\\', '/');
            this.setStyle("-fx-background-image:url(" + bcgPath + ");-fx-background-repeat: stretch;  -fx-background-position: center center; -fx-background-size:stretch");
        }

    }
}
