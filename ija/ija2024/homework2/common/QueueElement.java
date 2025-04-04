package ija.ija2024.homework2.common;
import java.util.Random;

public class QueueElement {

    private int cost;
    private MapElement node;
    private Side direction;
    private int idx;

    public QueueElement(MapElement node, Side direction, Random random, int idx) {
        this.node = node;
        this.direction = direction;
        this.cost = random.nextInt(201); // Generates number in [min, max]
        this.idx = idx;
    }

    public int getCost() {return cost;};
    public MapElement getNode() {return node;}
    public Side getDirection() {return direction;}
    public int getIdx() {return idx;}
    public void setIdx(int idx) {this.idx = idx;}
}
