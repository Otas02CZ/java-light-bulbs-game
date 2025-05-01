package com.example.game.gui;

import com.example.game.model.Game;
import com.example.game.shared.GUIObservable;
import com.example.game.shared.ToolEnvironment;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.util.logging.Level;

public class HelpViewController extends Controller implements GUIObservable.GUIObserver {

    private final int CELL_SIZE = 40;
    private final int PADD_HEIGHT = 150;
    private final int PADD_WIDTH = 80;

    private ToolEnvironment game = null;

    private Label[][] labelGrid = null;

    @FXML
    private GridPane helpViewGrid;

    @FXML
    private BorderPane helpViewPane;

    public void setGame(Game game) {
        this.game = game;
        game.setupGUIObserver(this);
    }

    public void setup() {
        WIDTH = 260;
        HEIGHT = 300;
        super.setup();
        generateHelpViewGrid();
    }

    private void generateHelpViewGrid() {
        int rows = game.rows();
        int cols = game.cols();

        labelGrid = new Label[rows][cols];

        helpViewGrid.getChildren().clear();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Label label = new Label();
                label.setMinWidth(CELL_SIZE);
                label.setMinHeight(CELL_SIZE);
                label.setMaxWidth(CELL_SIZE);
                label.setMaxHeight(CELL_SIZE);
                label.getStyleClass().add("game-cell");
                label.getStyleClass().add("help-view-cell");
                label.setAlignment(Pos.CENTER);          // Centers text *inside* the label
                label.setTextAlignment(TextAlignment.CENTER);
                label.setText(Integer.toString(game.fieldAt(row, col).remainingTurns()));

                labelGrid[row][col] = label;
                helpViewGrid.add(label, col, row);
            }
        }
        logger.log(Level.INFO, "Help View grid generated.");

        // Calculate preferred size for the Stage
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

    public void updateGUI() {
        for (int row = 0; row < game.rows(); row++) {
            for (int col = 0; col < game.cols(); col++) {
                labelGrid[row][col].setText(Integer.toString(game.fieldAt(row, col).remainingTurns()));
            }
        }
    }

}
