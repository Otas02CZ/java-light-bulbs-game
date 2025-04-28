package com.example.game.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Spinner<Integer> sizeSpinner;

    @FXML
    private Spinner<Integer> emptyCellsSpinner;

    @FXML
    private Spinner<Integer> shuffleSpinner;

    @FXML
    private Spinner<Integer> timeSpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSpinner(sizeSpinner, 5, 20, 10);
        setupSpinner(emptyCellsSpinner, 0, 100, 10);
        setupSpinner(shuffleSpinner, 0, 100, 50);
        setupSpinner(timeSpinner, 1, 60, 5);
        difficultyComboBox.setValue("Medium");
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
    private void onCancelClicked() {
        System.out.println("Cancel clicked!");
        // Add code to close window or return to menu
    }

    @FXML
    private void onPlayClicked() {
        System.out.println("Play clicked!");
        // Read form values
        String difficulty = difficultyComboBox.getValue();
        int size = sizeSpinner.getValue();
        int emptyCells = emptyCellsSpinner.getValue();
        int shuffleCoef = shuffleSpinner.getValue();
        int time = timeSpinner.getValue();

        System.out.println("Difficulty: " + difficulty);
        System.out.println("Size: " + size);
        System.out.println("Empty cells: " + emptyCells);
        System.out.println("Shuffle coefficient: " + shuffleCoef);
        System.out.println("Time: " + time);

        // Now you can proceed to start the game with these settings!
    }
}
