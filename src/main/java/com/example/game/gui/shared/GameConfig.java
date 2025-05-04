/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: GameConfig.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: Stores a single default configuration of the game
 */

package com.example.game.gui.shared;

/**
 * GameConfig class for storing default configurations
 */
public class GameConfig {

    /**
     * name of the configuration
     */
    private final String name;

    /**
     * size of the game map
     */
    private final int size;

    /**
     * empty coefficient
     */
    private final double emptyCoefficient;

    /**
     * shuffling strength coefficient
     */
    private final double shuffleCoefficient;

    /**
     * max time duration of the game
     */
    private final int maxTime;

    /**
     * GameConfig constructor
     * @param name of the config (difficulty)
     * @param size of the map
     * @param emptyCoefficient denoting how many cells will be empty
     * @param shuffleCoefficient denoting strength of the shuffling
     * @param maxTime of the game
     */
    public GameConfig(String name, int size, double emptyCoefficient, double shuffleCoefficient, int maxTime) {
        this.name = name;
        this.size = size;
        this.emptyCoefficient = emptyCoefficient;
        this.shuffleCoefficient = shuffleCoefficient;
        this.maxTime = maxTime;
    }

    /**
     * returns name of the game
     * @return difficulty name
     */
    public String getName() {
        return name;
    }

    /**
     * returns empty coefficient
     * @return empty coefficient
     */
    public double getEmptyCoefficient() {
        return emptyCoefficient;
    }

    /**
     * returns shuffle coefficient
     * @return shuffle coefficient
     */
    public double getShuffleCoefficient() {
        return shuffleCoefficient;
    }

    /**
     * returns size of the game map
     * @return size of the map
     */
    public int getSize() {
        return size;
    }

    /**
     * returns max duration of the game
     * @return maximum duration of the game
     */
    public int getMaxTime() {
        return maxTime;
    }
}
