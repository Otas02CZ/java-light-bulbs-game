package com.example.game.cli;

import java.io.IOException;

import com.example.game.logger.Logger;
import com.example.game.model.common.MapGenerator;

public class MainCLI {
    public static void main(String[] args) {
        try {
            Logger.init();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        Logger.obtain().log("nadhera");
        MapGenerator mapGenerator = new MapGenerator(5, 5, 0.2f);
        mapGenerator.generateMap();
        mapGenerator.printMapTerminal();
        mapGenerator.randomizeMap(20);
        mapGenerator.printMapTerminal();
    }
}
