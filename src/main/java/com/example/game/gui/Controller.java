/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: Controller.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Abstract class that provides shared functionality
 * for all Controllers
 */

package com.example.game.gui;

import javafx.application.HostServices;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Abstract Controller class for shared functionality
 * of all used Controllers.
 */
public abstract class Controller {
    /**
     * Width of the window
     */
    protected int WIDTH = 800;

    /**
     * Height of the window
     */
    protected int HEIGHT = 600;

    /**
     * Logger used for logging in all inheriting Controllers
     */
    protected Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Stage of the current scene
     */
    protected Stage stage = null;

    /**
     * Host services propagated from main Application
     */
    protected HostServices hostServices = null;

    /**
     * Sets stage to given instance
     * @param stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets Host Services to specified instance
     * @param hostServices instance to set
     */
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    /**
     * Setup Controller
     */
    public void setup() {
        setupWindowSize();
    }

    /**
     * Configure window size and resizable flag
     */
    protected void setupWindowSize() {
        stage.setWidth(WIDTH);
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinHeight(HEIGHT);
        stage.setMaxHeight(HEIGHT);
        stage.setResizable(false);
    }

    /**
     * Shows dialog of specified type and parameters.
     * In case of a CONFIRM dialog also wait for the user's response
     * and return it.
     *
     * @param type of the alert
     * @param title of the alert
     * @param header of the alert
     * @param content of the alert
     * @return boolean whether the user confirmed the dialog (if CONFIRM dialog was specified)
     */
    protected boolean showDialog(Alert.AlertType type, String title, String header, String content) {
        // create a confirmation alert
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        if (type == Alert.AlertType.CONFIRMATION) {
            // show the dialog and wait for user interaction
            Optional<ButtonType> result = alert.showAndWait();

            // check the result
            return result.isPresent() && result.get() == ButtonType.OK;
        }

        alert.show();

        return false;
    }
}
