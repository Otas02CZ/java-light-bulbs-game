package com.example.game.cli;

import com.example.game.model.common.MapGenerator;

public class MainCLI {
    public static void main(String[] args) {
        MapGenerator mapGenerator = new MapGenerator(10, 10);
        mapGenerator.generateMap();
        mapGenerator.printMapTerminal();
    }
}
