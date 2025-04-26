package com.example.game.cli;

import com.example.game.model.common.MapGenerator;

public class MainCLI {
    public static void main(String[] args) {
        MapGenerator mapGenerator = new MapGenerator(5, 5, 0.2f);
        mapGenerator.generateMap();
        mapGenerator.printMapTerminal();
    }
}
