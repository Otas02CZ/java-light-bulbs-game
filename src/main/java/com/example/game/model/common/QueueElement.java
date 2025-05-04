/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: QueueElement.java
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Represents an element in the priority queue with associated cost,
 * node, direction, and index for managing game elements.
 */

package com.example.game.model.common;
import java.util.Random;

/**
 * Class representing an element in the priority queue.
 * It contains the cost, node, direction, and index of the element.
 */
public class QueueElement {
    /**
     * Cost associated with the element.
     */
    private final int cost;

    /**
     * The MapElement associated with this QueueElement.
     */
    private final MapElement node;

    /**
     * The direction associated with this QueueElement.
     */
    private final Side direction;

    /**
     * Index of the element in the queue.
     */
    private int idx;

    /**
     * Constructor for the QueueElement class.
     * @param node The MapElement associated with this QueueElement.
     * @param direction The direction associated with this QueueElement.
     * @param random Random number generator for generating a random cost.
     * @param idx Index of the element in the queue.
     */
    public QueueElement(MapElement node, Side direction, Random random, int idx) {
        this.node = node;
        this.direction = direction;
        this.cost = random.nextInt(201); // Generates number in [min, max]
        this.idx = idx;
    }

    /**
     * Returns the cost associated with this QueueElement.
     * @return The cost of the element.
     */
    public int getCost() {return cost;};

    /**
     * Returns the MapElement associated with this QueueElement.
     * @return The MapElement of this QueueElement.
     */
    public MapElement getNode() {return node;}

    /**
     * Returns the direction associated with this QueueElement.
     * @return The direction of this QueueElement.
     */
    public Side getDirection() {return direction;}

    /**
     * Returns the index of this QueueElement in the queue.
     * @return The index of this QueueElement.
     */
    public int getIdx() {return idx;}

    /**
     * Sets the index of this QueueElement in the queue.
     * @param idx The new index of this QueueElement.
     */
    public void setIdx(int idx) {this.idx = idx;}
}
