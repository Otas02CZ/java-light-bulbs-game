/**
 * IJA HW: 2
 * FILE: GameNodePower.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class GameNodePower an element of 2D Game
 */

package ija.ija2024.homework2.common;

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

    @Override
    public boolean isPower() {
        return true;
    }

    @Override
    public String toString() {
        return getToString("P");
    }
}
