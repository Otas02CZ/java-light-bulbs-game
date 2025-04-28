package com.example.game.gui.shared;

public class GameConfig {
    private String name;
    private int size;
    private double emptyCoef;
    private double shuffleCoef;
    private int maxTime;

    public GameConfig(String name, int size, double emptyCoef, double shuffleCoef, int maxTime) {
        this.name = name;
        this.size = size;
        this.emptyCoef = emptyCoef;
        this.shuffleCoef = shuffleCoef;
        this.maxTime = maxTime;
    }

    public String getName() {
        return name;
    }

    public double getEmptyCoef() {
        return emptyCoef;
    }

    public double getShuffleCoef() {
        return shuffleCoef;
    }

    public int getSize() {
        return size;
    }

    public int getMaxTime() {
        return maxTime;
    }
}
