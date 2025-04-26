package com.example.game.shared;

public interface ToolEnvironment {
    int rows();

    int cols();

    ToolField fieldAt(int x, int y);

    void undo();

    void redo();

    boolean canUndo();

    boolean canRedo();

    boolean hasWon();

}