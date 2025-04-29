package com.example.game.gui;

import com.example.game.logger.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class MainGUI extends Application {
    private Stage stage;

    @Override
    public void init() throws Exception {

        Font font1 = Font.loadFont(getClass().getResourceAsStream("/fonts/SpaceMono-Regular.ttf"), 40);
        Font font2 = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 40);
        System.out.println("Fonts loaded, names to be used in CSS: |" + font1.getName() + "| |" + font2.getName() + "|.");
        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/intro.fxml"));
        Parent root = loader.load();
        IntroController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setHostServices(getHostServices());

        InputStream iconStream = getClass().getResourceAsStream("/images/icons/app/icon_16.png");
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Could not load app icon.");
        }

        loadAppIcon("/images/icons/app/icon_16.png");
        loadAppIcon("/images/icons/app/icon_32.png");
        loadAppIcon("/images/icons/app/icon_64.png");
        loadAppIcon("/images/icons/app/icon_128.png");
        loadAppIcon("/images/icons/power_1111.png");


        Scene scene = new Scene(root);

        stage.setTitle("Light Bulbs Game");
        stage.setScene(scene);

        controller.setup();
        stage.show();
    }

    private void loadAppIcon(String path) {
        InputStream iconStream = getClass().getResourceAsStream(path);
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Could not load app icon from path: " + path);
        }
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
