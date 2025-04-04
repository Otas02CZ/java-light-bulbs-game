package ija.ija2024.homework2.common;

public class MapElement {

    final private Position pos;

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

    public String[] getRepresentation(String bfr1, String bfr2, String bfr3) {
        String signal = "\uD83D\uDFE9";
        String space = "\uD83E\uDDAF";
        String middle = (hasConnections()) ? signal : space;
        bfr1 += space + ((isConnectedOnSide(Side.NORTH)) ? signal : space) + space;
        bfr2 += ((isConnectedOnSide(Side.WEST)) ? signal : space) + middle + ((isConnectedOnSide(Side.EAST)) ? signal : space);
        bfr3 += space + ((isConnectedOnSide(Side.SOUTH)) ? signal : space) + space;
        return new String[]{bfr1, bfr2, bfr3};
    }
}
