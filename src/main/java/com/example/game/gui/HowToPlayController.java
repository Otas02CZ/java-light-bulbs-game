package com.example.game.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HowToPlayController {

    private final int WIDTH = 400;
    private final int HEIGHT = 600;

    private Stage stage = null;

    @FXML
    public ImageView powerSourceImage;

    @FXML
    public ImageView lightBulbImage;

    @FXML
    public ImageView wireImage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setup() {
        setupWindowSize();
        setupImages();
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

    private void setupImages() {
        Image image = new Image(getClass().getResourceAsStream("/images/icons/power_0001.png"));
        powerSourceImage.setImage(image);

        image = new Image(getClass().getResourceAsStream("/images/icons/bulb_on_1000.png"));
        lightBulbImage.setImage(image);

        image = new Image(getClass().getResourceAsStream("/images/icons/wire_on_1100.png"));
        wireImage.setImage(image);
    }
}
