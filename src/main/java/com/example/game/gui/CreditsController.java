package com.example.game.gui;

import javafx.fxml.FXML;

public class CreditsController extends Controller {

    public void setup() {
        WIDTH = 400;
        HEIGHT = 550;
        super.setup();
    }

    @FXML
    public void handleAlarmClockLink() {
        hostServices.showDocument("https://icons8.com/icons/set/alarm-clock--corners-round");
    }

    @FXML
    public void handleSpaceMonoLink() {
        hostServices.showDocument("https://fonts.google.com/specimen/Space+Mono");
    }

    @FXML
    public void handlePressStartLink() {
        hostServices.showDocument("https://fonts.google.com/specimen/Press+Start+2P");
    }
}
