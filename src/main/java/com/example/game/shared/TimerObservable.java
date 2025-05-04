/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: TimerObservable.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Observer and Observable interfaces for notification of GUI
 * elements from Game model. Notifies about each second that passed since
 * the Timer was started.
 */

package com.example.game.shared;

/**
 * TimerObservable serves as the interface for managing timer-based operations
 * and notifying observers about timer-related updates.
 *
 * Classes implementing this interface can allow observers to subscribe to
 * timer updates, remove subscriptions, and notify all subscribed observers
 * when each second passes.
 */
public interface TimerObservable {

    /**
     * Sets up a TimerObserver to receive timer-related updates. The provided observer
     * will be notified by the implementing class when timer updates occur, such as
     * when each second elapses.
     *
     * @param observer the TimerObserver instance to be registered for timer updates
     */
    void setupTimerObserver(TimerObserver observer);

    /**
     * Removes a TimerObserver that was previously registered to receive timer-related updates.
     * Once removed, the observer will no longer be notified by the implementing class
     * about timer updates or changes.
     *
     * @param observer the TimerObserver instance to be removed from receiving timer updates
     */
    void removeTimerObserver(TimerObserver observer);

    /**
     * Notifies all registered timer observers about the occurrence of a timer-related event.
     * This method should be called by the implementing class when timer updates occur,
     * such as the passage of a second. Each registered observer will
     * be updated with relevant timer information.
     */
    void notifyTimerObserver();

    /**
     * Interface representing a timer observer used in the observer design pattern.
     * TimerObserver is used to respond to timer updates from a TimerObservable.
     * Implementing classes must provide the logic for handling notified timer changes.
     *
     * This interface facilitates a loose coupling between the timer logic (observable)
     * and the classes that need to react to timer changes (observers).
     */
    interface TimerObserver {
        /**
         * Called to notify the TimerObserver when a second passes. The provided parameter
         * contains the remaining amount of seconds until the timer times out.
         *
         * @param time remaining time until the timer reaches 0.
         */
        void updateTimer(int time);
    }
}
