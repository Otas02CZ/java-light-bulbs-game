/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: IntroController.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Controller for the intro scene
 */

package com.example.game.gui;

import com.example.game.gui.shared.MusicManager;
import com.example.game.model.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

/**
 * Controller for the Intro scene
 * Handles button presses and logic needed to switch scenes.
 */
public class IntroController extends Controller {

    /**
     * Stores instance of HowToPlay stage if it is opened.
     */
    private Stage howToPlayStage = null;

    /**
     * Stores instance of Credits stage if it is opened.
     */
    private Stage creditsStage = null;

    private MusicManager musicManager = null;

    @FXML
    private Button musicButton;

    /**
     * Setups Intro controller
     */
    public void setup() {
        super.setup(); // call parent setup

        // set logic on window close
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

    /**
     * Handles NewGame button, closes opened howToPlay and Credits stages
     * @throws IOException when IO operations fail
     */
    @FXML
    private void handleNewGame() throws IOException {
        // close opened HowToPlay stage
        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }

        // close opened Credits stage
        if (creditsStage != null) {
            creditsStage.close();
            creditsStage = null;
        }

        // load gameSetup scene and setup controller
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
        controller.setHostServices(hostServices);
        controller.setup();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        logger.log(Level.INFO, "Switched to Game Setup scene.");
    }

    /**
     * Handles LoadGame button, opens file selection dialog to let the user
     * choose file, then it attempts to load the game.
     * @throws IOException when IO operation fails
     */
    @FXML
    private void handleLoadGame() throws IOException {
        // open file chooser to get path to save file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON game save file", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null) {
            // file not selected
            logger.log(Level.INFO, "No file selected.");
            String title = "File not selected";
            String header = "No file selected";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }
        // get path to selected save file
        String path = selectedFile.getAbsolutePath();
        logger.log(Level.INFO, "Loading game save from file: " + path);

        try { // attempts to load the game
            Game game = Game.load(path);
            if (game == null) { // game loading failed
                logger.log(Level.INFO, "Failed to load game save from file: " + path);
                String title = "Failed to load game";
                String header = "File is not a valid game save file";
                String content = "Please try again.";
                showDialog(Alert.AlertType.WARNING, title, header, content);
                return;
            }
            // close opened HowToPlay stage
            if (howToPlayStage != null) {
                howToPlayStage.close();
                howToPlayStage = null;
            }
            // close opened Credits stage
            if (creditsStage != null) {
                creditsStage.close();
                creditsStage = null;
            }

            // load game scene and setup controller
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
            controller.setHostServices(hostServices);
            controller.setGameModel(game);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            controller.setup(0, "From save");
            logger.log(Level.INFO, "Switched to Game scene.");

        } catch (JSONException e) { // save file parsing failed
            logger.log(Level.WARNING, "Failed to load game save from file: " + path);
            String title = "Failed to load game";
            String header = "File is not a valid game save file";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            logger.log(Level.WARNING, "Failed to load game save from file: " + path, e);
        }

    }

    /**
     * Handles HowToPlay button, opens HowToPlay window if not yet opened
     * @throws IOException when IO operation fails
     */
    @FXML
    private void handleHowToPlay() throws IOException {
        // open only if not yet opened
        if (howToPlayStage != null) {
            return;
        }

        // load howToPlay scene and setup controller
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

        // setup event when user closes the HowToPlay window
        howToPlayStage.setOnCloseRequest(event -> {
            howToPlayStage = null;
            logger.log(Level.INFO, "Closing How To Play stage.");
        });
    }

    /**
     * Handles Credits button, opens Credits windows if not yet opened
     * @throws IOException when IO operation fails
     */
    @FXML
    private void handleCredits() throws IOException {
        // only open if not yet opened
        if (creditsStage != null) {
            return;
        }

        // load Credits scene and setup controller
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

        // setup event when user closes the Credits window
        creditsStage.setOnCloseRequest(event -> {
            creditsStage = null;
            logger.log(Level.INFO, "Closing Credits stage.");
        });
    }

    /**
     * Handles Quit button that quits the whole application
     */
    @FXML
    private void handleQuit() {
        // close HowToPlay window if opened
        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }
        // close Credits window if opened
        if (creditsStage != null) {
            creditsStage.close();
            creditsStage = null;
        }

        // quit the app
        stage.close();
        logger.log(Level.INFO, "Closing GUI from Intro scene.");
    }

    @FXML
    private void handleMusicControls() {
        musicManager.toggleMusic();

        if (musicManager.isMusicPlaying()) {
            musicButton.setText("Music: ON");
        } else {
            musicButton.setText("Music: OFF");
        }

    }
}
