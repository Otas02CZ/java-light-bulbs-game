/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: HelpViewController.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Controller for the HelpView scene
 */

package com.example.game.gui;

import com.example.game.model.Game;
import com.example.game.shared.GUIObservable;
import com.example.game.shared.ToolEnvironment;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import java.util.logging.Level;

/**
 * Controller for the HelpVew scene
 * Shows a grid of labels that inform about remaining
 * turns at given positions
 */
public class HelpViewController extends Controller implements GUIObservable.GUIObserver {

    /**
     * Size of the cell in the grid (both X and Y)
     */
    private final int CELL_SIZE = 40;

    /**
     * Number of pixels to pad the height when changing window size
     */
    private final int PADD_HEIGHT = 150;

    /**
     * Number of pixels to pad the width when changing window size
     */
    private final int PADD_WIDTH = 80;

    /**
     * Game model element used to obtaion information
     */
    private ToolEnvironment game = null;

    /**
     * 2D array of labels that were placed to the grid
     */
    private Label[][] labelGrid = null;

    /**
     * Label grid, where the buttons are spawned
     */
    @FXML
    private GridPane helpViewGrid;

    /**
     * Sets Game model to specified instance
     * Sets up this instance as observer to GUI events
     * @param game instance to set to
     */
    public void setGame(Game game) {
        this.game = game;
        // setup this observer to be notified by changes needing GUI redraws
        game.setupGUIObserver(this);
    }

    /**
     * Controller setup
     */
    public void setup() {
        WIDTH = 260; // override default width
        HEIGHT = 300; // override default height
        super.setup(); // setup in the parent class
        generateHelpViewGrid(); // generate the grid of labels
    }

    /**
     * Generates the grid of labels in the HelpView scene.
     * These labels are also available from array list labelGrid.
     */
    private void generateHelpViewGrid() {
        int rows = game.rows();
        int cols = game.cols();

        labelGrid = new Label[rows][cols];

        helpViewGrid.getChildren().clear();

        // generate the label grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Label label = new Label(); // new label
                // set sizes
                label.setMinWidth(CELL_SIZE);
                label.setMinHeight(CELL_SIZE);
                label.setMaxWidth(CELL_SIZE);
                label.setMaxHeight(CELL_SIZE);
                // setup its classes
                label.getStyleClass().add("game-cell");
                label.getStyleClass().add("help-view-cell");
                label.setAlignment(Pos.CENTER);          // centers text *inside* the label
                label.setTextAlignment(TextAlignment.CENTER);
                // set default text
                label.setText(Integer.toString(game.fieldAt(row, col).remainingTurns()));

                labelGrid[row][col] = label;
                helpViewGrid.add(label, col, row);
            }
        }
        logger.log(Level.INFO, "Help View grid generated.");

        // calculate preferred size for the Stage
        double gridHeight = (CELL_SIZE) * rows;
        double gridWidth = (CELL_SIZE) * cols;
        double totalHeight = Math.max(HEIGHT, gridHeight + PADD_HEIGHT); // Extra for toolbar, labels, etc.
        double totalWidth = Math.max(WIDTH, gridWidth + PADD_WIDTH);

        stage.setHeight(totalHeight);
        stage.setMinHeight(totalHeight);
        stage.setMaxHeight(totalHeight);

        stage.setWidth(totalWidth);
        stage.setMinWidth(totalWidth);
        stage.setMaxWidth(totalWidth);

        logger.log(Level.INFO, "Help View stage size adjusted.");
    }

    /**
     * Called by observables when GUI redraws are necessary
     * Updates the labels to correct remaining turns
     */
    public void updateGUI() {
        for (int row = 0; row < game.rows(); row++) {
            for (int col = 0; col < game.cols(); col++) {
                labelGrid[row][col].setText(Integer.toString(game.fieldAt(row, col).remainingTurns()));
            }
        }
    }

}
