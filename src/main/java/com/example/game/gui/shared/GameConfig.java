package com.example.game.gui.shared;

public class GameConfig {
    private final String name;
    private final int size;
    private final double emptyCoefficient;
    private final double shuffleCoefficient;
    private final int maxTime;

    public GameConfig(String name, int size, double emptyCoefficient, double shuffleCoefficient, int maxTime) {
        this.name = name;
        this.size = size;
        this.emptyCoefficient = emptyCoefficient;
        this.shuffleCoefficient = shuffleCoefficient;
        this.maxTime = maxTime;
    }

    public String getName() {
        return name;
    }

    public double getEmptyCoefficient() {
        return emptyCoefficient;
    }

    public double getShuffleCoefficient() {
        return shuffleCoefficient;
    }

    public int getSize() {
        return size;
    }

    public int getMaxTime() {
        return maxTime;
    }
}
