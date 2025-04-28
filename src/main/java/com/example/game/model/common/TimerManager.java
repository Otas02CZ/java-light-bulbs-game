package com.example.game.model.common;

import com.example.game.shared.TimerObservable;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerManager implements TimerObservable {

    private final AtomicInteger timeLeft = new AtomicInteger(); // Holds the remaining time
    private ScheduledExecutorService scheduledExecutor;
    private final List<TimerObservable.TimerObserver> timerObservers = new ArrayList<>();

    // Starts the timer with an initial time
    public void startTimer(int initialTime) {
        this.timeLeft.set(initialTime);

        if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
            scheduledExecutor.shutdownNow(); // Stop any previous timer
        }

        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        // Schedule a task to run every 1 second
        scheduledExecutor.scheduleAtFixedRate(() -> {
            int currentValue = timeLeft.decrementAndGet();
            if (currentValue <= 0) {
                stopTimer(); // Stop when timer reaches 0
            }

            // Notify on the JavaFX application thread
            Platform.runLater(this::notifyTimerObserver);

        }, 0, 1, TimeUnit.SECONDS); // Initial delay: 0, interval: 1 second
    }

    // Stops the timer
    public void stopTimer() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
        }
    }

    public void setupTimerObserver(TimerObserver observer) {
        timerObservers.add(observer);
    }

    public void removeTimerObserver(TimerObserver observer) {
        timerObservers.remove(observer);
    }

    // Notify observer (called on JavaFX thread)
    public void notifyTimerObserver() {
        for (TimerObserver observer : timerObservers) {
            observer.updateTimer(timeLeft.get());
        }
    }

    public int getTimeLeft() {
        return timeLeft.get();
    }
}