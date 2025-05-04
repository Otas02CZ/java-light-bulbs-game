/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: GameSetupController.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Controller for the game setup scene
 */

package com.example.game.gui;

import com.example.game.gui.shared.GameConfig;
import com.example.game.model.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Controls game setup scene
 * Handles buttons for configuration changes and switching views
 */
public class GameSetupController extends Controller {

    /**
     * Minimum size of game grid
     */
    private final int SIZE_MIN = 5;

    /**
     * Maximum size of game grid
     */
    private final int SIZE_MAX = 15;

    /**
     * Minimum time duration in minutes
     */
    private final int TIME_MIN = 1;

    /**
     * Maximum time duration in minutes
     */
    private final int TIME_MAX = 60;

    /**
     * Default shuffle constant
     */
    private final int SHUFFLE_CONST = 1;

    /**
     * Hash map of default configurations
     */
    private HashMap<String, GameConfig> defaultConfigs;

    /**
     * Whether configuration checks are disabled now
     * (when setting values programmatically)
     */
    private boolean disableConfigChecks = false;

    /**
     * Combo box for changing predefined difficulty presets
     */
    @FXML
    private ComboBox<String> difficultyComboBox;

    /**
     * Spinner for game map size
     */
    @FXML
    private Spinner<Integer> sizeSpinner;

    /**
     * Slider for setting empty cell coefficient
     */
    @FXML
    private Slider emptySlider;

    /**
     * Slider for setting shuffle strength
     */
    @FXML
    private Slider shuffleSlider;

    /**
     * Spinner for time duration selection
     */
    @FXML
    private Spinner<Integer> timeSpinner;

    /**
     * Setup controllers default configuration
     * This method needs to be executed first
     */
    public GameSetupController() {
        setupConfig();
    }

    /**
     * Controller setup and initialization
     */
    public void setup() {
        super.setup();
        initialize();
    }

    /**
     * Setup HashMap of default difficulties
     */
    private void setupConfig() {
        this.defaultConfigs = new HashMap<>();

        // add all default difficulties
        this.defaultConfigs.put("Easy", new GameConfig("Easy", 5, 0.7, 0.3, 10));
        this.defaultConfigs.put("Medium", new GameConfig("Medium", 10, 0.25, 0.6, 10));
        this.defaultConfigs.put("Hard", new GameConfig("Hard", 15, 0.1, 1, 15));
    }

    /**
     * Initialize constraints on sliders and spinners
     * Set default values
     */
    public void initialize() {
        emptySlider.setMax(1.0);
        emptySlider.setMin(0.0);

        shuffleSlider.setMax(1.0);
        shuffleSlider.setMin(0.0);

        GameConfig defaultConfig = this.defaultConfigs.get("Medium");
        difficultyComboBox.setValue(defaultConfig.getName());

        emptySlider.setValue(defaultConfig.getEmptyCoefficient());
        shuffleSlider.setValue(defaultConfig.getShuffleCoefficient());

        setupSpinner(sizeSpinner, SIZE_MIN, SIZE_MAX, defaultConfig.getSize());
        setupSpinner(timeSpinner, TIME_MIN, TIME_MAX, defaultConfig.getMaxTime());

        setupConfigListeners();
        logger.log(java.util.logging.Level.INFO, "Game Setup initialized.");
    }

