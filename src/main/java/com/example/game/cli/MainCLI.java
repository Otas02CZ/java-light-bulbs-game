package com.example.game.cli;

import java.io.IOException;

import com.example.game.logger.Logger;
import com.example.game.model.Game;
import com.example.game.model.common.GameNode;
import com.example.game.model.common.MapGenerator;
import com.example.game.model.common.Position;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainCLI {
    public static void main(String[] args) {
        try {
            Logger.init();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        Logger.obtain().log("nadhera");
        //MapGenerator mapGenerator = new MapGenerator(5, 5, 0.2f);
        //mapGenerator.generateMap();
        //mapGenerator.printMapTerminal();
        //mapGenerator.randomizeMap(20);
        //mapGenerator.printMapTerminal();

        Game game = Game.generate(3, 3, 0.1f, 5);

        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile("\\d+ \\d+"); // regex: number, space, number

        // eventloop
        while (true) {
            // print map
            game.printMapTerminal();

            if (game.hasWon()) break;

            // get user input
            System.out.print("Enter row & col (separated by a space): ");
            String input = scanner.nextLine();

            Matcher matcher = pattern.matcher(input);

            if (!matcher.matches()) {
                System.out.println("Invalid input! Please enter two numbers separated by a space.");
                continue;
            }

            String[] numbers = input.split(" ");
            int row = Integer.parseInt(numbers[0]);
            int col = Integer.parseInt(numbers[1]);

            GameNode node = game.node(new Position(row, col));
            if (node == null) {
                System.out.println("ERROR, invalid cords!");
                continue;
            }
            node.turn();
        }
        System.out.println("You won :D");
    }
}
