/**
 * IJA HW: 2
 * FILE: GameNodeLink.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class GameNodeLink an element of 2D Game
 */

package ija.ija2024.homework2.common;

/**
 * Extended GameNode representing a Link
 */
public class GameNodeLink extends GameNode {
    /**
     * Constructor for a Link
     * @param position Position in 2D space
     * @param sides Side[] storing sides with connectors
     */
    public GameNodeLink(Position position, Side... sides) {
        super(position);
        for (Side side : sides) {
            this.connectors.put(side, true);
        }
    }

    @Override
    public boolean isLink() {
        return true;
    }

    @Override
    public String toString() {
        return getToString("L");
    }
}
