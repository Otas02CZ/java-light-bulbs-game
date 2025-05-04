/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: ToolField.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Interface through which GUI access Game nodes in the model.
 * NOTE: This interface was inspired by the provided interface in HW2
 */

package com.example.game.shared;

/**
 * Represents a field or node in the game that can be interacted with.
 * This interface provides access to the Game nodes for the GUI elements.
 * It consists of functions for turning the node qnd querying
 * about the node's state.
 */
public interface ToolField {
    /**
     * Rotates the current game node 90 deg. This method modifies the state of the node by
     * performing a rotation operation, which may affect the node's connections
     * and orientation in the game.
     */
    void turn();

    /**
     * Checks whether the current game node has a connection to the north
     *
     * @return true if the game node has a connection to the north, false otherwise
     */
    boolean north();

    /**
     * Checks whether the current game node has a connection to the east.
     *
     * @return true if the game node has a connection to the east, false otherwise.
     */
    boolean east();

    /**
     * Checks whether the current game node has a connection to the south.
     *
     * @return true if the game node has a connection to the south, false otherwise.
     */
    boolean south();

    /**
     * Checks whether the current game node has a connection to the west.
     *
     * @return true if the game node has a connection to the west, false otherwise.
     */
    boolean west();

    /**
     * Checks whether the current game node is illuminated by a light source.
     *
     * @return true if the game node is lit by a light source, false otherwise.
     */
    boolean light();

    /**
     * Checks whether the current game node is empty.
     *
     * @return true if the game node is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Determines if the current game node is a link node.
     *
     * @return true if the game node is a link node, false otherwise.
     */
    boolean isLink();

    /**
     * Determines if the current game node is a bulb.
     *
     * @return true if the game node is a bulb, false otherwise.
     */
    boolean isBulb();

    /**
     * Determines if the current game node is powered.
     *
     * @return true if the game node has power, false otherwise.
     */
    boolean isPower();

    /**
     * Retrieves the number of turns remaining for the current game node.
     * This value represents the number of rotations needed to achieve the
     * correct node orientation.
     *
     * @return the number of remaining turns for the current game node
     */
    int remainingTurns();

    /**
     * Retrieves the number of turns that have been used for the current game node.
     * This value represents the number of rotations that have been applied to the node
     * since its initial orientation was set.
     *
     * @return the number of used turns for the current game node
     */
    int usedTurns();
    
}