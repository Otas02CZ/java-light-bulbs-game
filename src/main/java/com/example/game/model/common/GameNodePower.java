/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: GameNodePower.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class GameNodePower an element of 2D Game
 */

package com.example.game.model.common;

import org.json.JSONObject;

/**
 * Extended GameNode representing a Power source
 */
public class GameNodePower extends GameNode {
    /**
     * Constructor for a Power source
     * @param position Position in 2D space
     * @param sides Side[] sides with connectors
     */
    public GameNodePower(Position position, Side... sides) {
        super(position);
        for (Side side : sides) {
            this.connectors.put(side, true);
        }
    }

    /**
     * helper method to identify the type of GameNode
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * helper method to identify the type of GameNode
     */
    @Override
    public boolean isPower() {
        return true;
    }

    /**
     * Overrides toString to print in format:
     * {P[ROW@COL][SIDE_1,SIDE_2,...]}
     * @return String representation
     */
    @Override
    public String toString() {
        return getToString("P");
    }

    /**
     * Method to get the string representation of the GameNode
     * @param bfr1 StringBuilder for the first line
     * @param bfr2 StringBuilder for the second line
     * @param bfr3 StringBuilder for the third line
     */
    @Override
    public void represent(StringBuilder bfr1, StringBuilder bfr2, StringBuilder bfr3) {
        String space = "\uD83E\uDDAF";
        String wire = powered ?  "\uD83D\uDFE9" :  "\uD83D\uDFE5";
        String middle = "\uD83E\uDEAB";

        bfr1.append(space).append(containsConnector(Side.NORTH) ? wire : space).append(space);
        bfr2.append(containsConnector(Side.WEST) ? wire : space).append(middle).append(containsConnector(Side.EAST) ? wire : space);
        bfr3.append(space).append(containsConnector(Side.SOUTH) ? wire : space).append(space);
    }

    /**
     * Method to get the JSON representation of the GameNode
     * @return JSON representation
     */
    @Override
    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("type", "power");
        return obj;
    }

}
