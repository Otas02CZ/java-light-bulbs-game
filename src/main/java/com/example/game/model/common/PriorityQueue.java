package com.example.game.model.common;

import java.util.Random;
import java.util.ArrayList;

public class PriorityQueue {

    final private ArrayList<Object> queue;
    final private Random random;

    public PriorityQueue(Random random) {
        queue = new ArrayList<>();
        this.random = random;
}

    public void Add(MapElement node, Side side) {
        QueueElement elm = new QueueElement(node, side, random, queue.size());
        queue.add(elm);
    }

    public QueueElement getQueueElement(int index) {
        if (index < 0 || index >= queue.size()) return null;
        return (QueueElement) queue.get(index);
    }

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

    public int getSize() {
        return queue.size();
    }

    public void removeIdx(int idx) {
        if (idx < 0 || idx >= queue.size()) {
            return;
        }

        queue.remove(idx);
    }

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
