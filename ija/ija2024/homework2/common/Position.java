/**
 * IJA HW: 2
 * FILE: Position.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 03/2025
 * DESCRIPTION: Class representing XY position in 2D space
 */

package ija.ija2024.homework2.common;

/**
 * Stores XY coordinates in 2D space
 */
public final class Position {
    /**
     * X coordinate
     */
    private final int row;
    /**
     * Y coordinate
     */
    private final int col;

    /**
     * Constructs Position at given row and col
     * @param row int
     * @param col int
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return int X coordinate in 2D space
     */
    public int getRow() {
        return row;
    }

    /**
     * @return int Y coordinate in 2D space
     */
    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    //TODO: maybe change?
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    /**
     * Get Neighbor position in given direction
     * @param direction where to take the neighbor position
     * @return neighbor position in given direction
     */
    public Position getNeighbourPos(Side direction) {
        switch (direction) {
            case NORTH:
                return new Position(row - 1, col);
            case SOUTH:
                return new Position(row + 1, col);
            case EAST:
                return new Position(row, col + 1);
            case WEST:
                return new Position(row, col - 1);
            default:
                return null;
        }
    }
}
