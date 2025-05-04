/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: CreditsController.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Controller for the credits scene
 */

package com.example.game.gui;

import javafx.fxml.FXML;

/**
 * Credits controller. Handles opening of links from the credits window
 */
public class CreditsController extends Controller {

    /**
     * Setup the controller
     */
    public void setup() {
        WIDTH = 400; // override width
        HEIGHT = 550; // override height
        super.setup(); // setup in parent class
    }

    /**
     * Handle Click on Alarm Clock image link, opens browser with link
     */
    @FXML
    public void handleAlarmClockLink() {
        hostServices.showDocument("https://icons8.com/icons/set/alarm-clock--corners-round");
    }

    /**
     * Handle Click on Space Mono font link, opens browser with link
     */
    @FXML
    public void handleSpaceMonoLink() {
        hostServices.showDocument("https://fonts.google.com/specimen/Space+Mono");
    }

    /**
     * Handle Click on Press Start font link, opens browser with link
     */
    @FXML
    public void handlePressStartLink() {
        hostServices.showDocument("https://fonts.google.com/specimen/Press+Start+2P");
    }

    /**
     * Handle Click on Motivation link, opens browser with link
     */
    @FXML
    public void handleMotivationLink() {
        hostServices.showDocument("https://www.youtube.com/watch?v=G1IbRujko-A");
    }
}
