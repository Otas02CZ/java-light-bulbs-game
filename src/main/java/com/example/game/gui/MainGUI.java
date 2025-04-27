package com.example.game.gui;

import com.example.game.logger.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGUI extends Application {
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/intro.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Light Bulbs Game");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        try {
            Logger.init();
        } catch (Exception e) {
            System.out.println("Can not initialize logger. Ending.");
            e.printStackTrace();
            return;
        }

        launch(args);
    }
}
