/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: MapElement.java
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class MapElement represents an element of a 2D map in the game.
 *              It manages its position, connections to other elements, rotation,
 *              and its state as a root or light source. Used for generating the gameModel
 */

package com.example.game.model.common;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for creating a map for the game.
 * It generates a 2D map with elements that can be connected to each other.
 * The map is generated using a modified Prim's algorithm that ensures that all elements are connected.
*/
public class MapGenerator {

    /**
     * Percentage of empty spaces in the map.
     */
    final private int rows;

    /**
     * Number of rows in the map.
     */
    final private int cols;

    /**
     * 2D array representing the map.
     */
    final private MapElement[][] map;

    /**
     * Priority queue for managing the elements in the map.
     */
    final private PriorityQueue queue;

    /**
     * Random number generator for generating random positions in the map.
     */
    final private Random random;

    /**
     * Logger for logging information.
     */
    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Constructor for the MapGenerator class.
     * @param rows Number of rows in the map.
     * @param cols Number of columns in the map.
     * @param emptyPercentage Percentage of empty spaces in the map.
     */
    public MapGenerator(int rows, int cols, float emptyPercentage) {
        this.rows = rows;
        this.cols = cols;
        map = new MapElement[rows][cols];
        random = new Random();

        // create the root
        int rx = random.nextInt(rows);
        int ry = random.nextInt(cols);
        MapElement root =  new MapElement(new Position(ry, rx));
        root.setRoot();
        map[ry][rx] = root;

        logger.log(Level.INFO, "Generating Map");
        logger.log(Level.INFO, "root element: " + root.getPos());

        // create the root neighbours
        for (Side side : Side.values()) {
            Position newPos = root.getPos().getNeighbourPos(side);
            if (isOutOfBounds(newPos)) continue;
            map[newPos.getRow()][newPos.getCol()] = new MapElement(new Position(newPos.getRow(), newPos.getCol()));
        }

        // fill in blank elements
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (map[y][x] != null) continue;
                if (Math.random() < emptyPercentage) {
                    map[y][x] = null;
                } else {
                    map[y][x] = new MapElement(new Position(y, x));
                }
            }
        }

        // create the queue
        queue = new PriorityQueue(random);

        // add the initial moves for the root element
        AddAvailableMoves(root);
        removeInvalidMoves();
    }

    /**
     * Removes invalid moves from the queue.
     * A move is invalid if it leads to an out of bounds position or if the target element has connections.
     */
    private void removeInvalidMoves() {

        int idx = 0;
        QueueElement elm = queue.getQueueElement(idx);
        while (elm != null) {
            MapElement node = elm.getNode();
            Position newPos = node.getPos().getNeighbourPos(elm.getDirection());
            MapElement other = getMapElement(newPos);
            if (other == null || other.hasConnections()) {
                queue.removeIdx(idx);
                idx--; // in case we removed one element, we need to move back one slot, for the logic to work
            }
            elm = queue.getQueueElement(++idx);
        }
    }

    /**
     * Checks if the given position is out of bounds of the map.
     * @param pos The position to check.
     * @return true if the position is out of bounds, false otherwise.
     */
    private boolean isOutOfBounds(Position pos) {
        int x = pos.getCol();
        int y = pos.getRow();
        // out of bounds check
        return x < 0 || x >= cols || y < 0 || y >= rows;
    }

    /**
     * Gets the MapElement at the given position.
     * @param pos The position of the element.
     * @return The MapElement at the given position, or null if it is out of bounds.
     */
    public MapElement getMapElement(Position pos) {

        int x = pos.getCol();
        int y = pos.getRow();
        // out of bounds check
        if (isOutOfBounds(pos)) return null;
        return map[y][x];
    }

    /**
     * Gets the neighbor of the given QueueElement.
     * @param elm The QueueElement to get the neighbor of.
     * @return The MapElement at the neighbor position, or null if it is out of bounds.
     */
    private MapElement getNeighbor(QueueElement elm) {

        if (elm == null) return null;

        MapElement node = elm.getNode();
        Position pos = node.getPos();

        Position neighbourPos = pos.getNeighbourPos(elm.getDirection());
        return getMapElement(neighbourPos);
    }

    /**
     * Adds available moves for the given MapElement to the queue.
     * @param targetElement The MapElement to add available moves for.
    */
    private void AddAvailableMoves(MapElement targetElement) {

        Position targetPos = targetElement.getPos();
        if (targetPos == null) throw new Error("std::POMOC!!!");

        // add the available moves
        for (Side side : Side.values()) {
            Position newPos = targetPos.getNeighbourPos(side);
            MapElement elm = getMapElement(newPos);
            if (elm == null) continue; // out of bounds
            if (elm.hasConnections()) continue; // it is allReady connected, leave it out
            queue.Add(targetElement, side);
        }
    }

    /**
     * Generates the map using a modified Prim's algorithm.
     * It connects all elements in the map and ensures that all elements are reachable from the root element.
     */
    public void generateMap() {

        int overflowProtection = rows * cols * 10;
        int steps = 0;

        while (true) {
            if (steps >= overflowProtection) { throw new Error("overflow protection in map generation"); }

            // remove unavailable moves
            removeInvalidMoves();

            // find the min cost
            QueueElement elm = queue.findMinCost();

            // no more moves, finished
            if (elm == null) {
                return;
            }

            // execute the move
            MapElement neighbour = getNeighbor(elm);
            logger.log(Level.INFO, "generating connection to neighbour: " + neighbour.getPos());

            queue.executeMove(elm, neighbour);
            AddAvailableMoves(neighbour);
            steps++;
        }
    }

    /**
     * Randomizes the map by rotating a given number of elements.
     * @param amountOfRotations The number of elements to rotate.
     */
    public void randomizeMap(int amountOfRotations) {

        int rotationsDone = 0;
        while (rotationsDone < amountOfRotations) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            MapElement elm = map[x][y];
            if (elm == null) continue;

            elm.rotate();

            rotationsDone++;
        }
    }

    /**
     * Prints the map to the terminal.
     * It shows the connections between elements and their positions.
     */
    public void printMapTerminal() {

        StringBuilder bfr0 = new StringBuilder();
        for (int x = 0; x < cols; x++) {
            bfr0.append(String.format("%4d  ", x));
        }
        System.out.println(bfr0); // Print column indexes

        for (int y = 0; y < rows; y++) {
            StringBuilder bfr1 = new StringBuilder("  ");
            StringBuilder bfr2 = new StringBuilder(String.format("%-2d", y)); // Add row index for bfr2
            StringBuilder bfr3 = new StringBuilder("  ");

            for (int x = 0; x < cols; x++) {
                if (map[y][x] == null) {
                    bfr1.append("      ");
                    bfr2.append("      ");
                    bfr3.append("      ");
                } else {

                String[] result = map[y][x].getRepresentation(bfr1.toString(), bfr2.toString(), bfr3.toString());
                bfr1 = new StringBuilder(result[0]);
                bfr2 = new StringBuilder(result[1]);
                bfr3 = new StringBuilder(result[2]);
                }
            }

            System.out.println(bfr1);
            System.out.println(bfr2);
            System.out.println(bfr3);
        }
    }
}
