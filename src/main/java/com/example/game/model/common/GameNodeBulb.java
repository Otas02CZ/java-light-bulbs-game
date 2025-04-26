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
}
