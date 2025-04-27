/**
 * IJA HW: 2
 * FILE: GameNode.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class GameNode an element of 2D Game
 */

package com.example.game.model.common;

import com.example.game.shared.ToolField;
import com.example.game.shared.GameObservable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


/**
 * GameNode element used in 2D Game space
 * This class provides only basic and shared functionality
 * It is expanded in Bulb, Link and Power nodes
 */
public class GameNode implements ToolField, GameObservable {

    /**
     * CurrentRotation
     */
    private int rotation;

    /**
     * Countr for rotations
     */
    private int totalRotations = 0;

    /**
     * Position in 2D space
     */
    protected Position position;

    /**
     * Is this node currently powered by source
     */
    protected boolean powered;

    /**
     * Stores reference to gameObserver, used to notify Game when
     * connection recheck is required
     */
    protected GameObserver gameObserver;

    /**
     * Stores which sides are connectors to other nodes
     */
    protected HashMap<Side, Boolean> connectors;

    /**
     * Basic constructor, Position is set to invalid [0,0]
     */
    public GameNode() {
        connectors = new HashMap<>();
        connectors.put(Side.NORTH, false);
        connectors.put(Side.WEST, false);
        connectors.put(Side.SOUTH, false);
        connectors.put(Side.EAST, false);
        this.position = new Position(0, 0);
        this.powered = false;
        this.gameObserver = null;
    }

    /**
     * Basic constructor, position is set to given position
     * @param position Position
     */
    public GameNode(Position position) {
        connectors = new HashMap<>();
        connectors.put(Side.NORTH, false);
        connectors.put(Side.WEST, false);
        connectors.put(Side.SOUTH, false);
        connectors.put(Side.EAST, false);
        this.position = position;
        this.powered = false;
        this.gameObserver = null;
    }

    /**
     * @return Position of node in 2D space
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Turns (rotates) node 90 deg clockwise
     */
    public void turn() {
        HashMap<Side, Boolean> tempConnectors = new HashMap<>();

        if (connectors.get(Side.NORTH))
            tempConnectors.put(Side.EAST, true);
        else
            tempConnectors.put(Side.EAST, false);

        if (connectors.get(Side.EAST))
            tempConnectors.put(Side.SOUTH, true);
        else
            tempConnectors.put(Side.SOUTH, false);

        if (connectors.get(Side.SOUTH))
            tempConnectors.put(Side.WEST, true);
        else
            tempConnectors.put(Side.WEST, false);

        if (connectors.get(Side.WEST))
            tempConnectors.put(Side.NORTH, true);
        else
            tempConnectors.put(Side.NORTH, false);

        connectors = tempConnectors;

        totalRotations++;
        rotation = (rotation + 1) % 4;
        this.notifyGameObserver();
    }

    /**
     * @return boolean is node a link ?
     */
    public boolean isLink() {
        return false;
    }

    /**
     * @return boolean is node a bulb ?
     */
    public boolean isBulb() {
        return false;
    }

    /**
     * @return boolean is node a power source ?
     */
    public boolean isPower() {
        return false;
    }

    /**
     * Returns boolean whether given side has connector
     * @param side Side
     * @return boolean
     */
    public boolean containsConnector(Side side) {
        return connectors.get(side);
    }

    /**
     * Get info about node's powered state
     * @return boolean whether this instance is currently turned on/off
     */
    public boolean light() {
        return powered;
    }

    /**
     * Turns node off
     */
    public void turnOff() {
        if (!powered)
            return;
        powered = false;
    }

    /**
     * Turns node on
     */
    public void turnOn() {
        if (powered)
            return;
        powered = true;
    }

    /**
     * Overrides toString to print in format:
     * {TYPE[ROW@COL][SIDE_1,SIDE_2,...]}
     * @return String string representation
     */
    @Override
    public String toString() {
        return getToString("E");
    }

    /**
     * Whether this instance has connector to EAST
     * @return boolean whether EAST connector is present
     */
    public boolean east() {
        return connectors.get(Side.EAST);
    }

    /**
     * Whether this instance has connector to WEST
     * @return boolean whether WEST connector is present
     */
    public boolean west() {
        return connectors.get(Side.WEST);
    }

    /**
     * Whether this instance has connector to NORTH
     * @return boolean whether NORTH connector is present
     */
    public boolean north() {
        return connectors.get(Side.NORTH);
    }

    /**
     * Whether this instance has connector to SOUTH
     * @return boolean whether SOUTH connector is present
     */
    public boolean south() {
        return connectors.get(Side.SOUTH);
    }

    /**
     * Gets list of active connectors of this node
     * @return list of connectors as ArrayList of Sides
     */
    public ArrayList<Side> getConnectedSides() {
        ArrayList<Side> connectedSides;
        connectedSides = new ArrayList<>();
        for (Side side : Side.values()) {
            if (connectors.get(side)) {
                connectedSides.add(side);
            }
        }
        return connectedSides;
    }

    /**
     * Setup given observer as Game observer to notify about
     * changes that need connection recheck
     * @param observer to use for notifying about Game events
     */
    public void setupGameObserver(GameObserver observer) {
        this.gameObserver = observer;
    }

    /**
     * Removes set Game observer
     */
    public void removeGameObserver() {
        this.gameObserver = null;
    }

    /**
     * Notify Game observer when connection recalculation is required
     */
    public void notifyGameObserver() {
        if (gameObserver == null)
            return;
        this.gameObserver.update(this);
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int usedTurns() {
        return totalRotations;
    }

    public int remainingTurns() {
        return -rotation + 4;
    }

    /**
     * Helper function for simplification of toString override
     */
    protected String getToString(String name) {
        // required order of sides
        List<Side> orderedSides = Arrays.asList(Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST);

        // get active connectors
        List<String> activeConnectors = orderedSides.stream()
                .filter(side -> connectors.getOrDefault(side, false))
                .map(Enum::name)
                .toList();

        // format return message
        return String.format("{%s[%d@%d][%s]}",
                name,
                position.getRow(),
                position.getCol(),
                String.join(",", activeConnectors));
    }

    public void represent(StringBuilder bfr1, StringBuilder bfr2, StringBuilder bfr3) {
        String space = "\uD83E\uDDAF";
        bfr1.append(space).append(space).append(space);
        bfr2.append(space).append(space).append(space);
        bfr3.append(space).append(space).append(space);
    }
}
