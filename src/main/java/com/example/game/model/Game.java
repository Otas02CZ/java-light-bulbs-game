/**
 * IJA HW: 2
 * FILE: Game.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class Game representing LightBulb game
 */

package com.example.game.model;

import com.example.game.model.common.*;
import com.example.game.shared.*;

import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents LightBulb game
 * implements ToolEnvironment - for GUI visualisation
 * implements GameObserver - for notifications informing about the need of recalculate connections
 */
public class Game implements ToolEnvironment, GameObservable.GameObserver, TimerObservable, GUIObservable {

    boolean gameWon = false;

    private int amountOfBulbs = 0;

    private int bulbsOn = 0;

    private UndoBackLog undoBackLog;

    private final TimerManager timerManager = new TimerManager();

    private List<GUIObserver> observers = new ArrayList<>();

    private ScheduledExecutorService solverExecutor = null;

    private boolean usedSolver = false;

    private int amountOfTurns = 0;

    /**
     * Map storing nodes in game world (2D space)
     */
    private GameNode[][] map;
    /**
     * Number of rows of game world
     */
    private int sizeRows;
    /**
     * Number of cols of game world
     */
    private int sizeCols;
    /**
     * Whether power node exists
     */
    private GameNodePower powerNode;

    /**
     * Basic constructor for Game
     */
    public Game() {}

    /**
     * Constructor for Game, intializes map and prepares instance
     * for further use. Nodes start at [1,1]
     * @param rows int length in X
     * @param cols int length in Y
     * @return Game instance of a Game
     * @throws IllegalArgumentException when wrong size of game world is specified
     */
    public static Game create(int rows, int cols) throws IllegalArgumentException {
        if (rows < 1 || cols < 1)
            throw new IllegalArgumentException();

        Game game = new Game();
        game.sizeRows = rows;
        game.sizeCols = cols;
        game.map = new GameNode[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GameNode node = new GameNode(new Position(i, j));
                game.map[i][j] = node;
                node.setupGameObserver(game);
            }
        }
        game.powerNode = null;
        game.undoBackLog = new UndoBackLog();

