package com.example.game.gui;

import com.example.game.model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class IntroController {
    private Stage stage;

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setup() {
        setupWindowSize();
    }

    private void setupWindowSize() {
        stage.setWidth(WIDTH);
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinHeight(HEIGHT);
        stage.setMaxHeight(HEIGHT);
    }

    @FXML
    private void handleNewGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameSetup.fxml"));
        Parent root = loader.load();
        GameSetupController controller = loader.getController();
        controller.setStage(stage);
        controller.setup();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void handleLoadGame(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON game save file", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null) {
            String title = "File not selected";
            String header = "No file selected";
            String content = "Please try again.";
            showInfoDialog(title, header, content);
            return;
        }

        String path = selectedFile.getAbsolutePath();
        System.out.println("Loading game from file: " + path);

        try {
            Game game = Game.load(path);
            if (game == null) {
                String title = "Failed to load game";
                String header = "File is not a valid game save file";
                String content = "Please try again.";
                showInfoDialog(title, header, content);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game.fxml"));
            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.setStage(this.stage);
            controller.setGameModel(game);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            controller.setup(0, "From save");

        } catch (JSONException e) {
            String title = "Failed to load game";
            String header = "File is not a valid game save file";
            String content = "Please try again.";
            showInfoDialog(title, header, content);
        }

    }

    @FXML
    private void handleHowToPlay(ActionEvent event) {
        // TODO: Show instructions
    }

    @FXML
    private void handleCredits(ActionEvent event) {
        // TODO: Show credits
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        stage.close();
    }

    private void showInfoDialog(String title, String header, String content) {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.show();
    }
}
