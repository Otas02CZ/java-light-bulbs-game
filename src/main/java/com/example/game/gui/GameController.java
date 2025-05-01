package com.example.game.gui;

import com.example.game.gui.shared.ImageCache;
import com.example.game.model.Game;
import com.example.game.shared.GUIObservable;
import com.example.game.shared.TimerObservable;
import com.example.game.shared.ToolEnvironment;
import com.example.game.shared.ToolField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

public class GameController extends Controller implements GUIObservable.GUIObserver, TimerObservable.TimerObserver {

    private final int CELL_SIZE = 40;
    private final int PADD_HEIGHT = 300;

    private final int SOLVE_DELAY = 250;

    private ToolEnvironment game;

    private int turns;

    private ImageView[][] imageGrid = null;
    private Tooltip[][] tooltipGrid = null;

    private boolean autoSolving = false;

    private boolean locked = false;

    private Stage helpViewStage = null;

    private Stage howToPlayStage = null;

    private int lastTime = 0;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button solveButton;

    @FXML
    private Button helpViewButton;

    @FXML
    private BorderPane gamePane;

    @FXML
    private Label mainLabel;

    @FXML
    private ImageView timerIcon;

    @FXML
    private Label timerLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label turnsLabel;

    @FXML
    private Label difficultyLabel;

    @FXML
    private Label litUpLabel;

    public void setGameModel(Game game) {
        this.game = game;
        game.setupGUIObserver(this);
        game.setupTimerObserver(this);
    }

    public void setup(int time, String difficulty) {
        WIDTH = 1100;
        HEIGHT = 400;
        super.setup();

        stage.setOnCloseRequest(event -> {
            if (!game.hasWon()) {
                String title = "Quitting the Game";
                String header = "Are you sure you want to quit the game?";
                String content = "All unsaved progress will be lost!";

                if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
                    event.consume();
            }

            game.stopTimer();
            game.stopSolver();
            if (helpViewStage != null) {
                helpViewStage.close();
                helpViewStage = null;
            }

            if (howToPlayStage != null) {
                howToPlayStage.close();
                howToPlayStage = null;
            }
        });

        if (time != 0)
            game.startTimer(time);

        setupTimerIcon();
        updateTurns();
        setDifficulty(difficulty);
        updateLitUp();
        generateGameGrid();
        updateUndoRedoLocking();
    }

    private void updateUndoRedoLocking() {
        if (game.canUndo())
            undoButton.setDisable(false);
        else
            undoButton.setDisable(true);

        if (game.canRedo())
            redoButton.setDisable(false);
        else
            redoButton.setDisable(true);
    }

