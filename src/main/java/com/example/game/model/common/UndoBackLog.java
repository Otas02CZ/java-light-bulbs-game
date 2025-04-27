package com.example.game.model.common;

import java.util.ArrayList;

public class UndoBackLog {

    final private ArrayList<Position> buffer;
    private int currentIndex;

    public UndoBackLog() {
        buffer = new ArrayList<>();
        currentIndex = -1;
    }

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
}
