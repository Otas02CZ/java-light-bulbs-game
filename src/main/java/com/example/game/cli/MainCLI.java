package com.example.game.cli;

import java.io.IOException;

import com.example.game.model.Game;
import com.example.game.model.common.GameNode;
import com.example.game.model.common.Position;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainCLI {
    public static void main(String[] args) {

        // create a new game
        Game game = Game.generate(10, 10, 0.1f, 5);
        game.startTimer(60*5);

        // load game1.json
        //Game game = Game.load("./game1.json");
        //if (game == null) throw new RuntimeException("Error on loading game");

        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile("\\d+ \\d+"); // regex: number, space, number

        // eventloop
        while (true) {
            // print map
            game.printMapTerminal();
            System.out.println("Undo: " + (game.canUndo() ? "Yes" : "No"));
            System.out.println("Redo: " + (game.canRedo() ? "Yes" : "No"));
            System.out.println("Time left: " + game.timeLeft());

            if (game.hasWon()) break;

            // get user input
            System.out.print("Enter row & col (separated by a space): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("/undo")) {
                game.undo();
                continue;
            }

            if (input.equalsIgnoreCase("/redo")) {
                game.redo();
                continue;
            }

            if (input.equalsIgnoreCase("/save")) {
                try {
                    game.save("./game1.json");
                } catch (IOException e) {
                    System.out.println("ERROR saving game");
                }
                continue;
            }

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
