package com.example.game.gui;

import com.example.game.gui.shared.GameConfig;
import com.example.game.model.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 */
public class GameSetupController {

    private final int SIZE_MIN = 5;
    private final int SIZE_MAX = 15;
    private final int TIME_MIN = 1;
    private final int TIME_MAX = 60;
    private final int SHUFFLE_CONST = 1;

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private Stage stage;

    private HashMap<String, GameConfig> defaultConfigs;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Spinner<Integer> sizeSpinner;

    @FXML
    private Slider emptySlider;

    @FXML
    private Slider shuffleSlider;

    @FXML
    private Spinner<Integer> timeSpinner;

    public GameSetupController() {
        setupConfig();
    }

    public void setup() {
        setupWindowSize();
        initialize();
    }

    private void setupConfig() {
        this.defaultConfigs = new HashMap<>();

        this.defaultConfigs.put("Easy", new GameConfig("Easy", 5, 0.7, 0.3, 10));
        this.defaultConfigs.put("Medium", new GameConfig("Medium", 10, 0.25, 0.6, 10));
        this.defaultConfigs.put("Hard", new GameConfig("Hard", 15, 0.1, 1, 15));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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

    public void initialize() {
        emptySlider.setMax(1.0);
        emptySlider.setMin(0.0);

        shuffleSlider.setMax(1.0);
        shuffleSlider.setMin(0.0);

        GameConfig defaultConfig = this.defaultConfigs.get("Medium");
        difficultyComboBox.setValue(defaultConfig.getName());

        emptySlider.setValue(defaultConfig.getEmptyCoef());
        shuffleSlider.setValue(defaultConfig.getShuffleCoef());

        setupSpinner(sizeSpinner, SIZE_MIN, SIZE_MAX, defaultConfig.getSize());
        setupSpinner(timeSpinner, TIME_MIN, TIME_MAX, defaultConfig.getMaxTime());
    }

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

    private void commitEditorText(Spinner<Integer> spinner, int min, int max) {
        String text = spinner.getEditor().getText();
        try {
            int entered = Integer.parseInt(text);
            // Clamp value into allowed range
            if (entered < min) entered = min;
            if (entered > max) entered = max;
            spinner.getValueFactory().setValue(entered);
        } catch (NumberFormatException e) {
            // Reset to current spinner value if invalid input
            spinner.getEditor().setText(spinner.getValue().toString());
        }
    }

    @FXML
    private void onDifficultyChange() {
        System.out.println("Difficulty changed!");
        String difficulty = difficultyComboBox.getValue();

        if (difficulty == null || difficulty.equals("Custom"))
            return;

        GameConfig config = this.defaultConfigs.get(difficulty);

        sizeSpinner.getValueFactory().setValue(config.getSize());
        emptySlider.setValue(config.getEmptyCoef());
        shuffleSlider.setValue(config.getShuffleCoef());
        timeSpinner.getValueFactory().setValue(config.getMaxTime());
    }

    @FXML
    private void onBackClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/intro.fxml"));
        Parent root = loader.load();
        IntroController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setup();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void onPlayClicked() throws IOException {
        System.out.println("Play clicked!");
        // Read form values
        String difficulty = difficultyComboBox.getValue();
        int size = sizeSpinner.getValue();
        double emptyCoef = emptySlider.getValue();
        double shuffleCoef = shuffleSlider.getValue();
        int time = timeSpinner.getValue();

        System.out.println("Difficulty: " + difficulty);
        System.out.println("Size: " + size);
        System.out.println("Empty cells: " + emptyCoef);
        System.out.println("Shuffle coefficient: " + shuffleCoef);
        System.out.println("Time: " + time);

        this.startGame();
    }

    private void startGame() throws IOException {
        String difficulty = difficultyComboBox.getValue();
        int size = sizeSpinner.getValue();
        double emptyCoef = emptySlider.getValue() / 3;
        double shuffleCoef = shuffleSlider.getValue();
        int time = timeSpinner.getValue();

        int shuffles = (int) (size * size * shuffleCoef * SHUFFLE_CONST);

        Game game = Game.generate(size, size, (float) emptyCoef, shuffles);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/game.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setGameModel(game);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        controller.setup(time*60, difficulty);
    }
}
