/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: TimerManager.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Timer implementation for the Game model
 */

package com.example.game.model.common;

import com.example.game.shared.TimerObservable;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that handles Timer. Internally ScheduledExecutorService is used.
 */
public class TimerManager implements TimerObservable {

    /**
     * Holds the remaining time
     */
    private final AtomicInteger timeLeft = new AtomicInteger();
    /**
     * scheduled executor service for timer
     */
    private ScheduledExecutorService scheduledExecutor;

    /**
     * List of observers to notify when a second passes
     */
    private final List<TimerObservable.TimerObserver> timerObservers = new ArrayList<>();

    /**
     * Starts timer with specified time, each second notifies the observers
     * @param initialTime to start the timer with
     */
    public void startTimer(int initialTime) {
        this.timeLeft.set(initialTime);

        // shutdown existing executor
        if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
            scheduledExecutor.shutdownNow(); // Stop any previous timer
        }

        // create new executor
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        // schedule task to run every 1 second
        scheduledExecutor.scheduleAtFixedRate(() -> {
            int currentValue = timeLeft.decrementAndGet();
            if (currentValue <= 0) {
                stopTimer(); // stop when timer reaches 0
            }

            // notify observers on the JavaFX application thread
            Platform.runLater(this::notifyTimerObserver);

        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Stop timer if it exists
     */
    public void stopTimer() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
        }
    }

    /**
     * Register specified observer for further notifications
     * @param observer the TimerObserver instance to be registered for timer updates
     */
    public void setupTimerObserver(TimerObserver observer) {
        timerObservers.add(observer);
    }

    /**
     * Remove specified observer from registered observers
     * @param observer the TimerObserver instance to be removed from receiving timer updates
     */
    public void removeTimerObserver(TimerObserver observer) {
        timerObservers.remove(observer);
    }

    /**
     * Notify all registered observers
     */
    public void notifyTimerObserver() {
        for (TimerObserver observer : timerObservers) {
            observer.updateTimer(timeLeft.get());
        }
    }

    /**
     * Return remaining time of the timer
     * @return remaining time
     */
    public int getTimeLeft() {
        return timeLeft.get();
    }
}