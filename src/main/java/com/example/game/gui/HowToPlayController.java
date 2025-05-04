/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: HowToPlayController.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Controller for the HowToPlay scene
 */

package com.example.game.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Controller for the HowToPlay scene
 * Only setups images.
 */
public class HowToPlayController extends Controller {

    /**
     * Image of a power source
     */
    @FXML
    public ImageView powerSourceImage;

    /**
     * Image of light bulb
     */
    @FXML
    public ImageView lightBulbImage;

    /**
     * Image of a wire
     */
    @FXML
    public ImageView wireImage;

    /**
     * Setup this controller
     */
    public void setup() {
        WIDTH = 400; // override the width of the window
        super.setup(); // setup in parent
        // setup images
        setupImage(powerSourceImage, "/images/icons/power_0001.jpg");
        setupImage(lightBulbImage, "/images/icons/bulb_on_1000.jpg");
        setupImage(wireImage, "/images/icons/wire_on_1100.jpg");
    }

    /**
     * Setup given Image view to use Image from specified path
     * @param imageView to set up
     * @param path with the image to use
     */
    private void setupImage(ImageView imageView, String path) {
        InputStream imageURL = getClass().getResourceAsStream(path);
        if (imageURL == null) {
            logger.log(Level.WARNING, "Could not find image: " + path);
            return;
        }
        Image image = new Image(imageURL);
        imageView.setImage(image);
        logger.log(Level.INFO, "Loaded image: " + path);
    }
}
