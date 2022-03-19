package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Card;
import model.ConcentrationModel;
import model.Observer;

import java.util.*;

/**
 * The ConcentrationGUI application is the UI for Concentration.
 *
 * @author Ryan Nowak
 */
public class ConcentrationGUI extends Application
        implements Observer< ConcentrationModel, Object > {

    //Model for the view and controller
    private ConcentrationModel model;

    //Board size
    private final static int ROWS = 4;
    private final static int COLS = 4;

    //Card face-up images
    private Image abra = new Image(getClass().getResourceAsStream(
            "resources/abra.png"));
    private Image bulbasaur = new Image(getClass().getResourceAsStream(
            "resources/bulbasaur.png"));
    private Image charmander = new Image(getClass().getResourceAsStream(
            "resources/charmander.png"));
    private Image jigglypuff = new Image(getClass().getResourceAsStream(
            "resources/jigglypuff.png"));
    private Image meowth = new Image(getClass().getResourceAsStream(
            "resources/meowth.png"));
    private Image pikachu = new Image(getClass().getResourceAsStream(
            "resources/pikachu.png"));
    private Image squirtle = new Image(getClass().getResourceAsStream(
            "resources/squirtle.png"));
    private Image venomoth = new Image(getClass().getResourceAsStream(
            "resources/venomoth.png"));
    //Card face-down image
    private Image pokeball = new Image(getClass().getResourceAsStream(
            "resources/pokeball.png"));

    //Label that displays current turn and win status
    private Label labelMessage;

    //List of card buttons
    private ArrayList<Button> pokemonButtons = new ArrayList<>();

    //Reset button
    private Button reset;

    //Undo button
    private Button undo;

    //Cheat button
    private Button cheat;

    //Label that displays number of moves
    private Label moves;


    /**
     * Helper function for start method. Makes a grid of pokemon
     * buttons that are put into a GridPane.
     * @return GridPane of pokemon buttons
     */
    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Button button = new Button();
                button.setGraphic(new ImageView(pokeball));
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> {
                    model.selectCard((finalRow *4)+ finalCol);
                    System.out.println("Event");
                });
                pokemonButtons.add(button);
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }


    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
    }

    /**
     * Constructs the layout for the game. The scene contains a BorderPane
     * where the top has a label that displays the current move or if the
     * player has won. The center contains a GridPane of pokemon buttons.
     * The bottom has a BorderPane where the center contains the buttons for
     * reset, undo, and cheat and the right margin contains the move counter.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start( Stage stage ) throws Exception {
        this.model = new ConcentrationModel();
        model.addObserver(this::update);

        BorderPane borderPane = new BorderPane();

        this.labelMessage = new Label("Select the first card.");
        borderPane.setTop(labelMessage);
        BorderPane.setAlignment(labelMessage, Pos.CENTER_LEFT);

        borderPane.setCenter(makeGridPane());

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().add(reset = new Button());

        BorderPane bottomBorder = new BorderPane();

        HBox bottomButtons = new HBox();
        reset = new Button();
        reset.setText("Reset");
        reset.setOnAction(event -> model.reset());
        undo = new Button();
        undo.setText("Undo");
        undo.setOnAction(event -> model.undo());
        cheat = new Button();
        cheat.setText("Cheat");
        cheat.setOnAction(event -> model.cheat());
        bottomButtons.getChildren().add(reset);
        bottomButtons.getChildren().add(undo);
        bottomButtons.getChildren().add(cheat);


        moves = new Label("0 Moves");
        bottomBorder.setCenter(bottomButtons);
        bottomBorder.setRight(moves);
        borderPane.setBottom(bottomBorder);
        BorderPane.setAlignment(bottomButtons, Pos.CENTER);

        Scene scene = new Scene(borderPane);
        stage.setTitle("Concentration");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Update the UI. This method is called by an object in the game model.
     * The contents of the buttons are changed based on the card faces in
     * the model. Changes in the the text in the labels may also occur based
     * on the changed model state.
     * The update makes calls to the public interface of the model components
     * to determine the new values to display.
     *
     * @param concentrationModel
     * @param o
     */
    @Override
    public void update( ConcentrationModel concentrationModel, Object o ) {
        //Sets the current move count
        moves.setText(model.getMoveCount() + " Moves");

        //Flip cards over and checks if player wins game
        ArrayList<Card> cards = model.getCards();
        int numFaceUp = 0;
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card.isFaceUp()) {
                numFaceUp++;
                switch (card.getNumber()) {
                    case 0:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(abra));
                        break;
                    case 1:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(bulbasaur));
                        break;
                    case 2:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(charmander));
                        break;
                    case 3:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(jigglypuff));
                        break;
                    case 4:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(meowth));
                        break;
                    case 5:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(pikachu));
                        break;
                    case 6:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(squirtle));
                        break;
                    case 7:
                        pokemonButtons.get(i).setGraphic(
                                new ImageView(venomoth));
                        break;

                }
            }
            else {
                pokemonButtons.get(i).setGraphic(
                        new ImageView(pokeball));
            }
        }
        if (numFaceUp == cards.size()) {
            labelMessage.setText("You Win!");
        }
        else if (numFaceUp % 2 == 0) {
            labelMessage.setText("Select the first card.");
        }
        else if (numFaceUp % 2 == 1) {
            labelMessage.setText("Select the second card.");
        }

        //Cheat button is pressed
        if (o != null && o.equals("cheat")) {
            showCheatScreen();
        }

    }

    /**
     * Helper function for update method. Updates the GUI to show the
     * cheat screen.
     */
    private void showCheatScreen() {
        Stage stage = new Stage();
        stage.setTitle("Cheat window");

        //Make gridPane for pokemon buttons to go into
        GridPane gridPane = new GridPane();

        //Make new pokemon buttons that are faceUp and add them to
        //the GridPane
        ArrayList<Card> cards = model.getCards();
        for (int i = 0; i < cards.size(); i++) {
            Card card = new Card(cards.get(i));
            card.setFaceUp();
            Button button = new Button();
            switch (card.getNumber()) {
                case 0:
                    button.setGraphic(new ImageView(abra));
                    break;
                case 1:
                    button.setGraphic(new ImageView(bulbasaur));
                    break;
                case 2:
                    button.setGraphic(new ImageView(charmander));
                    break;
                case 3:
                    button.setGraphic(new ImageView(jigglypuff));
                    break;
                case 4:
                    button.setGraphic(new ImageView(meowth));
                    break;
                case 5:
                    button.setGraphic(new ImageView(pikachu));
                    break;
                case 6:
                    button.setGraphic(new ImageView(squirtle));
                    break;
                case 7:
                    button.setGraphic(new ImageView(venomoth));
                    break;
            }
            gridPane.add(button, i%4, i/4);
        }

        //Put GridPane into scene and show the stage
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * main entry point launches the JavaFX GUI.
     *
     * @param args not used
     */
    public static void main( String[] args ) {
        Application.launch( args );
    }
}
