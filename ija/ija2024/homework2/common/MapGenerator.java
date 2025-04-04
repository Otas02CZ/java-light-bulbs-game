package ija.ija2024.homework2.common;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class MapGenerator {

    final private int rows;
    final private int cols;
    private MapElement[][] map;
    private Position rootPosition;
    private Random random;
    private PriorityQueue queue;

    public MapGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        map = new MapElement[rows][cols];
        this.random = new Random();

        // fill in blank elements
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                map[y][x] = new MapElement(new Position(y, x)); // replace later with actual stuff
                // why the fuck do you want the first param in position be y????
                /*
                     col -> (in a normal world that would be x)
                row  +-------+
                ||   |       |
                \/   |       |
                (y)  +-------+

                 from this diagram, i would use what normal people do, and use the format (x, y)
                 but for some reason you have (row, col) which means you have (y, x), which is stupid .....
                */
            }
        }

        // create the queue
        queue = new PriorityQueue(random);

        int x = random.nextInt(rows);
        int y = random.nextInt(cols);

        // add the initial moves for the root element
        MapElement root = map[y][x];
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

    // Method for printing the map, for cmd line only
    public void printMapTerminal() {

        StringBuilder bfr0 = new StringBuilder();
        for (int x = 0; x < cols; x++) {
            bfr0.append(String.format("%4d  ", x));
        }
        System.out.println(bfr0); // Print column indexes

        for (int y = 0; y < rows; y++) {
            String bfr1 = "  ";
            String bfr2 = String.format("%-2d", y); // Add row index for bfr2
            String bfr3 = "  ";

            for (int x = 0; x < cols; x++) {
                String[] result = map[y][x].getRepresentation(bfr1, bfr2, bfr3);
                bfr1 = result[0];
                bfr2 = result[1];
                bfr3 = result[2];
            }

            System.out.println(bfr1);
            System.out.println(bfr2);
            System.out.println(bfr3);
        }
    }
}
