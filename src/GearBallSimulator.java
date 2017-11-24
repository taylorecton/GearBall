/**
 * Author           Taylor Ecton
 * File Name        GearBallSimulator.java
 * Date Modified    2017-09-23
 * Purpose          A class that handles user interaction with the gear ball.
 */

import java.util.Scanner;

public class GearBallSimulator {
    // driving function for the program
    public static void main(String[] args) {
        GearBall gearBall = new GearBall();

        System.out.println("Welcome to the Gear Ball Simulator!\n");
        displayMenu();

        // repeatedly prints the menu and gets user input until
        // the user chooses the quit option
        while (true) {
            String userInput = getUserInput();
            gearBall = processInput(userInput, gearBall);
            System.out.println();
            displayMenu();
        }
    }

    /**
     * Prints out user menu.
     */
    private static void displayMenu() {
        System.out.println("Please choose from the following options:");
        System.out.println("\t1. Randomize Gear Ball");
        System.out.println("\t2. Solve Gear Ball");
        System.out.println("\t3. Print Gear Ball");
        System.out.println("\t4. Reset Gear Ball");
        System.out.println("\t5. Check if Solved");
        System.out.println("\t6. Verify Moves");
        System.out.println("\t7. Help");
        System.out.println("\t8. Quit");
        System.out.print("\n(Type 1, 2, 3, 4, 5, 6, 7, or 8 and press 'ENTER'): ");
    }

    /**
     * Gets the user input, validating to make sure entry is valid.
     * @return The input made by the user.
     */
    private static String getUserInput() {
        String input;
        Scanner scanner = new Scanner(System.in);

        input = scanner.nextLine();
        while (!isValid(input)) {
            System.out.println("\nSorry! 1, 2, 3, 4, 5, 6, 7, and 8 are the only valid options.");
            System.out.print("(Type 1, 2, 3, 4, 5, 6, 7, or 8 and press 'ENTER'): ");
            input = scanner.nextLine();
        }

        return input;
    }

    /**
     * Verifies validity of menu input.
     * @param input User input string.
     * @return true if input is valid, false otherwise
     */
    private static boolean isValid(String input) {
        if (input.equals("1") || input.equals("2") ||
                input.equals("3") || input.equals("4") ||
                input.equals("5") || input.equals("6") ||
                input.equals("7") || input.equals("8"))
            return true;
        else
            return false;
    }

    /**
     * Calls the appropriate function based on user input
     * @param input User input string
     * @param gearBall The GearBall instance.
     */
    private static GearBall processInput(String input, GearBall gearBall) {
        GearBallRandomizer gbRandomizer = new GearBallRandomizer();
        GearBallAStar gbAStar = new GearBallAStar(gearBall);
        long startTime;
        long stopTime;
        double elapsedInSeconds;

        switch (input) {
            case "1":
                // Randomize GearBall
                gbRandomizer.randomize(gearBall);
                break;
            case "2":
                startTime = System.currentTimeMillis();
                if (gbAStar.performSearch()) {
                    stopTime = System.currentTimeMillis();
                    elapsedInSeconds = (stopTime - startTime) / 1000.0;

                    gbAStar.getCurrentNode().printGearBall();
                    gearBall = gbAStar.getCurrentNode();

                    System.out.println("Solution found at depth: " + gbAStar.getSolutionDepth());
                    System.out.println("Number of nodes expanded: " + gbAStar.getNodesExpanded());
                    System.out.println("Elapsed time (seconds): " + elapsedInSeconds + "\n");
                } else {
                    System.out.println("Unable to find a solution.");
                }
                break;
            case "3":
                // Print GearBall
                gearBall.printGearBall();
                System.out.println();
                break;
            case "4":
                // Reset GearBall
                gearBall.reset();
                break;
            case "5":
                // Check if Solved
                if (gearBall.isSolved()) {
                    System.out.println("The gear ball is in a solved configuration!\n");
                } else {
                    System.out.println("The gear ball is NOT in a solved configuration!\n");
                }
                break;
            case "6":
                // Verify Moves
                gbRandomizer.verifyMoves(gearBall);
                break;
            case "7":
                // Print Help
                printHelp();
                break;
            case "8":
                // Quit
                System.out.println("Thanks for using Gear Ball Simulator!");
                System.exit(0);
                break;
            default:
                // Execution should not reach this
                System.err.println("Encountered error - Invalid menu option: " + input);
                System.exit(1);
        }

        return gearBall;
    }

    /**
     * Prints menu help.
     */
    private static void printHelp() {
        System.out.println("This program simulates a gear ball!\n");
        System.out.println("The menu options perform the following functions:\n");
        System.out.println("\t1 - This puts the gear ball into a random configuration!");
        System.out.println("\t2 - This option attempts to solve the gear ball using the");
        System.out.println("\t    A* algorithm!");
        System.out.println("\t3 - This prints the current configuration of the gear ball!");
        System.out.println("\t4 - This resets the gear ball to its initial configuration!");
        System.out.println("\t5 - This tells you if the gear ball is currently solved!");
        System.out.println("\t6 - This makes a pre-defined sequence of moves, followed by a");
        System.out.println("\t    sequence that undoes all of those moves. This is used to");
        System.out.println("\t    verify the system correctly manipulates the gear ball.");
        System.out.println("\t7 - This prints out help! (<-- YOU ARE HERE)");
        System.out.println("\t8 - This ends the simulation!\n");
    }


}
