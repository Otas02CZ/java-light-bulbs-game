package com.example.game.gui;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class CreditsController {

    private final int WIDTH = 400;
    private final int HEIGHT = 600;

    private Stage stage = null;
    private HostServices hostServices = null;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
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
        stage.setResizable(false);
    }

     @FXML
    public void handleAlarmClockLink() {
        hostServices.showDocument("https://icons8.com/icons/set/alarm-clock--corners-round");
    }
}
