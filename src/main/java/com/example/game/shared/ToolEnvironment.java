package com.example.game.shared;

import com.example.game.shared.ToolField;

public interface ToolEnvironment {
    int rows();

    int cols();

    ToolField fieldAt(int x, int y);
}