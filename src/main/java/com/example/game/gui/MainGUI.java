/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: MainGUI.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: GUI application entry point
 */

package com.example.game.gui;

import com.example.game.gui.shared.MusicManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * GUI application entry point, starts JavaFX application.
 * Creates main stage, prepares fonts and setups.
 */
public class MainGUI extends Application {
    /**
     * JavaFX stage of the GUI
     */
    private Stage stage;

    private MusicManager musicManager = null;

    /**
     * Logger instance for logging within this class
     */
    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Overriden JavaFX Application init function, loads fonts.
     */
    @Override
    public void init() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/SpaceMono-Regular.ttf"), 40);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 40);
        logger.log(Level.INFO, "Fonts loaded.");
    }

    /**
     * Overriden JavaFX application start method. Starts the JavaFX application
     * with the main stage. Loads application icons. The rest of the application logic
     * is handled scene controllers
     * @param stage main stage
     * @throws IOException when IO operations
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        // setup music manager
        musicManager = new MusicManager("/audio/soundtrack.mp3");

        // load intro scene and setup its controller
        URL introFXML = getClass().getResource("/intro.fxml");
        if (introFXML == null) {
            logger.log(Level.SEVERE, "Could not find intro.fxml file.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(introFXML);
        Parent root = loader.load();
        IntroController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setHostServices(getHostServices());

        // load application icons
        loadAppIcon("/images/icons/app/icon_16.jpg");
        loadAppIcon("/images/icons/app/icon_32.jpg");
        loadAppIcon("/images/icons/app/icon_64.jpg");
        loadAppIcon("/images/icons/app/icon_128.jpg");
        loadAppIcon("/images/icons/app/icon_192.jpg");
        logger.log(Level.INFO, "App icons loaded.");

        Scene scene = new Scene(root);
        stage.setTitle("Light Bulbs Game");
        stage.setScene(scene);

        controller.setup();
        stage.show();
        logger.log(Level.INFO, "GUI started.");
    }

    /**
     * Loads and setups an application icon from given path
     * @param path to the icon
     */
    private void loadAppIcon(String path) {
        InputStream iconStream = getClass().getResourceAsStream(path);
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
            logger.log(Level.INFO, "App icon loaded from path: " + path);
        } else {
            logger.log(Level.WARNING, "Could not load app icon from path: " + path);
        }
    }

    /**
     * GUI application entry point
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, "Unhandled Exception.", e);
        }
    }
}
