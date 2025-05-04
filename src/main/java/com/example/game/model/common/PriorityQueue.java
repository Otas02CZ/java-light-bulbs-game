/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: PriorityQueue.java
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Implementation of a priority queue for managing game elements
 * with cost-based prioritization and movement execution.
 */

package com.example.game.model.common;

import java.util.Random;
import java.util.ArrayList;

/**
 * Class representing a priority queue for managing game elements.
 * The queue is used to prioritize elements based on their cost and
 * allows for the execution of moves between elements.
 */
public class PriorityQueue {

    /**
     * Represents the queue of elements.
     */
    final private ArrayList<Object> queue;

    /**
     * Random number generator for generating random values.
     */
    final private Random random;

    /**
     * Constructor for the PriorityQueue class.
     * Initializes the queue and random number generator.
     * @param random number generator
     */
    public PriorityQueue(Random random) {
        queue = new ArrayList<>();
        this.random = random;
    }

    /**
     * Adds a new element to the queue.
     * @param node The MapElement to be added.
     * @param side The direction associated with the element.
     */
    public void Add(MapElement node, Side side) {
        QueueElement elm = new QueueElement(node, side, random, queue.size());
        queue.add(elm);
    }

    /**
     * Retrieves the element at the specified index in the queue.
     * @param index The index of the element to retrieve.
     * @return The QueueElement at the specified index, or null if the index is out of bounds.
     */
    public QueueElement getQueueElement(int index) {
        if (index < 0 || index >= queue.size()) return null;
        return (QueueElement) queue.get(index);
    }

    /**
     * Finds the element with the minimum cost in the queue.
     * @return The QueueElement with the minimum cost, or null if the queue is empty.
     */
    public QueueElement findMinCost() {
        int minCost = -1;
        int index = 0;
        for (int i = 0; i < queue.size(); i++) {
            QueueElement elm = (QueueElement) queue.get(i);
            if (elm.getCost() < minCost || minCost == -1) {
                minCost = elm.getCost();
                index = i;
            }
        }
        if (minCost == -1) {
            return null;
        }

        QueueElement elm = (QueueElement) queue.get(index);
        elm.setIdx(index);
        return elm;
    }

    /**
     * Retrieves the size of the queue.
     * @return The number of elements in the queue.
     */
    public int getSize() {
        return queue.size();
    }

    /**
     * Removes the element at the specified index from the queue.
     * @param idx The index of the element to remove.
     * If the index is out of bounds, no action is taken.
    */
    public void removeIdx(int idx) {
        if (idx < 0 || idx >= queue.size()) {
            return;
        }

        queue.remove(idx);
    }

    /**
     * Executes a move between the specified QueueElement and its neighbor.
     * @param elm The QueueElement to move.
     * @param neighbour The MapElement representing the neighbor.
     */
    public void executeMove(QueueElement elm, MapElement neighbour) {

        MapElement root = elm.getNode();
        Side side = elm.getDirection();

        switch (side) {
            case NORTH:
                root.setNorth();
                neighbour.setSouth();
                break;
            case EAST:
                root.setEast();
                neighbour.setWest();
                break;
            case SOUTH:
                root.setSouth();
                neighbour.setNorth();
                break;
            case WEST:
                root.setWest();
                neighbour.setEast();
                break;
            default:
                return;
        }
        queue.remove(elm.getIdx());
    }
}
