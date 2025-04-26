/**
 * IJA HW: 2
 * FILE: GameNodeBulb.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class GameNodeBulb extending GameNode
 */

package com.example.game.model.common;

/**
 * Extended GameNode representing a Bulb
 */
public class GameNodeBulb extends GameNode {
    /**
     * Constructor for a Bulb
     * @param position Position in 2D space
     * @param side Side with connector
     */
    public GameNodeBulb(Position position, Side side) {
        super(position);
        this.connectors.put(side, true);
    }

    @Override
    public boolean isBulb() {
        return true;
    }

    @Override
    public String toString() {
        return getToString("B");

    }

    @Override
    public void represent(StringBuilder bfr1, StringBuilder bfr2, StringBuilder bfr3) {
        String space = "\uD83E\uDDAF";
        String wire = powered ?  "\uD83D\uDFE9" :  "\uD83D\uDFE5";
        String middle = powered ? "\uD83C\uDF15" : "\uD83C\uDF11";

        bfr1.append(space).append(containsConnector(Side.NORTH) ? wire : space).append(space);
        bfr2.append(containsConnector(Side.WEST) ? wire : space).append(middle).append(containsConnector(Side.EAST) ? wire : space);
        bfr3.append(space).append(containsConnector(Side.SOUTH) ? wire : space).append(space);
    }
}
