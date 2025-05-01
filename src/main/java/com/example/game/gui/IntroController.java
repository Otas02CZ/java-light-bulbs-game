package com.example.game.gui;

import com.example.game.model.Game;
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
import java.net.URL;
import java.util.logging.Level;

public class IntroController extends Controller {

    private Stage howToPlayStage = null;
    private Stage creditsStage = null;

    public void setup() {
        super.setup();

        stage.setOnCloseRequest(event -> {
            if (howToPlayStage != null) {
                howToPlayStage.close();
            }

            if (creditsStage != null) {
                creditsStage.close();
            }

            logger.log(Level.INFO, "Closing GUI from Intro scene.");
        });
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

        URL gameSetupFXML = getClass().getResource("/gameSetup.fxml");
        if (gameSetupFXML == null) {
            logger.log(Level.SEVERE, "Failed to load gameSetup.fxml file.");
            String title = "Failed to load gameSetup";
            String header = "Failed to load gameSetup";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        FXMLLoader loader = new FXMLLoader(gameSetupFXML);
        Parent root = loader.load();
        GameSetupController controller = loader.getController();
        controller.setStage(stage);
        controller.setup();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        logger.log(Level.INFO, "Switched to Game Setup scene.");
    }

    @FXML
    private void handleLoadGame() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON game save file", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null) {
            logger.log(Level.INFO, "No file selected.");
            String title = "File not selected";
            String header = "No file selected";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        String path = selectedFile.getAbsolutePath();
        logger.log(Level.INFO, "Loading game save from file: " + path);

        try {
            Game game = Game.load(path);
            if (game == null) {
                logger.log(Level.INFO, "Failed to load game save from file: " + path);
                String title = "Failed to load game";
                String header = "File is not a valid game save file";
                String content = "Please try again.";
                showDialog(Alert.AlertType.WARNING, title, header, content);
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

            URL gameFXML = getClass().getResource("/game.fxml");
            if (gameFXML == null) {
                logger.log(Level.SEVERE, "Failed to load game.fxml file.");
                String title = "Failed to load game";
                String header = "Failed to load game";
                String content = "Please try again.";
                showDialog(Alert.AlertType.WARNING, title, header, content);
                return;
            }

            FXMLLoader loader = new FXMLLoader(gameFXML);
            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.setStage(this.stage);
            controller.setGameModel(game);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            controller.setup(0, "From save");
            logger.log(Level.INFO, "Switched to Game scene.");

        } catch (JSONException e) {
            logger.log(Level.WARNING, "Failed to load game save from file: " + path);
            String title = "Failed to load game";
            String header = "File is not a valid game save file";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            logger.log(Level.WARNING, "Failed to load game save from file: " + path, e);
        }

    }

    @FXML
    private void handleHowToPlay() throws IOException {
        if (howToPlayStage != null) {
            return;
        }

        URL howToPlayFXML = getClass().getResource("/howToPlay.fxml");
        if (howToPlayFXML == null) {
            logger.log(Level.SEVERE, "Failed to load howToPlay.fxml file.");
            String title = "Failed to load howToPlay";
            String header = "Failed to load howToPlay";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
        }

        FXMLLoader loader = new FXMLLoader(howToPlayFXML);
        Parent root = loader.load();
        HowToPlayController howToPlayController = loader.getController();

        howToPlayStage = new Stage();

        howToPlayController.setStage(howToPlayStage);
        howToPlayController.setup();

        howToPlayStage.setTitle("How To Play");
        howToPlayStage.setScene(new Scene(root));
        howToPlayStage.show();
        logger.log(Level.INFO, "Opened How To Play stage.");

        howToPlayStage.setOnCloseRequest(event -> {
            howToPlayStage = null;
            logger.log(Level.INFO, "Closing How To Play stage.");
        });
    }

    @FXML
    private void handleCredits() throws IOException {
        if (creditsStage != null) {
            return;
        }

        URL creditsFXML = getClass().getResource("/credits.fxml");
        if (creditsFXML == null) {
            logger.log(Level.SEVERE, "Failed to load credits.fxml file.");
            String title = "Failed to load credits";
            String header = "Failed to load credits";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        FXMLLoader loader = new FXMLLoader(creditsFXML);
        Parent root = loader.load();
        CreditsController creditsController = loader.getController();

        creditsStage = new Stage();

        creditsController.setStage(creditsStage);
        creditsController.setHostServices(hostServices);
        creditsController.setup();

        creditsStage.setTitle("Credits");
        creditsStage.setScene(new Scene(root));
        creditsStage.show();
        logger.log(Level.INFO, "Opened Credits stage.");

        creditsStage.setOnCloseRequest(event -> {
            creditsStage = null;
            logger.log(Level.INFO, "Closing Credits stage.");
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
        logger.log(Level.INFO, "Closing GUI from Intro scene.");
    }
}
