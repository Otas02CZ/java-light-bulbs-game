package com.example.game.shared;

public interface TimerObservable {

    void setupTimerObserver(TimerObserver observer);

    void removeTimerObserver(TimerObserver observer);

    void notifyTimerObserver();

    public interface TimerObserver {
        void updateTimer(int time);
    }
}
