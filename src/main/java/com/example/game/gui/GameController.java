/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: GameController.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Controller for the game scene
 */

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

/**
 * Controller for the Game scene.
 * Handles all logic associated with user playing the game.
 * Handles buttons available in the Game scene
 * Uses Game model for all game related tasks.
 */
public class GameController extends Controller implements GUIObservable.GUIObserver, TimerObservable.TimerObserver {

    /**
     * Size of the game cell in the grid
     */
    private final int CELL_SIZE = 40;

    /**
     * Number of pixels to pad the height
     */
    private final int PADD_HEIGHT = 300;

    /**
     * Delay for each step of the auto solver
     */
    private final int SOLVE_DELAY = 250;

    /**
     * Game instance to work with
     */
    private ToolEnvironment game;

    /**
     * Grid of ImageViews of the game cells
     */
    private ImageView[][] imageGrid = null;

    /**
     * Tooltips of the ImageViews
     */
    private Tooltip[][] tooltipGrid = null;

    /**
     * Whether auto solving is currently running
     */
    private boolean autoSolving = false;

    /**
     * Whether the game is now locked (solving or finished)
     */
    private boolean locked = false;

    /**
     * Is game finished (won or lost)?
     */
    private boolean finished = false;

    /**
     * Contains stage of HelpView if its window is opened
     */
    private Stage helpViewStage = null;

    /**
     * Contains stage of HowToPlay if its window is opened
     */
    private Stage howToPlayStage = null;

    /**
     * Stores the last remaining count of seconds (timer)
     */
    private int lastTime = 0;

    /**
     * Undo Button in the Scene
     */
    @FXML
    private Button undoButton;

    /**
     * Redo Button in the Scene
     */
    @FXML
    private Button redoButton;

    /**
     * Save Button in the Scene
     */
    @FXML
    private Button saveButton;

    /**
     * Solve Button in the scene
     */
    @FXML
    private Button solveButton;

    /**
     * HelpView Button in the scene
     */
    @FXML
    private Button helpViewButton;

    /**
     * GamePane (the actual scene)
     */
    @FXML
    private BorderPane gamePane;

    /**
     * Label with the main text (hurry up, won, lost)
     */
    @FXML
    private Label mainLabel;

    /**
     * Icon of the timer
     */
    @FXML
    private ImageView timerIcon;

    /**
     * Label showing the remaining time
     */
    @FXML
    private Label timerLabel;

    /**
     * Game grid (here are spawned the cells as image views)
     */
    @FXML
    private GridPane gameGrid;

    /**
     * Label showing the amount of turns used
     */
    @FXML
    private Label turnsLabel;

    /**
     * Label with the difficulty or game type
     */
    @FXML
    private Label difficultyLabel;

    /**
     * Label showing how many bulbs are lit up
     */
    @FXML
    private Label litUpLabel;

    /**
     * Set the game model to provide instance
     * Sets up this instance as observer for GUI and Timer events
     * @param game instance to set to
     */
    public void setGameModel(Game game) {
        this.game = game;
        // setup observables
        game.setupGUIObserver(this);
        game.setupTimerObserver(this);
    }

