package Helpers;

import Enums.GameMode;
import Game_Package.gController;
import Enums.Piece;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static Helpers.GlobalVars.srcPath;


public class MainMenu extends Application {

    private gController ctrl;

    public MainMenu(){
        this.ctrl = null;
        //used to know if a controller was already assigned or not
    }

    @Override
    public void start(Stage stage){
        Platform.setImplicitExit(true);//if the tab closes the program will close
        VBox root = new VBox();
        Label lblBoard = new Label();
        Label lblBoardShd = new Label();
        Label title = new Label("ABALONE");
        title.setStyle("-fx-font-size: 80px; -fx-font-weight: bold; -fx-effect: dropshadow( gaussian , black , 0,0,1,1 ); -fx-font-family: \"Calibri\"; -fx-text-fill:#ffff00;-fx-underline: true; ");
        root.setAlignment(Pos.CENTER);

        String bckStagePath = "file:" + srcPath + "\\Pictures\\Stage.jpg";
        bckStagePath = bckStagePath.replace('\\','/');
        root.setStyle("-fx-background-image:url(" + bckStagePath + ");-fx-background-repeat: stretch;  -fx-background-position: center center; -fx-background-size:stretch");
        //setting root's background image
        root.getChildren().addAll(title,lblBoard);

        Image imgFloatingBoard = new Image("file:" + srcPath + "\\Pictures\\Floating board.png");
        ImageView viewFloatingBoard = new ImageView(imgFloatingBoard);
        viewFloatingBoard.setFitWidth(202);
        viewFloatingBoard.setFitHeight(130.5);
        lblBoard.setGraphic(viewFloatingBoard);
        //showing the floating board at the start of the game

        /**Setting the animation of the floating board**/
        Path upAndDownPath = new Path();
        upAndDownPath.getElements().add (new MoveTo (101.2f,0f));
        upAndDownPath.getElements().add (new VLineTo(50f));
        PathTransition boardPath = new PathTransition();
        boardPath.setPath(upAndDownPath);
        boardPath.setDuration(Duration.millis(1000));
        boardPath.setNode(lblBoard);
        boardPath.setInterpolator(Interpolator.LINEAR);
        boardPath.setCycleCount(Animation.INDEFINITE);
        boardPath.setAutoReverse(true);

        ImageView viewFloatingBoardShd = new ImageView(imgFloatingBoard);
        viewFloatingBoardShd.setFitWidth(202);
        viewFloatingBoardShd.setFitHeight(130.5);
        viewFloatingBoardShd.setOpacity(0.2);
        lblBoardShd.setGraphic(viewFloatingBoardShd);
        root.getChildren().add(lblBoardShd);
        //creating a shadow copy of the floating board

        /**Setting the animation of the floating board shadow**/
        PathTransition shdPath = new PathTransition();
        shdPath.setPath(upAndDownPath);
        shdPath.setDuration(Duration.millis(1000));
        shdPath.setNode(lblBoardShd);
        shdPath.setInterpolator(Interpolator.LINEAR);
        shdPath.setCycleCount(Animation.INDEFINITE);
        shdPath.setAutoReverse(true);

        boardPath.play();
        shdPath.play();
        //playing both animations
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("file:" + srcPath + "\\Pictures\\Abalone Icon.png"));
        stage.setTitle("Abalone");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        double space = ((scene.getHeight() - lblBoard.getHeight() - lblBoardShd.getHeight() - title.getHeight())/3);
        root.setSpacing(space);

        lblBoard.addEventHandler(MouseEvent.MOUSE_CLICKED,//clicked on the floating board
                event -> {
                    GameMode gameMode = PlayerGameModeChoice();
                    if(gameMode != null) {//if a game mode was selected
                        Piece pieceA = PlayerBallChoice(Piece.NONE, 1);
                        if(pieceA != Piece.NONE) {//if a ball was selected
                            Piece pieceB = PlayerBallChoice(pieceA, 2);
                            if(pieceB != Piece.NONE) {//if a ball was selcted
                                Platform.setImplicitExit(false);//Stopping javafx from shutting down automatically once main menu window is closed
                                stage.setMaximized(false);
                                //if maximizing won't be turned off, java will look at the new window as maximized
                                //without the main menu being maximized by the code/user, and it will cause the maximizing button to bug out
                                if(ctrl == null)//first time starting the game
                                    ctrl = new gController(this,stage,pieceA,pieceB,gameMode);
                                else //rerunning the game
                                    ctrl.StartGame(stage,pieceA,pieceB,gameMode);
                            }
                        }
                    }
                });
        lblBoard.setOnMouseEntered(event -> {
            //changing the boards to the glowing boards
            viewFloatingBoard.setImage(new Image("file:" + srcPath + "\\Pictures\\Glowing Floating board.png"));
            viewFloatingBoardShd.setImage(new Image("file:" + srcPath + "\\Pictures\\Glowing Floating board.png"));
            lblBoardShd.setGraphic(viewFloatingBoardShd);
            lblBoard.setGraphic(viewFloatingBoard);
            scene.setCursor(Cursor.HAND);
        });

