package com.example.game.gui;

import javafx.application.HostServices;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.logging.Logger;

public abstract class Controller {

    protected int WIDTH = 800;
    protected int HEIGHT = 600;

    protected Logger logger = Logger.getLogger(getClass().getName());

    protected Stage stage = null;

    protected HostServices hostServices = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setup() {
        setupWindowSize();
    }

    protected void setupWindowSize() {
        stage.setWidth(WIDTH);
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinHeight(HEIGHT);
        stage.setMaxHeight(HEIGHT);
        stage.setResizable(false);
    }

    protected boolean showDialog(Alert.AlertType type, String title, String header, String content) {
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
}
