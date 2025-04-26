
package com.example.game.shared;

import com.example.game.shared.Observable;

public interface ToolField extends Observable {
    void turn();

    boolean north();

    boolean east();

    boolean south();

    boolean west();

    boolean light();

    boolean isLink();

    boolean isBulb();

    boolean isPower();
}