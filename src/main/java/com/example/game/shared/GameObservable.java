/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: GameObservable.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Observer and Observable interfaces for notification of Game
 * when game nodes trigger connection recalculation
 */

package com.example.game.shared;

import com.example.game.model.common.Position;

/**
 * Represents an observable entity in a game context. The interface follows the observer
 * design pattern, enabling the registration, removal, and notification of game observers,
 * particularly for scenarios where a recalculation of game state connections is necessary.
 * Classes implementing this interface are expected to maintain and manage their respective
 * game observers and notify them upon relevant state changes.
 */
public interface GameObservable {
    /**
     * Registers the specified GameObserver to receive notifications about changes
     * in the observables state. The observer will be notified when events
     * occur that require a recalculation or update of game state connections.
     *
     * @param observer the GameObserver to be registered for receiving notifications
     */
    void setupGameObserver(GameObserver observer);

    /**
     * Removes the specified GameObserver from the list of observers. After removal,
     * the observer will no longer receive notifications about changes or events
     * related to the game state that would otherwise require updates or recalculations.
     *
     * @param observer the GameObserver to be removed from the list of observers
     */
    void removeGameObserver(GameObserver observer);

    /**
     * Notifies all registered GameObservers of a change or event that requires
     * recalculation or validation of game state connections. This method is commonly
     * invoked after a significant state modification, such as the rotation of a game node,
     * which may impact current game connections or logic.
     */
    void notifyGameObserver();

    /**
     * Retrieves the current position of the observable entity in 2D space.
     *
     * @return the Position object representing the X and Y coordinates
     *         in a two-dimensional grid.
     */
    Position getPosition();

    /**
     * Represents an observer in the observer design pattern for game-related updates.
     * Implementing classes will be notified when there are changes in the game state
     * or game logic that require a recalculation or validation of game connections.
     * Observers registered to a GameObservable will receive notifications
     * when significant events or state updates occur in the game.
     */
    interface GameObserver {

        /**
         * Called to notify the observer of changes in the game state or game logic
         * that may require recalculation or validation of game connections. The method
         * is typically invoked by the observable entity when significant events or
         * updates occur, ensuring that the observer can act accordingly.
         *
         * @param observable the GameObservable instance that notifies the observer
         *                   of the change or event. It provides context for the update
         *                   and allows the observer to query the observable for
         *                   relevant state information if necessary.
         */
        void update(GameObservable observable);
    }
}