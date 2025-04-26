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
import com.example.game.shared.GameObservable;
import com.example.game.shared.ToolEnvironment;
import com.example.game.shared.ToolField;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Represents LightBulb game
 * implements ToolEnvironment - for GUI visualisation
 * implements GameObserver - for notifications informing about the need of recalculate connections
 */
public class Game implements ToolEnvironment, GameObservable.GameObserver {
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
                GameNode node = new GameNode(new Position(i + 1, j + 1));
                game.map[i][j] = node;
                node.setupGameObserver(game);
            }
        }
        game.powerNode = null;

        return game;
    }

    /**
     * Creates single Link node at given position
     * @param position Position where to create the node
     * @param sides Side[] sides with connectors
     * @return GameNode instance
     */
    public GameNode createLinkNode(Position position, Side... sides) {
        if (this.isPositionInvalid(position))
            return null;

        if (this.areSidesInvalid(sides))
            return null;

        if (this.areSidesAtPositionInvalid(position, sides))
            return null;

        if (sides.length < 2)
            return null;

        GameNode node = new GameNodeLink(position, sides);
        node.setupGameObserver(this);

        this.map[position.getRow()-1][position.getCol()-1] = node;

        return node;
    }

    /**
     * Creates single Power source node at given position
     * @param position Position where to create the node
     * @param sides Side[] sides with connectors
     * @return GameNode instance
     */
    public GameNode createPowerNode(Position position, Side... sides) {
        if (this.isPositionInvalid(position))
            return null;

        if (this.areSidesInvalid(sides))
            return null;

        if (this.areSidesAtPositionInvalid(position, sides))
            return null;

        if (this.powerNode != null)
            return null;

        GameNodePower node = new GameNodePower(position, sides);
        node.setupGameObserver(this);

        this.map[position.getRow()-1][position.getCol()-1] = node;

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
        if (this.isPositionInvalid(position))
            return null;

        if (this.areSidesInvalid(side))
            return null;

        if (this.areSidesAtPositionInvalid(position, side))
            return null;

        GameNode node = new GameNodeBulb(position, side);
        node.setupGameObserver(this);

        this.map[position.getRow()-1][position.getCol()-1] = node;

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

    /**
     * Obtains GameNode at given position
     * @param position Position of the node
     * @return GameNode
     */
    public GameNode node(Position position) {
        if (this.isPositionInvalid(position))
            return null;

        return this.map[position.getRow()-1][position.getCol()-1];
    }

    /**
     * Return node (ToolField) instance at given row and col
     * @param row
     * @param col
     * @return ToolField node
     */
    public ToolField fieldAt(int row, int col) {
        return this.map[row-1][col-1];
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
        this.powerUp();
    }

    /**
     * Performs connection recalculation (lights up everything connected to source)
     */
    private void powerUp() {
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

        }
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

        if (position.getRow() < 1 || position.getRow() > this.sizeRows)
            return true;

        if (position.getCol() < 1 || position.getCol() > this.sizeCols)
            return true;

        return false;
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
     * Checks that we can have a node connecting to given side at given position
     * @param position Position
     * @param sides Side[] to check
     * @return true if invalid, otherwise false
     */
    private boolean areSidesAtPositionInvalid(Position position, Side... sides) {
        if (position.getRow() == 1) {
            for (Side side : sides) {
                if (side == Side.NORTH)
                    return true;
            }
        }

        if (position.getRow() == this.sizeRows) {
            for (Side side : sides) {
                if (side == Side.SOUTH)
                    return true;
            }
        }

        if (position.getCol() == 1) {
            for (Side side : sides) {
                if (side == Side.WEST)
                    return true;
            }
        }

        if (position.getCol() == this.sizeCols) {
            for (Side side : sides) {
                if (side == Side.EAST)
                    return true;
            }
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