        return game;
    }

    public void startTimer(int timeLeft) {
        this.timerManager.startTimer(timeLeft);
    }

    public void stopTimer() {
        this.timerManager.stopTimer();
    }


    public static Game generate(int rows, int cols, float emptyCoef, int shuffleCoef) {
        Game game = Game.create(rows, cols);

        MapGenerator generator = new MapGenerator(rows, cols, emptyCoef);
        generator.generateMap();
        generator.randomizeMap(shuffleCoef);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GameNode node = game.map[i][j];
                MapElement elm = generator.getMapElement(node.getPosition());
                if (elm == null || !elm.hasConnections()) continue;

                int rotation = elm.getRotated();

                Side[] sides = elm.getSides();
                if (elm.isPower()) {
                    game.createPowerNode(node.getPosition(), sides).setRotation(rotation);
                    continue;
                }

                if (elm.isLight()) {
                    game.createBulbNode(node.getPosition(), sides[0]).setRotation(rotation);
                    continue;
                }
                game.createLinkNode(node.getPosition(), sides).setRotation(rotation);
            }
        }

        game.powerUp();
        return game;
    }

    public void setupTimerObserver(TimerObserver timerObserver) {
        this.timerManager.setupTimerObserver(timerObserver);
    }

    public void removeTimerObserver(TimerObserver observer) {
        this.timerManager.removeTimerObserver(observer);
    }

    public void notifyTimerObserver() {

    }

    public void setupGUIObserver(GUIObserver observer) {
        observers.add(observer);
    }

    public void removeGUIObserver(GUIObserver observer) {
        Iterator<GUIObserver> it = observers.iterator();
        while (it.hasNext()) {
            GUIObserver o = it.next();
            if (o == observer) {
                it.remove();
                break;
            }
        }
    }

    public void notifyGUIObserver() {
        for (GUIObserver observer : observers) {
            observer.updateGUI();
        }
    }

    private static JSONObject createJSON(String path) throws JSONException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            return new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Or throw exception if you want
        }
    }

    public static Game load(String path) throws JSONException {

        JSONObject data = createJSON(path);
        if (data == null) return null;

        // game size
        int rows = data.getInt("rows");
        int cols = data.getInt("cols");
        Game game = Game.create(rows, cols);

        // game attributes
        game.usedSolver = data.getBoolean("usedSolver");
        game.amountOfTurns = data.getInt("turnsTotal");
        int timeLeft = data.getInt("timeLeft");

        // backlog
        JSONObject backlog = data.getJSONObject("backlog");
        JSONArray buffer = backlog.getJSONArray("buffer");
        int currentIndex = backlog.getInt("currentIndex");

        // set up the moves buffer
        UndoBackLog undoBackLog = new UndoBackLog();
        for (int i = 0; i < buffer.length(); i++) {
            JSONObject pos = buffer.getJSONObject(i);
            int col = pos.getInt("col");
            int row = pos.getInt("row");
            Position p = new Position(row, col);
            undoBackLog.AddMove(p);
        }

        // set up the index
        undoBackLog.setIndex(currentIndex);
        game.undoBackLog = undoBackLog;

        // map
        JSONArray map = data.getJSONArray("map");
        for (int i = 0; i < map.length(); i++) {
            JSONObject gameNode = map.getJSONObject(i);

            // attributes
            int rotation = gameNode.getInt("rotation");
            int totalRotations = gameNode.getInt("totalRotations");
            String type = gameNode.getString("type");
            JSONArray sides = gameNode.getJSONArray("sides");
            ArrayList<Side> sidesList = new ArrayList<>();

            // sides
            for (int j = 0; j < sides.length(); j++) {
                JSONObject side = sides.getJSONObject(j);
                switch (side.getString("side")) {
                    case "WEST":
                        sidesList.add(Side.WEST);
                        break;
                    case "EAST":
                        sidesList.add(Side.EAST);
                        break;
                    case "SOUTH":
                        sidesList.add(Side.SOUTH);
                        break;
                    case "NORTH":
                        sidesList.add(Side.NORTH);
                        break;
                    default:
                        break;
                }
            }

            // creating each game node
            int rw = i/rows;
            int cl = i%cols;
            Position p = new Position(rw, cl);
            GameNode node;
            switch (type) {
                case "None":
                    continue;
                case "bulb":
                    node = game.createBulbNode(p, sidesList.getFirst());
                    break;
                case "link":
                    node = game.createLinkNode(p, sidesList.toArray(new Side[0]));
                    break;
                case "power":
                    node = game.createPowerNode(p, sidesList.toArray(new Side[0]));
                    break;
                default:
                    throw new RuntimeException("Unsupported type: " + type);
            }
            node.setRotation(rotation);
            node.setTotalRotations(totalRotations);
        }
        game.powerUp();
        game.startTimer(timeLeft);
        return game;
    }

    public void save(String path) throws IOException {

        JSONObject obj = new JSONObject();
        obj.put("rows", this.rows());
        obj.put("cols", this.cols());
        obj.put("timeLeft", this.timeLeft());
        obj.put("usedSolver", this.usedSolver());
        obj.put("turnsTotal", this.amountOfTurns);
        obj.put("backlog", this.undoBackLog.toJson());
        JSONArray obj2 = new JSONArray();
        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < cols(); j++) {
                GameNode node = map[i][j];
                obj2.put(node.toJSON());
            }
        }
        obj.put("map", obj2);

        FileWriter file = new FileWriter(path);
        file.write(obj.toString(2)); // 2 = pretty print with 2-space indent
        file.flush();
    }

    public void undo() {
        if (!canUndo()) return;
        Position pos = undoBackLog.Undo();
        // this needs left turn
        this.map[pos.getRow()][pos.getCol()].internalTurn(true);
        this.powerUp();
    }

    public void redo() {
        if (!canRedo()) return;
        Position pos = undoBackLog.Redo();
        this.map[pos.getRow()][pos.getCol()].internalTurn(false);
        this.powerUp();
    }

    public boolean canUndo() {
        return undoBackLog.canUndo();
    }

    public boolean canRedo() {
        return undoBackLog.canRedo();
    }

    public boolean hasWon() {
        return gameWon;
    }

    public int getBulbsOn() {
        return bulbsOn;
    }

    public int getBulbsTotal() {
        return amountOfBulbs;
    }

    public int getUsedTurnsTotal() {
        return amountOfTurns;
    }

    public boolean usedSolver() {
        return this.usedSolver;
    }


    public void solve(int timeout) {
        if (solverExecutor != null) return;

        ArrayList<GameNode> nodes = new ArrayList<>();

        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < cols(); j++) {
                GameNode node = map[i][j];
                if (node.remainingTurns() > 0) {
                    nodes.add(node);
                }
            }
        }

        solverExecutor = Executors.newSingleThreadScheduledExecutor();
        solverExecutor.scheduleAtFixedRate(() -> {
            GameNode node = nodes.getFirst();
            node.turn();
            if (node.remainingTurns() == 0) {
                nodes.remove(node);
            }
            usedSolver = true;
        }, 0, timeout, TimeUnit.MILLISECONDS);
    }

    // Ensure you shut down the executor when done (e.g., on app exit)
    public void stopSolver() {
        if (solverExecutor != null) {
            solverExecutor.shutdownNow();
            solverExecutor = null;
        }
    }

    /**
     * Creates single Link node at given position
     * @param position Position where to create the node
     * @param sides Side[] sides with connectors
     * @return GameNode instance
     */
    public GameNode createLinkNode(Position position, Side... sides) {
        if (this.isPositionInvalid(position)) return null;
        if (this.areSidesInvalid(sides)) return null;
        if (sides.length < 2) return null;

        GameNode node = new GameNodeLink(position, sides);
        node.setupGameObserver(this);

        this.map[position.getRow()][position.getCol()] = node;

        return node;
    }

    /**
     * Creates single Power source node at given position
     * @param position Position where to create the node
     * @param sides Side[] sides with connectors
     * @return GameNode instance
     */
    public GameNode createPowerNode(Position position, Side... sides) {
        if (this.isPositionInvalid(position)) return null;
        if (this.areSidesInvalid(sides)) return null;
        if (this.powerNode != null) return null;

        GameNodePower node = new GameNodePower(position, sides);
        node.setupGameObserver(this);

        this.map[position.getRow()][position.getCol()] = node;
        this.powerNode = node;

        return node;
    }

    /**
     * Creates single Bulb node at given position
     * @param position Position where to create the node
     * @param side Side with connector
     * @return GameNode instance
     */
    public GameNode createBulbNode(Position position, Side side) {
        if (this.isPositionInvalid(position)) return null;
        if (this.areSidesInvalid(side)) return null;

        GameNode node = new GameNodeBulb(position, side);
        node.setupGameObserver(this);

        this.map[position.getRow()][position.getCol()] = node;
        amountOfBulbs++;

        return node;
    }

    /**
     * @return int number of rows of game world
     */
    public int rows() {
        return this.sizeRows;
    }

    /**
     * @return int number of cols of game world
     */
    public int cols() {
        return this.sizeCols;
    }

    public int timeLeft() {
        return this.timerManager.getTimeLeft();
    }

    /**
     * Obtains GameNode at given position
     * @param position Position of the node
     * @return GameNode
     */
    public GameNode node(Position position) {
        if (this.isPositionInvalid(position))
            return null;

        return this.map[position.getRow()][position.getCol()];
    }

    /**
     * Return node (ToolField) instance at given row and col
     * @param row
     * @param col
     * @return ToolField node
     */
    public ToolField fieldAt(int row, int col) {
        return this.map[row][col];
    }

    /**
     * Powers on all nodes connected to source
     */
    public void init() {
        this.powerUp();
    }

    /**
     * Entry point in Game observer when connection check is needed
     * @param observable that issued the notification
     */
    public void update(GameObservable observable) {
        undoBackLog.AddMove(observable.getPosition());
        amountOfTurns += 1;
        this.powerUp();
    }

    /**
     * Performs connection recalculation (lights up everything connected to source)
     */
    private void powerUp() {

        int bulbsOn = 0;
        this.turnAllOff();
        GameNode node = this.powerNode;
        if (node == null)
            return;

        Stack<GameNode> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            node = stack.pop();

            ArrayList<GameNode> neighbors = this.getNeighbours(node);

            for (GameNode neighbor : neighbors) {
                stack.push(neighbor);
            }
            node.turnOn();
            if (node.isBulb()) bulbsOn++;
        }
        if (bulbsOn == amountOfBulbs) gameWon = true;
        this.bulbsOn = bulbsOn;
        Platform.runLater(this::notifyGUIObserver);
    }

    /**
     * Turns all nodes off
     */
    private void turnAllOff() {
        for (int i = 0; i < this.rows(); i++) { // rows
            for (int j = 0; j < this.cols(); j++) { // cols
                this.map[i][j].turnOff();
            }
        }
    }

    /**
     * Helper function for validating given Position instance
     * @param position Position
     * @return true if invalid, otherwise false
     */
    private boolean isPositionInvalid(Position position) {
        if (position == null)
            return true;

        if (position.getRow() < 0 || position.getRow() >= this.sizeRows)
            return true;

        return position.getCol() < 0 || position.getCol() >= this.sizeCols;
    }

    /**
     * Helper function for validating given array of sides
     * @param sides Side[]
     * @return true if invalid, otherwise false
     */
    private boolean areSidesInvalid(Side... sides) {

        if (sides == null || sides.length == 0 || sides.length > 4)
            return true;

        ArrayList<Side> usedSides = new ArrayList<>();
        for (Side side : sides) {
            if (usedSides.contains(side))
                return true;
            usedSides.add(side);
        }

        return false;
    }

    /**
     * Method to check, if two nodes are facing each other
     * @param side Side
     * @param secondary GameNode to check
     * @return true if invalid, otherwise false
     */
    private boolean faceEachOther(Side side, GameNode secondary) {
        if (secondary == null) return false;
        return switch (side) {
            case NORTH -> secondary.south();
            case SOUTH -> secondary.north();
            case WEST -> secondary.east();
            case EAST -> secondary.west();
            default -> false;
        };
    }

    /**
     * Method that returns the valid Neighbours of a node
     * @param gameNode GameNode
     * @return a list of all valid Neighbours
     */
    private ArrayList<GameNode> getNeighbours(GameNode gameNode) {

        ArrayList<GameNode> neighbours;
        neighbours = new ArrayList<>();

        for (Side side : gameNode.getConnectedSides()) {
            GameNode node = this.node(gameNode.getPosition().getNeighbourPos(side));
            if (this.faceEachOther(side, node) && !node.light())
                neighbours.add(node);
        }

        return neighbours;
    }
}
