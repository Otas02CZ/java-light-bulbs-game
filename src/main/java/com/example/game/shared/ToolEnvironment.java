package com.example.game.shared;

import java.io.IOException;

public interface ToolEnvironment {
    int rows();

    int cols();

    ToolField fieldAt(int x, int y);

    void undo();

    void redo();

    boolean canUndo();

    boolean canRedo();

    boolean hasWon();

    void startTimer(int seconds);

    void stopTimer();

    int getBulbsOn();

    int getBulbsTotal();

    void save(String path) throws IOException;

    void solve(int seconds);

    void stopSolver();

    boolean usedSolver();

    int getUsedTurnsTotal();
}