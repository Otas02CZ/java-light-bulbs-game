package com.example.game.model.common;

import java.util.Random;

public class MapGenerator {

    final private int rows;
    final private int cols;
    final private MapElement[][] map;
    private Position rootPosition;
    final private PriorityQueue queue;
    final private Random random;

    public MapGenerator(int rows, int cols, float emptyPercentage) {
        this.rows = rows;
        this.cols = cols;
        map = new MapElement[rows][cols];
        random = new Random();

        // fill in blank elements
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (Math.random() < emptyPercentage) {
                    map[y][x] = null;
                } else {
                    map[y][x] = new MapElement(new Position(y, x));
                }
            }
        }

        // create the queue
        queue = new PriorityQueue(random);
        MapElement root = null;

        while (root == null) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            // add the initial moves for the root element
            root = map[y][x];
            if (root != null) map[y][x].setRoot();
        }

        AddAvailableMoves(root);
        removeInvalidMoves();
    }

    private void removeInvalidMoves() {

        int idx = 0;
        QueueElement elm = queue.getQueueElement(idx);
        while (elm != null) {
            MapElement node = elm.getNode();
            Position newPos = node.getPos().getNeighbourPos(elm.getDirection());
            MapElement other = getMapElement(newPos);
            if (other == null || other.hasConnections()) {
                queue.removeIdx(idx);
                idx--; // in case we removed one element, we need to move back one slot, for the logic to work
            }

            elm = queue.getQueueElement(++idx);
        }
    }

    public MapElement getMapElement(Position pos) {

        int x = pos.getCol();
        int y = pos.getRow();
        // out of bounds check
        if (x < 0 || x >= cols || y < 0 || y >= rows) return null;
        return map[y][x];
    }

    private MapElement getNeighbor(QueueElement elm) {

        if (elm == null) return null;

        MapElement node = elm.getNode();
        Position pos = node.getPos();

        Position neighbourPos = pos.getNeighbourPos(elm.getDirection());
        return getMapElement(neighbourPos);
    }

    private void AddAvailableMoves(MapElement targetElement) {

        Position targetPos = targetElement.getPos();
        if (targetPos == null) throw new Error("std::POMOC!!!");

        // add the available moves
        for (Side side : Side.values()) {

            Position newPos = targetPos.getNeighbourPos(side);
            MapElement elm = getMapElement(newPos);
            if (elm == null) continue; // out of bounds
            if (elm.hasConnections()) continue; // it is allReady connected, leave it out
            queue.Add(targetElement, side);
        }
    }

    public void generateMap() {

        int overflowProtection = rows * cols * 10;
        int steps = 0;

        while (true) {
            if (steps >= overflowProtection) { throw new Error("overflow protection in map generation"); }

            // remove unavailable moves
            removeInvalidMoves();

            // find the min cost
            QueueElement elm = queue.findMinCost();

            // no more moves, finished
            if (elm == null) {
                return;
            }

            // execute the move
            MapElement neighbour = getNeighbor(elm);
            if (neighbour == null) return; // mby not needed?

            queue.executeMove(elm, neighbour);
            AddAvailableMoves(neighbour);
            steps++;
        }
    }

    public void randomizeMap(int amountOfRotations) {

        int rotationsDone = 0;
        while (rotationsDone < amountOfRotations) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);

            MapElement elm = map[x][y];
            if (elm == null) continue;

            elm.rotate();

            rotationsDone++;
        }
    }

    // Method for printing the map, for cmd line only
    public void printMapTerminal() {

        StringBuilder bfr0 = new StringBuilder();
        for (int x = 0; x < cols; x++) {
            bfr0.append(String.format("%4d  ", x));
        }
        System.out.println(bfr0); // Print column indexes

        for (int y = 0; y < rows; y++) {
            StringBuilder bfr1 = new StringBuilder("  ");
            StringBuilder bfr2 = new StringBuilder(String.format("%-2d", y)); // Add row index for bfr2
            StringBuilder bfr3 = new StringBuilder("  ");

            for (int x = 0; x < cols; x++) {
                if (map[y][x] == null) {
                    bfr1.append("      ");
                    bfr2.append("      ");
                    bfr3.append("      ");
                } else {

                String[] result = map[y][x].getRepresentation(bfr1.toString(), bfr2.toString(), bfr3.toString());
                bfr1 = new StringBuilder(result[0]);
                bfr2 = new StringBuilder(result[1]);
                bfr3 = new StringBuilder(result[2]);
                }
            }

            System.out.println(bfr1);
            System.out.println(bfr2);
            System.out.println(bfr3);
        }
    }
}
