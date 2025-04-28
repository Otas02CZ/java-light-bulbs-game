package com.example.game.shared;

public interface GUIObservable {
    void setupGUIObserver(GUIObserver observer);

    void removeGUIObserver(GUIObserver observer);

    void notifyGUIObserver();

    public interface GUIObserver {
        void updateGUI();
    }
}
