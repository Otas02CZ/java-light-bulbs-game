package com.example.game.model.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapElement {

    final private Position pos;
    private final Logger logger = Logger.getLogger(getClass().getName());

    private int rotated = 0;
    private boolean isRoot = false;

    private boolean north = false;
    private boolean south = false;
    private boolean west = false;
    private boolean east = false;

    public MapElement(Position pos) {
        this.pos = pos;
    }

    public void setNorth() {north = true;};
    public void setSouth() {south = true;};
    public void setWest() {west = true;};
    public void setEast() {east = true;};

    public Position getPos() {return pos;}
    public boolean hasConnections() {return north || south || west || east;}

    public boolean isConnectedOnSide(Side side) {
        return switch (side) {
            case NORTH -> north;
            case SOUTH -> south;
            case WEST -> west;
            case EAST -> east;
        };
    }

    public void setRoot() {isRoot = true;};

    public void rotate() {

        logger.log(Level.INFO, "Rotating " + this.getPos());

        // north <-> East
        boolean tmp = north;
        north = east;
        east = tmp;

        //  north <-> south
        tmp = south;
        south = north;
        north = tmp;

        // north <- West
        tmp = west;
        west = north;
        north = tmp;

        rotated++;
    }

    public int getRotated() {return rotated;}

    public Side[] getSides() {
        List<Side> sidesList = new ArrayList<>();

        if (north) sidesList.add(Side.NORTH);
        if (south) sidesList.add(Side.SOUTH);
        if (west)  sidesList.add(Side.WEST);
        if (east)  sidesList.add(Side.EAST);

        return sidesList.toArray(new Side[0]);
    }

    public boolean isPower() {
        return isRoot;
    }

    public boolean isLight() {
        int count = 0;
        count += north ? 1 : 0;
        count += south ? 1 : 0;
        count += west ? 1 : 0;
        count += east ? 1 : 0;
        return count == 1;
    }

    public String[] getRepresentation(String bfr1, String bfr2, String bfr3) {
        //String signal = "\uD83D\uDFE9";
        //String space = "\uD83E\uDDAF";
        String signal = "XX";
        String space = "  ";
        String middle = (hasConnections()) ? signal : space;
        bfr1 += space + ((isConnectedOnSide(Side.NORTH)) ? signal : space) + space;
        bfr2 += ((isConnectedOnSide(Side.WEST)) ? signal : space) + middle + ((isConnectedOnSide(Side.EAST)) ? signal : space);
        bfr3 += space + ((isConnectedOnSide(Side.SOUTH)) ? signal : space) + space;
        return new String[]{bfr1, bfr2, bfr3};
    }
}