    private void setupTimerIcon() {
        timerIcon.setFitWidth(40);
        timerIcon.setFitHeight(40);

        InputStream alarmIconStream = getClass().getResourceAsStream("/images/icons/alarm_icon.png");
        if (alarmIconStream == null) {
            logger.log(Level.SEVERE, "Failed to load alarm_icon.png");
            String title = "Failed to load alarm_icon.png";
            String header = "Failed to load alarm_icon.png";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        Image image = new Image(alarmIconStream);

        timerIcon.setImage(image);
    }

    private void updateTurns() {
        turnsLabel.setText("Turns: " + turns);
    }

    private void setDifficulty(String difficulty) {
        difficultyLabel.setText(difficulty);
    }

    private void updateLitUp() {
        int litUp = game.getBulbsOn();
        int total = game.getBulbsTotal();
        litUpLabel.setText("Lit up: " + litUp + "/" + total);
    }

    public void updateTimer(int seconds) {
        lastTime = seconds;
        String timeLeft = String.format("%02d:%02d", seconds / 60, seconds % 60);
        timerLabel.setText(timeLeft);

        if (seconds <= 0) {
            transitionToGameLost();
        }
    }

    public void updateGUI() {
        updateLitUp();

        if (game.hasWon()) {
            transitionToGameWon();
        }

        int rows = game.rows();
        int cols = game.cols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                applyCorrectCellImage(row, col);
                String tooltipText = "Empty";
                if (!game.fieldAt(row, col).isEmpty())
                    tooltipText = "Turns: " + game.fieldAt(row, col).usedTurns();
                updateCellTooltip(row, col, tooltipText);
            }
        }
    }

    private void generateGameGrid() {
        int rows = game.rows();
        int cols = game.cols();

        imageGrid = new ImageView[rows][cols];
        tooltipGrid = new Tooltip[rows][cols];

        gameGrid.getChildren().clear();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ImageView cell = new ImageView();
                cell.setFitWidth(CELL_SIZE);
                cell.setFitHeight(CELL_SIZE);
                applyCorrectCellImage(row, col);
                cell.getStyleClass().add("game-cell");

                Tooltip tooltip = new Tooltip();
                if (game.fieldAt(row, col).isEmpty())
                    tooltip.setText("Empty");
                else
                    tooltip.setText("Turns: " + game.fieldAt(row, col).usedTurns());

                Tooltip.install(cell, tooltip);
                tooltipGrid[row][col] = tooltip;

                final int currentRow = row;
                final int currentCol = col;

                cell.setOnMouseClicked(event -> {
                    if (!locked) {
                        ToolField node = game.fieldAt(currentRow, currentCol);
                        if (node.isEmpty())
                            return;
                        node.turn();
                        turns++;
                        updateTurns();
                        updateUndoRedoLocking();
                    }
                });

                gameGrid.add(cell, col, row);
                imageGrid[row][col] = cell;
            }
        }
        logger.log(Level.INFO, "Generated game grid.");

        // Calculate preferred size for the Stage
        double gridHeight = (CELL_SIZE) * rows;
        double totalHeight = Math.max(HEIGHT, gridHeight + PADD_HEIGHT); // Extra for toolbar, labels, etc.

        stage.setHeight(totalHeight);
        stage.setMinHeight(totalHeight);
        stage.setMaxHeight(totalHeight);
        logger.log(Level.INFO, "Game Scene size adjusted.");
    }

    private void updateCellTooltip(int row, int col, String text) {
        Tooltip tooltip = tooltipGrid[row][col];

        if (tooltip == null) {
            return;
        }

        tooltip.setText(text);
    }

    private void applyCorrectCellImage(int row, int col) {
        StringBuilder imageNameBuilder = new StringBuilder("/images/icons/");
        ImageView cell = imageGrid[row][col];
        if (cell == null) {
            return;
        }
        // start type decoding
        ToolField node = game.fieldAt(row, col);

        if (node.isBulb())
            imageNameBuilder.append("bulb");
        else if (node.isLink())
            imageNameBuilder.append("wire");
        else if (node.isPower())
            imageNameBuilder.append("power");
        else
            imageNameBuilder.append("empty");

        if (node.isLink() || node.isBulb()) {
            if (node.light())
                imageNameBuilder.append("_on");
            else
                imageNameBuilder.append("_off");
        }

        if (node.isPower() || node.isBulb() || node.isLink()) {
            if (node.north())
                imageNameBuilder.append("_1");
            else
                imageNameBuilder.append("_0");

            if (node.east())
                imageNameBuilder.append("1");
            else
                imageNameBuilder.append("0");

            if (node.south())
                imageNameBuilder.append("1");
            else
                imageNameBuilder.append("0");

            if (node.west())
                imageNameBuilder.append("1");
            else
                imageNameBuilder.append("0");
        }

        imageNameBuilder.append(".jpg");

        Image image = ImageCache.getInstance().getImage(imageNameBuilder.toString());

        if (image == null) {
            return;
        }

        cell.setImage(image);
    }

    private void transitionToGameWon() {
        locked = true;
        game.stopTimer();
        game.stopSolver();
        autoSolving = false;
        mainLabel.setText("You won!");

        helpViewButton.setDisable(true);
        saveButton.setDisable(true);
        solveButton.setDisable(true);
        undoButton.setDisable(true);
        redoButton.setDisable(true);

        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }
        //TODO: change background
    }

    private void transitionToGameLost() {
        locked = true;
        game.stopTimer();
        game.stopSolver();
        mainLabel.setText("You lost!");

        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }

        helpViewButton.setDisable(true);
        saveButton.setDisable(true);
        solveButton.setDisable(true);
        undoButton.setDisable(true);
        redoButton.setDisable(true);

        for (int row = 0; row < game.rows(); row++) {
            for (int col = 0; col < game.cols(); col++) {
                ToolField node = game.fieldAt(row, col);
                String tooltipText = "Empty";
                if (!node.isEmpty())
                    tooltipText = "Turns/Remaining: " + node.usedTurns() + "/" + node.remainingTurns();
                updateCellTooltip(row, col, tooltipText);
            }
        }

        //TODO: change background
    }




    @FXML
    public void handleToMainMenu() throws IOException {
        if (!game.hasWon()) {
            String title = "Going back to Main Menu";
            String header = "Are you sure you want to go back to Main Menu?";
            String content = "All unsaved progress will be lost!";
            if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
                return;
        }

        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }

        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }

        game.stopTimer();
        game.stopSolver();

        URL introFXML = getClass().getResource("/intro.fxml");
        if (introFXML == null) {
            logger.log(Level.SEVERE, "Failed to load intro.fxml");
            String title = "Failed to load intro.fxml";
            String header = "Failed to load intro.fxml";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        FXMLLoader loader = new FXMLLoader(introFXML);
        Parent root = loader.load();
        IntroController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setup();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        logger.log(Level.INFO, "Switched to Main Menu scene.");
    }

    @FXML
    public void handleQuit() {
        if (!game.hasWon()) {
            String title = "Quitting the Game";
            String header = "Are you sure you want to quit the game?";
            String content = "All unsaved progress will be lost!";
            if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
                return;
        }

        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }

        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }

        game.stopTimer();
        game.stopSolver();
        stage.close();
        logger.log(Level.INFO, "Quitting game.");
    }

    @FXML
    public void handleShowHowToPlay() throws IOException {
        if (howToPlayStage != null) {
            return;
        }

        URL howToPlayFXML = getClass().getResource("/howToPlay.fxml");
        if (howToPlayFXML == null) {
            logger.log(Level.SEVERE, "Failed to load howToPlay.fxml");
            String title = "Failed to load howToPlay.fxml";
            String header = "Failed to load howToPlay.fxml";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        FXMLLoader loader = new FXMLLoader(howToPlayFXML);
        Parent root = loader.load();

        HowToPlayController howToPlayController = loader.getController();

        howToPlayStage = new Stage();

        howToPlayController.setStage(howToPlayStage);
        howToPlayController.setup();

        howToPlayStage.setTitle("How To Play");
        howToPlayStage.setScene(new Scene(root));
        howToPlayStage.show();
        logger.log(Level.INFO, "Opened How To Play stage.");

        howToPlayStage.setOnCloseRequest(event -> {
            howToPlayStage = null;
            logger.log(Level.INFO, "Closed How To Play stage.");
        });

    }

    @FXML
    public void handleShowHelpView() throws IOException {
        if (helpViewStage != null) {
            return;
        }

        URL helpViewFXML = getClass().getResource("/helpView.fxml");
        if (helpViewFXML == null) {
            logger.log(Level.SEVERE, "Failed to load helpView.fxml");
            String title = "Failed to load helpView.fxml";
            String header = "Failed to load helpView.fxml";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        FXMLLoader loader = new FXMLLoader(helpViewFXML);
        Parent root = loader.load();
        HelpViewController helpViewController = loader.getController();

        helpViewStage = new Stage();

        helpViewController.setGame((Game)game);
        helpViewController.setStage(helpViewStage);
        helpViewController.setup();

        helpViewStage.setTitle("Help View");
        helpViewStage.setScene(new Scene(root));
        helpViewStage.show();
        logger.log(Level.INFO, "Opened Help View stage.");

        helpViewStage.setOnCloseRequest(event -> {
            helpViewStage = null;
            logger.log(Level.INFO, "Closed Help View stage.");
        });
    }

    @FXML
    public void handleSolve() {
        if (autoSolving) {
            solveButton.setText("Auto Solving");
            saveButton.setDisable(false);
            mainLabel.setText("Hurry up! Time is Ticking!");
            game.stopSolver();
            autoSolving = false;
            locked = false;
            game.startTimer(lastTime);
            updateUndoRedoLocking();
            return;
        }

        locked = true;
        autoSolving = true;

        undoButton.setDisable(true);
        redoButton.setDisable(true);
        saveButton.setDisable(true);
        solveButton.setText("Stop Solving");
        mainLabel.setText("Auto solving ...");

        game.stopTimer();

        game.solve(SOLVE_DELAY);
    }

    @FXML
    public void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON game save file", "*.json"));
        File file = fileChooser.showSaveDialog(stage);

        if (file == null) {
            logger.log(Level.INFO, "No file selected.");
            String title = "Save file error";
            String header = "No file selected";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        String path = file.getAbsolutePath();
        logger.log(Level.INFO, "Saving game to file: " + path);

        try {
            game.save(path);

            String title = "Game saved";
            String header = "Game saved successfully";
            String content = "Continue, time is ticking.";
            showDialog(Alert.AlertType.INFORMATION, title, header, content);

        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to save game to file: " + path, e);
            String title = "Failed to save game";
            String header = "File is not a valid game save file";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
        }

    }

    @FXML
    public void handleUndo() {
        game.undo();
        updateUndoRedoLocking();
    }

    @FXML
    public void handleRedo() {
        game.redo();
        updateUndoRedoLocking();
    }

}
