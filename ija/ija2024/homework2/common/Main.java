package ija.ija2024.homework2.common;

public class Main {
    public static void main(String[] args) {
       MapGenerator mapGenerator = new MapGenerator(10, 10);
       mapGenerator.generateMap();
       mapGenerator.printMapTerminal();
    }
}