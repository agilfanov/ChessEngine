package com.company;

import java.util.Scanner;


public class Main {

    static final int gameLength = 100;
    static int engineDepth = 4;

    public static void main(String[] args) {
        Game game = new Game(engineDepth);
        Scanner input = new Scanner(System.in);
        System.out.println(Color.ANSI_RED + "-------------------------------------------------------------------------------------------------------------------------" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_BLUE + "What difficulty level should the engine be? Please enter a number between 1 and 4:" + Color.ANSI_RESET);
        engineDepth = input.nextInt();
        System.out.println(Color.ANSI_BLUE + "Game starting" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_RED + "-------------------------------------------------------------------------------------------------------------------------" + Color.ANSI_RESET);
        for (int i = 0; i < gameLength; i++) {
            game.printChessBoard();
            game.playTurnComputerBlack();

        }
    }
}



