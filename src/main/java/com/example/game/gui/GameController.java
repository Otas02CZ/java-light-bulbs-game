package com.example.game.gui;

import com.example.game.model.Game;
import com.example.game.shared.GUIObservable;
import com.example.game.shared.TimerObservable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameController implements GUIObservable.GUIObserver, TimerObservable.TimerObserver {

    private Stage stage;

    private Game game;

    @FXML
    private HBox topButtonRow;

    @FXML
    private Label mainLabel;

    @FXML
    private ImageView timerIcon;

    @FXML
    private Label timerLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label turnsLabel;

    @FXML
    private Label difficultyLabel;

    @FXML
    private Label litUpLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGameModel(Game game) {
        this.game = game;
    }

    public void startup(int time) {
        stage.setOnCloseRequest(event -> {
            game.stopTimer();
        });


        game.setupGUIObserver(this);
        game.setupTimerObserver(this);
        game.startTimer(time);
        // todo?
    }

    public void updateTimer(int seconds) {
        System.out.println("Timer update: " + seconds);
        String timeLeft = String.format("%02d:%02d", seconds / 60, seconds % 60);
        timerLabel.setText(timeLeft);
    }

    public void updateGUI() {

    }

    public void generateGameGrid(int rows, int cols) {
        gameGrid.getChildren().clear();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ImageView cell = new ImageView();
                cell.setFitWidth(40);
                cell.setFitHeight(40);
                cell.getStyleClass().add("game-cell");

                cell.setOnMouseClicked(event -> {
                    // handle click
                });

                gameGrid.add(cell, col, row);
            }
        }
    }
}
