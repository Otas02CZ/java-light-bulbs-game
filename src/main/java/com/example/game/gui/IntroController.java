package com.example.game.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class IntroController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleNewGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameSetup.fxml"));
        Parent root = loader.load();
        GameSetupController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void handleLoadGame(ActionEvent event) {
        // TODO: Load a saved game
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
        System.exit(0); //TODO: correct?
    }
}