    /**
     * Overriden setup method, prepares everything so the game can start
     * @param time maximum duration of the game
     * @param difficulty text
     */
    public void setup(int time, String difficulty) {
        WIDTH = 1100; // override width
        HEIGHT = 400; // pverride height
        super.setup(); // setup in parent class

        // set logic on window close
        stage.setOnCloseRequest(event -> {
            // if game is not finished show confirmation dialog
            if (!finished) {
                String title = "Quitting the Game";
                String header = "Are you sure you want to quit the game?";
                String content = "All unsaved progress will be lost!";

                if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
                    event.consume();
            }

            // stop timers and solvers if running, also close all opened windows

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

        // if timer was not already started, start it
        if (time != 0)
            game.startTimer(time);

        // setup everything else
        setupTimerIcon();
        updateTurns();
        setDifficulty(difficulty);
        updateLitUp();
        generateGameGrid(); // generate the game map
        updateUndoRedoLocking();
    }

    /**
     * Controls whether Undo and Redo buttons are toggleable
     * based on the sate of the game model
     */
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

    /**
     * Configures the timer icon
     */
    private void setupTimerIcon() {
        // set dimensions
        timerIcon.setFitWidth(40);
        timerIcon.setFitHeight(40);

        // get the stream
        InputStream alarmIconStream = getClass().getResourceAsStream("/images/icons/alarm_icon.png");
        if (alarmIconStream == null) { // fail
            logger.log(Level.SEVERE, "Failed to load alarm_icon.png");
            String title = "Failed to load alarm_icon.png";
            String header = "Failed to load alarm_icon.png";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        // load the icon
        Image image = new Image(alarmIconStream);
        timerIcon.setImage(image);
    }

    /**
     * Update the turns label
     */
    private void updateTurns() {
        turnsLabel.setText("Turns: " + game.getUsedTurnsTotal());
    }

    /**
     * Set the difficulty label
     * @param difficulty text to use
     */
    private void setDifficulty(String difficulty) {
        difficultyLabel.setText(difficulty);
    }

    /**
     * Update the lit up bulbs label
     */
    private void updateLitUp() {
        int litUp = game.getBulbsOn();
        int total = game.getBulbsTotal();
        litUpLabel.setText("Lit up: " + litUp + "/" + total);
    }

    /**
     * Update timer label, called by observables when second passes
     * @param seconds remaining time until the timer reaches 0.
     */
    public void updateTimer(int seconds) {
        lastTime = seconds;
        // build the string representation in format MM:SS
        String timeLeft = String.format("%02d:%02d", seconds / 60, seconds % 60);
        timerLabel.setText(timeLeft);

        if (seconds <= 0) {
            transitionToGameLost();
        }
    }

    /**
     * Update GUI when redraw is requested by the Game model observable
     */
    public void updateGUI() {
        // update turns and bulb labels
        updateLitUp();
        updateTurns();

        // if the winning condition is met transition to won state
        if (game.hasWon()) {
            transitionToGameWon();
        }

        int rows = game.rows();
        int cols = game.cols();

        // update all ImageViews to correct images and tooltips
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                applyCorrectCellImage(row, col); // applies single image based on its state and rotation
                String tooltipText = "Empty";
                if (!game.fieldAt(row, col).isEmpty())
                    tooltipText = "Turns: " + game.fieldAt(row, col).usedTurns();
                updateCellTooltip(row, col, tooltipText);
            }
        }
    }

    /**
     * Generates the game grid (ImageViews with Tooltips), adjusts window size
     * so that all cells fit nicely
     */
    private void generateGameGrid() {
        int rows = game.rows();
        int cols = game.cols();

        imageGrid = new ImageView[rows][cols];
        tooltipGrid = new Tooltip[rows][cols];

        gameGrid.getChildren().clear();

        // generate all game cells
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // new game cell
                ImageView cell = new ImageView();
                cell.setFitWidth(CELL_SIZE);
                cell.setFitHeight(CELL_SIZE);
                applyCorrectCellImage(row, col);
                cell.getStyleClass().add("game-cell");

                // new tooltip
                Tooltip tooltip = new Tooltip();
                if (game.fieldAt(row, col).isEmpty())
                    tooltip.setText("Empty");
                else
                    tooltip.setText("Turns: " + game.fieldAt(row, col).usedTurns());

                Tooltip.install(cell, tooltip);
                tooltipGrid[row][col] = tooltip;

                final int currentRow = row;
                final int currentCol = col;

                // on mouse click rotate the given cell and update undo redo buttons visibility
                cell.setOnMouseClicked(event -> {
                    if (!locked) {
                        ToolField node = game.fieldAt(currentRow, currentCol);
                        if (node.isEmpty())
                            return;
                        node.turn();
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

    /**
     * Update given tooltip at given coordinates to specified text
     * @param row of the tooltip
     * @param col of the tooltip
     * @param text to set
     */
    private void updateCellTooltip(int row, int col, String text) {
        Tooltip tooltip = tooltipGrid[row][col];

        if (tooltip == null) {
            return;
        }

        tooltip.setText(text);
    }

    /**
     * Applies correct cell image based on cell type and its orientation.
     * Assembles path to correct icon and uses ImageCache to obtain it.
     * @param row of the cell
     * @param col of the cell
     */
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
        // obtain the image
        Image image = ImageCache.getInstance().getImage(imageNameBuilder.toString());

        if (image == null) {
            return;
        }

        cell.setImage(image);
    }

    /**
     * Transition to won state, game is locked and finished,
     * timer and solver is stopped. Some buttons in menu button disabled and
     * main text changed together with the background.
     */
    private void transitionToGameWon() {
        locked = true;
        finished = true;
        game.stopTimer();
        game.stopSolver();
        autoSolving = false;
        if (game.usedSolver())
            mainLabel.setText("You won the game, but with a solver ...");
        else
            mainLabel.setText("Congratulations, you won the game!");

        // disable buttons in game menu
        helpViewButton.setDisable(true);
        saveButton.setDisable(true);
        solveButton.setDisable(true);
        undoButton.setDisable(true);
        redoButton.setDisable(true);

        // close help view stage if opened
        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }

        // change background
        gamePane.getStyleClass().remove("background");
        gamePane.getStyleClass().add("backgroundWon");
    }

    /**
     * Transition to lost state, game is locked and finished,
     * timer and solver is stopped. Some buttons in menu button disabled and
     * main text changed together with the background.
     */
    private void transitionToGameLost() {
        locked = true;
        finished = true;
        game.stopTimer();
        game.stopSolver();
        if (game.usedSolver())
            mainLabel.setText("You failed even with the solver?");
        else
            mainLabel.setText("Oh no you lost the game!");

        // help view stage is closed if opened
        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }

        // disable buttons in game menu
        helpViewButton.setDisable(true);
        saveButton.setDisable(true);
        solveButton.setDisable(true);
        undoButton.setDisable(true);
        redoButton.setDisable(true);

        // redraw tooltips to different format
        for (int row = 0; row < game.rows(); row++) {
            for (int col = 0; col < game.cols(); col++) {
                ToolField node = game.fieldAt(row, col);
                String tooltipText = "Empty";
                if (!node.isEmpty())
                    tooltipText = "Turns/Remaining: " + node.usedTurns() + "/" + node.remainingTurns();
                updateCellTooltip(row, col, tooltipText);
            }
        }

        gamePane.getStyleClass().remove("background");
        gamePane.getStyleClass().add("backgroundLost");
    }


    /**
     * Handles click on Main Menu button. Takes care
     * of transition to intro scene.
     * @throws IOException on IO error
     */
    @FXML
    public void handleToMainMenu() throws IOException {
        // if game is not finished yet ask the user for confirmation
        if (!finished) {
            String title = "Going back to Main Menu";
            String header = "Are you sure you want to go back to Main Menu?";
            String content = "All unsaved progress will be lost!";
            if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
                return;
        }

        // close help view stage if opened
        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }
        // close how to play stage if opened
        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }

        // stop timer and solver
        game.stopTimer();
        game.stopSolver();

        // load intro scene, setup controller and change active scene
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
        controller.setHostServices(hostServices);
        controller.setup();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        logger.log(Level.INFO, "Switched to Main Menu scene.");
    }

