/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: ToolEnvironment.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * AUTHOR: Martin Mendl x247581@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Interface through which GUI access Game model
 * NOTE: This interface was inspired by the provided interface in HW2
 */

package com.example.game.shared;

import java.io.IOException;

/**
 * The ToolEnvironment interface defines the necessary methods for managing
 * and interacting with the game model. The interface provides functionalities
 * for querying the game's dimensions, accessing and modifying individual cells,
 * managing the state of the game, and controlling game mechanics such as timers
 * and undo/redo actions.
 */
public interface ToolEnvironment {

    /**
     * Retrieves the number of rows in the game grid.
     *
     * @return the total number of rows in the game grid.
     */
    int rows();

    /**
     * Retrieves the number of columns in the game grid.
     *
     * @return the total number of columns in the game grid.
     */
    int cols();

    /**
     * Retrieves the ToolField object located at the specified grid coordinates.
     * The coordinates (x, y) correspond to the column and row indices in the game grid.
     *
     * @param x the x-coordinate (column index) of the field to retrieve.
     * @param y the y-coordinate (row index) of the field to retrieve.
     * @return the ToolField object located at the specified coordinates (x, y).
     */
    ToolField fieldAt(int x, int y);

    /**
     * Reverts the most recent action performed in the game, restoring the game
     * state to the state before the last action. If no actions can be undone,
     * this method has no effect.
     */
    void undo();

    /**
     * Redoes the most recent undone action in the game, restoring the game state
     * to the state it was in before the corresponding action was undone. If there
     * are no actions to redo, this method has no effect.
     */
    void redo();

    /**
     * Determines whether there is an action that can be undone.
     *
     * @return true if there is at least one action that can be undone, false otherwise.
     */
    boolean canUndo();

    /**
     * Determines whether there is an action available to redo.
     *
     * @return true if there is at least one undone action that can be redone,
     *         false otherwise.
     */
    boolean canRedo();

    /**
     * Determines if the current game state meets the conditions for a win.
     *
     * @return true if the player has achieved a winning state, false otherwise.
     */
    boolean hasWon();

    /**
     * Starts a countdown timer with the given duration in seconds. The timer runs
     * in the background and will notify observers with updates or actions related
     * to the timer, if implemented.
     *
     * @param seconds the total duration for the timer in seconds. Must be a
     *                non-negative integer indicating the countdown time.
     */
    void startTimer(int seconds);

    /**
     * Stops the currently running countdown timer. This method halts the timer
     * and prevents any additional updates or notifications related to the timer.
     * If no timer is active, invoking this method has no effect.
     */
    void stopTimer();

    /**
     * Retrieves the total number of bulbs that are currently lit in the game grid.
     *
     * @return the count of bulbs that are turned on.
     */
    int getBulbsOn();

    /**
     * Retrieves the total number of bulbs present in the game grid.
     *
     * @return the total count of bulbs in the game grid.
     */
    int getBulbsTotal();

    /**
     * Saves the current state of the game to a JSON file
     * located at the specified path. The method may throw an IOException
     * if an error occurs during the save process, such as when the file cannot be
     * created or written to.
     *
     * @param path the file path where the game should be saved to.
     *             It must be a valid, writable file path.
     * @throws IOException if an I/O error occurs during the saving process.
     */
    void save(String path) throws IOException;

    /**
     * Starts a background thread that will solve the game step by step. Each step
     * follows after a delay specified in parameter seconds. GUI updates are propagated
     * normally by the use of GUIObserver pattern.
     *
     * @param seconds the delay between each step of the solver
     */
    void solve(int seconds);

    /**
     * Stops the currently running solver process, if one is active. This method halts the
     * background thread responsible for solving the game and prevents further automated
     * actions from being performed by the solver. If no solver process is active, invoking
     * this method has no effect.
     */
    void stopSolver();

    /**
     * Determines whether the solver has been used in the game.
     *
     * @return true if the solver was used, false otherwise.
     */
    boolean usedSolver();

    /**
     * Retrieves the total number of turns used in the game.
     *
     * @return the total number of turns used in the game.
     */
    int getUsedTurnsTotal();
}