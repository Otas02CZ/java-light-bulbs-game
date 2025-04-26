/**
 * IJA HW: 2
 * FILE: GameObservable.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Observer and Observable interfaces for notification of Game
 * when game nodes require connection recalculation
 */

package com.example.game.shared;

/**
 * Game observable interface for observables that need to notify Game about
 * need of connection recheck
 */
public interface GameObservable {
    /**
     * Setup given observer as Game observer to notify about
     * changes that need connection recheck
     * @param observer to use for notifying about Game events
     */
    void setupGameObserver(GameObserver observer);
    /**
     * Setup given observer as Game observer to notify about
     * changes that need connection recheck
     * @param observer to use for notifying about Game events
     */
    void removeGameObserver();
    /**
     * Notify Game observer when connection recalculation is required
     */
    void notifyGameObserver();

    /**
     * Game observer interface for Game that needs to be notified when
     * connection recalculation is needed
     */
    public interface GameObserver {
        /**
         * Entry point in Game observer when connection check is needed
         * @param observable that issued the notification
         */
        void update(GameObservable observable);
    }
}