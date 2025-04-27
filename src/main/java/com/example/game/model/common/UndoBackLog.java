package com.example.game.model.common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UndoBackLog {

    final private ArrayList<Position> buffer;
    private int currentIndex;

    public UndoBackLog() {
        buffer = new ArrayList<>();
        currentIndex = -1;
    }

    public void setIndex(int index) {currentIndex = index;}

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

    public Position Undo() {
        currentIndex--;
        return  buffer.get(currentIndex+1);
    }

    public Position Redo() {
        currentIndex++;
        return buffer.get(currentIndex);
    }

    public boolean canUndo() {
        return currentIndex != -1;
    }

    public boolean canRedo() {
        return currentIndex < buffer.size() - 1;
    }

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