    /**
     * Handles Quit button, closes the application
     */
    @FXML
    public void handleQuit() {
        // asks for user's confirmation if game was not yet completed
        if (!finished) {
            String title = "Quitting the Game";
            String header = "Are you sure you want to quit the game?";
            String content = "All unsaved progress will be lost!";
            if (!showDialog(Alert.AlertType.CONFIRMATION, title, header, content))
                return;
        }

        // close HelpView if opened
        if (helpViewStage != null) {
            helpViewStage.close();
            helpViewStage = null;
        }
        // close HowToPlay stage if opened
        if (howToPlayStage != null) {
            howToPlayStage.close();
            howToPlayStage = null;
        }

        // stop timer and solver, close the app
        game.stopTimer();
        game.stopSolver();
        stage.close();
        logger.log(Level.INFO, "Quitting game.");
    }

    /**
     * Handles Show HowToPlay button, opens HowToPlay window
     * @throws IOException on IO error
     */
    @FXML
    public void handleShowHowToPlay() throws IOException {
        // only one window allowed
        if (howToPlayStage != null) {
            return;
        }

        // load HowToPlay scene, set up its controller and open it in new window
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

        // setup close event for the window
        howToPlayStage.setOnCloseRequest(event -> {
            howToPlayStage = null;
            logger.log(Level.INFO, "Closed How To Play stage.");
        });

    }

    /**
     * Handles HelpView buttons, opens HelpView window
     * @throws IOException on IO error
     */
    @FXML
    public void handleShowHelpView() throws IOException {
        // only one help view window allowed
        if (helpViewStage != null) {
            return;
        }

        // load HelpView scene, set up its controller and open it in new window
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

        // configure close event for the window
        helpViewStage.setOnCloseRequest(event -> {
            helpViewStage = null;
            logger.log(Level.INFO, "Closed Help View stage.");
        });
    }

    /**
     * Handles Solve button, toggles auto solving functionality
     * Uses underlying Game model
     */
    @FXML
    public void handleSolve() {
        // if already solving -> turn it off and allow the player
        // to continue playing
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

        // otherwise lock the game and some game menu buttons
        // and start auto solving process

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

    /**
     * Handles Save button click. Asks user for name and location of
     * the save file via FileChooser dialog. Then it attempts to save
     * the game into that file. Uses Game model.
     */
    @FXML
    public void handleSave() {
        // ask user for file location and name
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON game save file", "*.json"));
        File file = fileChooser.showSaveDialog(stage);

        if (file == null) { // no file selected
            logger.log(Level.INFO, "No file selected.");
            String title = "Save file error";
            String header = "No file selected";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
            return;
        }

        // obtain the actual path
        String path = file.getAbsolutePath();
        if (!path.endsWith(".json")) // in case it does not end in .json extension
            path += ".json"; // append it
        logger.log(Level.INFO, "Saving game to file: " + path);

        try { // attempt to save the game
            game.save(path);

            String title = "Game saved";
            String header = "Game saved successfully";
            String content = "Continue, time is ticking.";
            showDialog(Alert.AlertType.INFORMATION, title, header, content);

        } catch (IOException e) { // error occurred during saving of the game
            logger.log(Level.WARNING, "Failed to save game to file: " + path, e);
            String title = "Failed to save game";
            String header = "File is not a valid game save file";
            String content = "Please try again.";
            showDialog(Alert.AlertType.WARNING, title, header, content);
        }

    }

    /**
     * Handles Undo button click. Last turn is undone via the Game model.
     */
    @FXML
    public void handleUndo() {
        game.undo();
        updateUndoRedoLocking();
    }

    /**
     * Handles Redo button click. Last undone turn is redone via the Game model.
     */
    @FXML
    public void handleRedo() {
        game.redo();
        updateUndoRedoLocking();
    }

}
