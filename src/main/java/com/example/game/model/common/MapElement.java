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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing an element of a 2D map in the game.
 * It manages its position, connections to other elements, rotation,
 * and its state as a root or connector.
 */
public class MapElement {

    /**
     * Position of the element in the 2D map.
     */
    final private Position pos;

    /**
     * Logger for logging information.
     */
    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Number of times the element has been rotated.
     */
    private int rotated = 0;

    /**
     * Indicates if the element is a root element.
     */
    private boolean isRoot = false;

    /**
     * Represents the stat of the northem connection of the element.
     */
    private boolean north = false;

    /**
     * Represents the stat of the southern connection of the element.
     */
    private boolean south = false;

    /**
     * Represents the stat of the western connection of the element.
     */
    private boolean west = false;

    /**
     * Represents the stat of the eastern connection of the element.
     */
    private boolean east = false;

    /**
     * Constructor for MapElement.
     *
     * @param pos Position of the element in the 2D map.
     */
    public MapElement(Position pos) {
        this.pos = pos;
    }

    /**
     * Sets the connection to the north.
     */
    public void setNorth() {north = true;};

    /**
     * Sets the connection to the south.
     */
    public void setSouth() {south = true;};

    /**
     * Sets the connection to the west.
     */
    public void setWest() {west = true;};

    /**
     * Sets the connection to the east.
     */
    public void setEast() {east = true;};

    /**
     * Gets the position of the element.
     * @return position of the element
    */
    public Position getPos() {return pos;}

    /**
     * Checks if the element has any connections.
     * @return true if the element has connections, false otherwise.
     */
    public boolean hasConnections() {return north || south || west || east;}

    /**
     * Checks if the element is connected on a specific side.
     * @param side The side to check.
     * @return true if the element is connected on that side, false otherwise.
     */
    public boolean isConnectedOnSide(Side side) {
        return switch (side) {
            case NORTH -> north;
            case SOUTH -> south;
            case WEST -> west;
            case EAST -> east;
        };
    }

    /**
     * Checks if the element is a root element.
     */
    public void setRoot() {isRoot = true;};

    /**
     * Rotates the element clockwise.
    */
    public void rotate() {

        logger.log(Level.INFO, "Rotating " + this.getPos());

        // north <-> East
        boolean tmp = north;
        north = east;
        east = tmp;

        //  north <-> south
        tmp = south;
        south = north;
        north = tmp;

        // north <- West
        tmp = west;
        west = north;
        north = tmp;

        rotated++;
    }

    /**
     * Gets the number of times the element has been rotated.
     * @return The number of rotations.
     */
    public int getRotated() {return rotated;}

    /**
     * Gets all the connections of the element.
     * @return An array of sides representing the connections.
     */
    public Side[] getSides() {
        List<Side> sidesList = new ArrayList<>();

        if (north) sidesList.add(Side.NORTH);
        if (south) sidesList.add(Side.SOUTH);
        if (west)  sidesList.add(Side.WEST);
        if (east)  sidesList.add(Side.EAST);

        return sidesList.toArray(new Side[0]);
    }

    /**
     * Checks if the element is the root element.
     * @return true if it is, false otherwise
    */
    public boolean isPower() {
        return isRoot;
    }

    /**
     * Checks if the element is a light source (only has one connection).
     * @return true if the element is a light source, false otherwise.
     */
    public boolean isLight() {
        int count = 0;
        count += north ? 1 : 0;
        count += south ? 1 : 0;
        count += west ? 1 : 0;
        count += east ? 1 : 0;
        return count == 1;
    }

    /**
     * Gets the string representation of the element.
     * @param bfr1 StringBuilder for the first line.    
     * @param bfr2 StringBuilder for the second line.
     * @param bfr3 StringBuilder for the third line.
     * @return An array of strings representing the element.
     */
    public String[] getRepresentation(String bfr1, String bfr2, String bfr3) {
        String signal = "XX";
        String space = "  ";
        String middle = (hasConnections()) ? signal : space;
        bfr1 += space + ((isConnectedOnSide(Side.NORTH)) ? signal : space) + space;
        bfr2 += ((isConnectedOnSide(Side.WEST)) ? signal : space) + middle + ((isConnectedOnSide(Side.EAST)) ? signal : space);
        bfr3 += space + ((isConnectedOnSide(Side.SOUTH)) ? signal : space) + space;
        return new String[]{bfr1, bfr2, bfr3};
    }
}
