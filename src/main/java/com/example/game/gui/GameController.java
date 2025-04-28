package com.example.game.gui;

import com.example.game.model.Game;
import com.example.game.shared.GUIObservable;
import com.example.game.shared.TimerObservable;
import com.example.game.shared.ToolEnvironment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class GameController implements GUIObservable.GUIObserver, TimerObservable.TimerObserver {

    private Stage stage;

    private ToolEnvironment game;

    private int turns;

    private ImageView[][] imageGrid = null;

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
        game.setupGUIObserver(this);
        game.setupTimerObserver(this);
    }

    public void startup(int time, String difficulty) {
        stage.setOnCloseRequest(event -> {
            game.stopTimer();
        });

        if (time != 0)
            game.startTimer(time);

        setupTimerIcon();
        updateTurns();
        setDifficulty(difficulty);
        updateLitUp();
        generateGameGrid();
        // todo?
    }

    private void setupTimerIcon() {
        Image image = new Image(getClass().getResource("/images/icons/alarm_icon.png").toExternalForm());

        timerIcon.setImage(image);
        timerIcon.setFitWidth(40);
        timerIcon.setFitHeight(40);
    }

    private void updateTurns() {
        turnsLabel.setText("Turns: " + turns);
    }

    private void setDifficulty(String difficulty) {
        difficultyLabel.setText(difficulty);
    }

    private void updateLitUp() {
        int litUp = game.getBulbsOn();
        int total = game.getBulbsTotal();
        litUpLabel.setText("Lit up: " + litUp + "/" + total);
    }

    public void updateTimer(int seconds) {
        System.out.println("Timer update: " + seconds);
        String timeLeft = String.format("%02d:%02d", seconds / 60, seconds % 60);
        timerLabel.setText(timeLeft);

        if (seconds <= 0) {
            transitionToGameLost();
        }
    }

    public void updateGUI() {
        updateLitUp();

        if (game.hasWon()) {
            transitionToGameWon();
        }

        //TODO: update game field
    }

    private void generateGameGrid() {
        int rows = game.rows();
        int cols = game.cols();

        imageGrid = new ImageView[rows][cols];

        gameGrid.getChildren().clear();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ImageView cell = new ImageView();
                cell.setFitWidth(40);
                cell.setFitHeight(40);
                cell.getStyleClass().add("game-cell");

                cell.setOnMouseClicked(event -> {
                    // handle click
                    turns++;
                    updateTurns();
                });

                gameGrid.add(cell, col, row);
            }
        }

        applyCorrectCellImages();
    }

    private void applyCorrectCellImages() {
        int rows = game.rows();
        int cols = game.cols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

            }
        }
    }

    private void transitionToGameWon() {

    }

    private void transitionToGameLost() {

    }

    private void transitionGameGridToFinish() {
        //TODO: change game grid to non-clickable also add remaining turns to tooltips
    }

    private boolean showDialog(Alert.AlertType type, String title, String header, String content) {
        // Create a confirmation alert
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        if (type == Alert.AlertType.CONFIRMATION) {
            // Show the dialog and wait for user interaction
            Optional<ButtonType> result = alert.showAndWait();

            // Check the result
            return result.isPresent() && result.get() == ButtonType.OK;
        }

        alert.show();

        return false;
    }


    @FXML
    public void handleToMainMenu() throws IOException {
        String title = "Going back to Main Menu";
        String header = "Are you sure you want to go back to Main Menu?";
        String content = "All unsaved progress will be lost!";
        if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
            return;

        game.stopTimer();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/intro.fxml"));
        Parent root = loader.load();
        IntroController controller = loader.getController();
        controller.setStage(this.stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void handleQuit() {

        String title = "Quitting the Game";
        String header = "Are you sure you want to quit the game?";
        String content = "All unsaved progress will be lost!";
        if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
            return;

        game.stopTimer();
        stage.close();
    }

    @FXML
    public void handleShowHowToPlay() {

    }

    @FXML
    public void handleShowHelpView() {

    }

    @FXML
    public void handleSolve() {

    }

    @FXML
    public void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON game save file", "*.json"));
        File file = fileChooser.showSaveDialog(stage);

        if (file == null) {
            String title = "File not selected";
            String header = "No file selected";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        String path = file.getAbsolutePath();
        System.out.println("Saving game to file: " + path);

        try {
            game.save(path);

            String title = "Game saved";
            String header = "Game saved successfully";
            String content = "Continue, times is ticking.";
            showDialog(Alert.AlertType.INFORMATION, title, header, content);

        } catch (IOException e) {
            String title = "Failed to save game";
            String header = "File is not a valid game save file";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
        }

    }
}
