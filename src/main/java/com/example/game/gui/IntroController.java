package com.example.game.gui;

import com.example.game.model.Game;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class IntroController {
    private Stage stage;

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private Stage howToPlayStage = null;
    private Stage creditsStage = null;

    private HostServices hostServices = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setup() {
        setupWindowSize();
        stage.setOnCloseRequest(event -> {
            if (howToPlayStage != null) {
                howToPlayStage.close();
            }

            if (creditsStage != null) {
                creditsStage.close();
            }
        });
    }

    private void setupWindowSize() {
        stage.setWidth(WIDTH);
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinHeight(HEIGHT);
        stage.setMaxHeight(HEIGHT);
        stage.setResizable(false);
    }

    @FXML
    private void handleNewGame() throws IOException {
        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }

        if (creditsStage != null) {
            creditsStage.close();
            creditsStage = null;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameSetup.fxml"));
        Parent root = loader.load();
        GameSetupController controller = loader.getController();
        controller.setStage(stage);
        controller.setup();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void handleLoadGame() throws IOException {
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
            if (howToPlayStage != null) {
                howToPlayStage.close();
                howToPlayStage = null;
            }

            if (creditsStage != null) {
                creditsStage.close();
                creditsStage = null;
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
    private void handleHowToPlay() throws IOException {
        if (howToPlayStage != null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/howToPlay.fxml"));
        Parent root = loader.load();

        HowToPlayController howToPlayController = loader.getController();

        howToPlayStage = new Stage();

        howToPlayController.setStage(howToPlayStage);
        howToPlayController.setup();

        howToPlayStage.setTitle("How To Play");
        howToPlayStage.setScene(new Scene(root));
        howToPlayStage.show();

        howToPlayStage.setOnCloseRequest(event -> {
            howToPlayStage = null;
        });
    }

    @FXML
    private void handleCredits() throws IOException {
        if (creditsStage != null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/credits.fxml"));
        Parent root = loader.load();

        CreditsController creditsController = loader.getController();

        creditsStage = new Stage();

        creditsController.setStage(creditsStage);
        creditsController.setHostServices(hostServices);
        creditsController.setup();

        creditsStage.setTitle("Credits");
        creditsStage.setScene(new Scene(root));
        creditsStage.show();

        creditsStage.setOnCloseRequest(event -> {
            creditsStage = null;
        });
    }

    @FXML
    private void handleQuit() {
        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }

        if (creditsStage != null) {
            creditsStage.close();
            creditsStage = null;
        }

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
