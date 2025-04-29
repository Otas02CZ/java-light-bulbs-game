package com.example.game.gui;

import javafx.stage.Stage;

public class HowToPlayController {

    private final int WIDTH = 400;
    private final int HEIGHT = 600;

    private Stage stage = null;


    public void setStage(Stage stage) {
        this.stage = stage;
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
}
