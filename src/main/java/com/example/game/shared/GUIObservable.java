/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: GUIObservable.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Observer and Observable interfaces for notification of GUI
 * elements from Game model when GUI redraws are necessary.
 */

package com.example.game.shared;

/**
 * Represents an observable interface for GUI-related updates, following the
 * observer design pattern. Classes implementing this interface allow GUIObservers
 * to register, remove, and be notified of changes that require the GUI to be refreshed
 * or redrawn.
 */
public interface GUIObservable {
    /**
     * Registers the specified GUIObserver to be notified of updates or changes
     * that require the GUI to be refreshed or redrawn. This allows the observer
     * to react to changes in the application's state by updating the visual
     * representation of GUI elements.
     *
     * @param observer the GUIObserver to be registered for receiving updates
     *                 when there are changes requiring a GUI refresh or redraw
     */
    void setupGUIObserver(GUIObserver observer);

    /**
     * Removes the specified observer from the list of registered GUI observers.
     * The observer will no longer be notified about updates or changes that
     * require GUI redraws or updates.
     *
     * @param observer the GUIObserver to be removed from the list of observers
     */
    void removeGUIObserver(GUIObserver observer);

    /**
     * Notifies all registered GUI observers about updates or changes that require
     * the GUI to be refreshed or redrawn. This method is typically invoked when
     * there is a need to update the visual representation of GUI elements.
     */
    void notifyGUIObserver();

    /**
     * Represents an observer in the observer design pattern for GUI updates.
     * Implementing classes will be notified when there is a change or update
     * that requires the GUI to be refreshed or redrawn.
     */
    interface GUIObserver {
        /**
         * Called to notify the observer that GUI
         * needs to be updated. This method is typically invoked when there are
         * changes in the application's state that require a refresh or redraw of
         * the GUI components in order to display updated information or visuals
         * to the user. Implementations should handle the specific details of
         * refreshing the GUI.
         */
        void updateGUI();
    }
}