    /**
     * Setup given spinner with provided configuration
     * @param spinner to setup
     * @param min value
     * @param max value
     * @param initialValue as default value
     */
    private void setupSpinner(Spinner<Integer> spinner, int min, int max, int initialValue) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);

        // Add listener to validate when focus is lost
        spinner.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // focus lost
                commitEditorText(spinner, min, max);
            }
        });
    }

    /**
     * Sets up listeners for all spinners and sliders
     * so that configuration could be changed to Custom
     * when user changes the values
     */
    private void setupConfigListeners() {
        // setup size spinner listener
        sizeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (disableConfigChecks) return;

            String difficulty = difficultyComboBox.getValue();

            if (difficulty == null || difficulty.equals("Custom"))
                return;

            int curDiffSize = defaultConfigs.get(difficultyComboBox.getValue()).getSize();
            if (newValue != curDiffSize) {
                difficultyComboBox.setValue("Custom");
            }
        });

        // setup time spinner listener
        timeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (disableConfigChecks) return;

            String difficulty = difficultyComboBox.getValue();

            if (difficulty == null || difficulty.equals("Custom"))
                return;

            int curDiffTime = defaultConfigs.get(difficultyComboBox.getValue()).getMaxTime();
            if (newValue != curDiffTime) {
                difficultyComboBox.setValue("Custom");
            }
        });

        // setup empty slider listener
        emptySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (disableConfigChecks) return;

            String difficulty = difficultyComboBox.getValue();

            if (difficulty == null || difficulty.equals("Custom"))
                return;

            double curDiffEmpty = defaultConfigs.get(difficultyComboBox.getValue()).getEmptyCoefficient();
            if (newValue.doubleValue() != curDiffEmpty) {
                difficultyComboBox.setValue("Custom");
            }
        });

        // setup shuffle strength slider listener
        shuffleSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (disableConfigChecks) return;

            String difficulty = difficultyComboBox.getValue();

            if (difficulty == null || difficulty.equals("Custom"))
                return;

            double curDiffShuffle = defaultConfigs.get(difficultyComboBox.getValue()).getEmptyCoefficient();
            if (newValue.doubleValue() != curDiffShuffle) {
                difficultyComboBox.setValue("Custom");
            }
        });
    }

    /**
     * Commit edited text to the spinner value and clamp the value (min, max)
     * @param spinner to work with
     * @param min to use
     * @param max to use
     */
    private void commitEditorText(Spinner<Integer> spinner, int min, int max) {
        String text = spinner.getEditor().getText();
        try {
            int entered = Integer.parseInt(text);
            // Clamp value into allowed range
            if (entered < min) entered = min;
            if (entered > max) entered = max;
            disableConfigChecks = true;
            spinner.getValueFactory().setValue(entered);
            disableConfigChecks = false;
        } catch (NumberFormatException e) {
            // Reset to current spinner value if invalid input
            spinner.getEditor().setText(spinner.getValue().toString());
        }
    }

    /**
     * Handle change of the difficulty ComboBox
     */
    @FXML
    private void onDifficultyChange() {
        String difficulty = difficultyComboBox.getValue();

        if (difficulty == null || difficulty.equals("Custom"))
            return;

        GameConfig config = this.defaultConfigs.get(difficulty);

        disableConfigChecks = true;
        sizeSpinner.getValueFactory().setValue(config.getSize());
        emptySlider.setValue(config.getEmptyCoefficient());
        shuffleSlider.setValue(config.getShuffleCoefficient());
        timeSpinner.getValueFactory().setValue(config.getMaxTime());
        disableConfigChecks = false;
    }

    /**
     * Handle Back button press
     * @throws IOException on IO error
     */
    @FXML
    private void onBackClicked() throws IOException {
        // load intro scene and setup controller
        URL introFXML = getClass().getResource("/intro.fxml");
        if (introFXML == null) {
            logger.log(Level.SEVERE, "Failed to load intro.fxml file.");
            String title = "Failed to load intro";
            String header = "Failed to load intro";
            String content = "Try again or restart.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        FXMLLoader loader = new FXMLLoader(introFXML);
        Parent root = loader.load();
        IntroController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setHostServices(hostServices);
        controller.setup();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        logger.log(java.util.logging.Level.INFO, "Switched to Intro scene.");
    }

    /**
     * Handle Play button click
     * @throws IOException on IO error
     */
    @FXML
    private void onPlayClicked() throws IOException {
        // obtain the configured values
        String difficulty = difficultyComboBox.getValue();
        int size = sizeSpinner.getValue();
        double emptyCoef = emptySlider.getValue() / 3; // ease the empty coefficient
        double shuffleCoef = shuffleSlider.getValue();
        int time = timeSpinner.getValue();

        // recalculate shuffle count
        int shuffles = (int) (size * size * shuffleCoef * SHUFFLE_CONST);

        // generate new game
        Game game = Game.generate(size, size, (float) emptyCoef, shuffles);

        // load game scene and setup its controller
        URL gameFXML = getClass().getResource("/game.fxml");
        if (gameFXML == null) {
            logger.log(Level.SEVERE, "Failed to load game.fxml file.");
            String title = "Failed to load game";
            String header = "Failed to load game";
            String content = "Try again or restart.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        FXMLLoader loader = new FXMLLoader(gameFXML);
        Parent root = loader.load();
        GameController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setHostServices(hostServices);
        controller.setGameModel(game);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        controller.setup(time*60, difficulty);
        logger.log(java.util.logging.Level.INFO, "Switched to Game scene.");
    }
}
