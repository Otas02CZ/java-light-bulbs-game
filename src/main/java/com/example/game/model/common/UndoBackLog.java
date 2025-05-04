/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: UndoBackLog.java
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Manages the undo and redo functionality for game moves,
 * storing positions and providing JSON serialization.
 */

package com.example.game.model.common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class representing an undo/redo buffer for game moves.
 * It stores positions and allows for undoing and redoing moves.
 */
public class UndoBackLog {

    /**
     * Represents the buffer of moves.
     */
    final private ArrayList<Position> buffer;

    /**
     * Current index in the buffer.
     */
    private int currentIndex;

    /**
     * Constructor for the UndoBackLog class.
     * Initializes the buffer and sets the current index to -1.
     */
    public UndoBackLog() {
        buffer = new ArrayList<>();
        currentIndex = -1;
    }

    /**
     * Sets the current index to the specified value.
     * @param index The index to set.
    */
    public void setIndex(int index) {currentIndex = index;}

    /**
     * Adds a new move to the buffer.
     * @param position The position to add.
     * This method removes all moves after the current index before adding the new move.
    */
    public void AddMove(Position position) {

        // need to remove all the data form the CurrentIndex+1 to the end of the array
        int tmpIndex = currentIndex+1;
        while (tmpIndex < buffer.size()) {
            buffer.remove(tmpIndex);
        }

        // add to buffer
        buffer.add(position);
        currentIndex++;
    }

    /**
     * Undoes the last move and returns the position before the last move.
     * @return The position before the last move.
    */
    public Position Undo() {
        currentIndex--;
        return  buffer.get(currentIndex+1);
    }

    /**
     * Redoes the last undone move and returns the position after the last undone move.
     * @return The position after the last undone move.
    */
    public Position Redo() {
        currentIndex++;
        return buffer.get(currentIndex);
    }

    /**
     * Checks if there are any moves to undo.
     * @return true if there are moves to undo, false otherwise.
     */
    public boolean canUndo() {
        return currentIndex != -1;
    }

    /**
     * Checks if there are any moves to redo.
     * @return true if there are moves to redo, false otherwise.
     */
    public boolean canRedo() {
        return currentIndex < buffer.size() - 1;
    }

    /**
     * Converts the UndoBackLog to a JSON object.
     * @return A JSON object representing the UndoBackLog.
     */
    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        json.put("currentIndex", currentIndex);

        JSONArray array = new JSONArray();
        for (Position position : buffer) {
            JSONObject obj = new JSONObject();
            obj.put("row", position.getRow());
            obj.put("col", position.getCol());
            array.put(obj);
        }
        json.put("buffer", array);
        return json;
    }
}
