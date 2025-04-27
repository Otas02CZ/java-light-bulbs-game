
package com.example.game.shared;

public interface ToolField {
    void turn();

    boolean north();

    boolean east();

    boolean south();

    boolean west();

    boolean light();

    boolean isLink();

    boolean isBulb();

    boolean isPower();

    int remainingTurns();

    int usedTurns();
    
}