        lblBoard.setOnMouseExited(event -> {
            //changing the boards to the normal boards
            viewFloatingBoard.setImage(new Image("file:" + srcPath + "\\Pictures\\Floating board.png"));
            viewFloatingBoardShd.setImage(new Image("file:" + srcPath + "\\Pictures\\Floating board.png"));
            lblBoardShd.setGraphic(viewFloatingBoardShd);
            lblBoard.setGraphic(viewFloatingBoard);
            scene.setCursor(Cursor.DEFAULT);
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                //making sure the space is relative to the height of the window
                double space = ((scene.getHeight() - lblBoard.getHeight() - lblBoardShd.getHeight() - title.getHeight())/3);
                root.setSpacing(space);
            }
        });
    }

    public Piece PlayerBallChoice(Piece forbidden,int playerNum) {
        //showing the player ball dialog
        //it will not include the forbidden piece in the options
        //it will show the playerNum in the dialog
        List<String> choices = new ArrayList<>();
        for(Piece p: Piece.values())
            if(p != Piece.NONE && p != forbidden)
                choices.add(p.toString());
        //adding all the pieces to the choices

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Choose a color", choices);
        dialog.setTitle("Color Choice");
        dialog.setHeaderText("Player " + playerNum + " choose a color!");
        dialog.setContentText("Please choose one of the options:");
        dialog.setGraphic(new ImageView(new Image(Piece.BLUE.getDesignPath())));//showing the blue piece as an example
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        //getting the stage of the dialog
        stage.getIcons().add(new Image("file:" + srcPath + "\\Pictures\\Abalone Icon.png"));
        //setting the icon of the dialog

        dialog.selectedItemProperty().addListener((observableValue, s, t1) -> {//clicking on the drop-down list
            Piece p = Piece.RED;
            p = p.PieceByString(observableValue.getValue());
            dialog.setGraphic(new ImageView(new Image(p.getDesignPath())));
            //showing the selcted piece in the dialog
        });

        dialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);//removing the cancel button
        ButtonType btnCancel = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);//creating a custom cancel button
        dialog.getDialogPane().getButtonTypes().add(btnCancel);


        Optional<String> result = dialog.showAndWait();
        while(result.isPresent()) {
            if (result.isPresent()) {
                Piece p = Piece.RED;
                p = p.PieceByString(result.get());
                if (p != Piece.NONE)//if a ball was selcted
                    return p;
                result = dialog.showAndWait();
            }
        }
        return Piece.NONE;//if the dialog was closed
    }

    public GameMode PlayerGameModeChoice() {
        List<String> choices = new ArrayList<>();
        for(GameMode g: GameMode.values()) {
            choices.add(g.toString().replace("_", " "));
        }
        //creating a list of the game modes

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Choose a game mode", choices);
        dialog.setTitle("Game Mode Choice");
        dialog.setHeaderText("Choose a game mode!");
        dialog.setContentText("Please choose one of the options:");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        //getting the stage of the current dialog
        stage.getIcons().add(new Image("file:" + srcPath + "\\Pictures\\Abalone Icon.png"));
        //setting the icon of the dialog
        dialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);//removing the cancel button
        ButtonType buttonTypeCancel = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);//creating a custom cancel button
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.selectedItemProperty().addListener((observableValue, s, t1) -> {//clicking on the drop-down list
            GameMode g = GameMode.PLAYER_VS_AI;
            g = g.GameModeByString(observableValue.getValue());
            ImageView iv = new ImageView(new Image(g.getPath()));
            iv.setFitHeight(50);
            iv.setFitWidth(100);
            dialog.setGraphic(iv);
            //showing the game mode icon
        });

        Optional<String> result = dialog.showAndWait();
        while(result.isPresent()) {
            if (result.isPresent()) {
                GameMode g = GameMode.AI_VS_PLAYER;
                g = g.GameModeByString(result.get());
                if (g != null)//if a game mode was selcted
                    return g;
                result = dialog.showAndWait();
            }
        }
        return null;//the cancel button was clicked
    }
}