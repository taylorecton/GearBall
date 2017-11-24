/**
 * Author           Taylor Ecton
 * File Name        GearBallRandomizer.java
 * Date Modified    2017-09-23
 * Purpose          A class that handles randomization of gear ball configuration.
 */

import java.util.Random;
import java.util.Scanner;

public class GearBallRandomizer {

    // number of move types available
    private static final int NUM_MOVE_TYPES = 24;

    // integer representation previous move
    private static int previousMove;

    private static int numTimesSameColRotated = 0;

    public GearBallRandomizer() {
    }

    /**
     * Put the gear ball into a random configuration
     */
    public void randomize(GearBall gearBall) {

        // Will not further randomize already randomized GearBall
        if (gearBall.isSolved()) {
            int numMoves;
            String input;

            // new RNG using system time as a seed
            Random random = new Random(System.currentTimeMillis());

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter how many rotations to make: ");
            input = scanner.nextLine();

            while (!validInteger(input)) {
                System.out.println("Please enter a positive integer: ");
                input = scanner.nextLine();
            }

            numMoves = Integer.parseInt(input);

            // tell user how many random moves are being made
            System.out.println("Making " + numMoves + " random rotations...\n");

            // make a number of moves on GearBall equal to moves
            for (int i = 0; i < numMoves; i++) {
                int move = random.nextInt(NUM_MOVE_TYPES);

                // if this is not the first move...
                if (i != 0) {
                    // then check to make sure the move is not an inverse of
                    // the previous move
                    while (isInverse(move) || moveMadeFiveTimes(move)) {
                        // change move if it is an inverse of previous move
                        move = random.nextInt(NUM_MOVE_TYPES);
                    }
                }

                // do the movement represented by move
                doMove(gearBall, move);

                // set previousMove to the move just made
                previousMove = move;

                // print the current GearBall configuration
                gearBall.printGearBall();
                System.out.println(); // for readability
            }
        } else {
            // User must reset GearBall before randomizing again
            System.out.println("Looks like your gear ball is already randomized!");
            System.out.println("Please reset your gear ball first if you want to");
            System.out.println("randomize it again.\n");
        }
    }

    /**
     * Make a series of moves and make corresponding inverse moves to verify program.
     * @param gearBall The GearBall instance.
     */
    public void verifyMoves(GearBall gearBall) {
        // mixing gearBall
        doMove(gearBall, 0);
        gearBall.printGearBall();
        doMove(gearBall, 12);
        gearBall.printGearBall();
        doMove(gearBall, 11);
        gearBall.printGearBall();
        doMove(gearBall, 16);
        gearBall.printGearBall();
        doMove(gearBall, 4);

        System.out.println("\nAfter mixing gear ball: \n");
        gearBall.printGearBall();
        System.out.println();

        // undoing sequence
        doMove(gearBall, 5);
        gearBall.printGearBall();
        doMove(gearBall, 17);
        gearBall.printGearBall();
        doMove(gearBall, 8);
        gearBall.printGearBall();
        doMove(gearBall, 13);
        gearBall.printGearBall();
        doMove(gearBall, 1);
        System.out.println("\nAfter undoing the initial sequence: \n");
        gearBall.printGearBall();
    }

    /**
     * Checks if the same move has been made 5 times.
     * @param move The current move to check against previous move.
     * @return true if move has been made 5 times, false otherwise
     */
    private static boolean moveMadeFiveTimes(int move) {
        String[] currMoveParams = GearBall.MOVES.get(move);
        String[] prevMoveParams = GearBall.MOVES.get(previousMove);

        String currHeld = currMoveParams[0];
        String currRotated = currMoveParams[1];
        String currDirection = currMoveParams[2];

        String prevHeld = prevMoveParams[0];
        String prevRotated = prevMoveParams[1];
        String prevDirection = prevMoveParams[2];

        if (currHeld.equals("middle") && prevHeld.equals("middle")) {
            if (currRotated.equals(prevRotated) && currDirection.equals(prevDirection))
                numTimesSameColRotated++;
        } else if (currHeld.equals(prevHeld) && currDirection.equals(prevDirection)) {
            numTimesSameColRotated++;
        } else {
            numTimesSameColRotated = 0;
        }

        if (numTimesSameColRotated > 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates user input for number of moves to make
     * @param input user input string
     * @return true if input is an integer greater than 0, false otherwise
     */
    private static boolean validInteger(String input) {
        int test;
        try {
            test = Integer.parseInt(input);
            if (test > 0)
                return true;
        } catch (NumberFormatException e) {
            System.out.println("Input is not an integer.");
            return false;
        }
        return false;
    }

    /**
     * Performs move on gearBall, prints string saying what move does first.
     * @param gearBall The GearBall instance.
     * @param move The integer representing the move.
     */
    private static void doMove(GearBall gearBall, int move) {
        if (move < 0 || move > 23) {
            System.err.println("Encountered error - Invalid move number: " + move);
            System.exit(1);
        }

        // get parameters for move number to print in human readable form
        String[] moveParameters = GearBall.MOVES.get(move);

        // print what the move is supposed to do
        System.out.println("Holding the " + moveParameters[0] + ", rotating the " + moveParameters[1]
                            +  " " + moveParameters[2] + "...");

        // do rotation
        move = gearBall.getSimplifiedMoveNum(move);
        gearBall.rotate(move);
    }

    /**
     * Checks to see if this move undoes the previous move
     * @param move The integer representing the move number.
     * @return true if move will undo previousMove, false if not
     */
    private static boolean isInverse(int move) {
        switch (previousMove) {
            case 0:
                if (move == 1 || move == 3)
                    return true;
                break;
            case 1:
                if (move == 0 || move == 2)
                    return true;
                break;
            case 2:
                if (move == 1 || move == 3)
                    return true;
                break;
            case 3:
                if (move == 0 || move == 2)
                    return true;
                break;
            case 4:
                if (move == 5 || move == 6)
                    return true;
                break;
            case 5:
                if (move == 4 || move == 7)
                    return true;
                break;
            case 6:
                if (move == 4 || move == 7)
                    return true;
                break;
            case 7:
                if (move == 5 || move == 6)
                    return true;
                break;
            case 8:
                if (move == 9 || move == 11)
                    return true;
                break;
            case 9:
                if (move == 8 || move == 10)
                    return true;
                break;
            case 10:
                if (move == 9 || move == 11)
                    return true;
                break;
            case 11:
                if (move == 8 || move == 10)
                    return true;
                break;
            case 12:
                if (move == 13 || move == 15)
                    return true;
                break;
            case 13:
                if (move == 12 || move == 14)
                    return true;
                break;
            case 14:
                if (move == 13 || move == 15)
                    return true;
                break;
            case 15:
                if (move == 12 || move == 14)
                    return true;
                break;
            case 16:
                if (move == 17 || move == 18)
                    return true;
                break;
            case 17:
                if (move == 16 || move == 19)
                    return true;
                break;
            case 18:
                if (move == 16 || move == 19)
                    return true;
                break;
            case 19:
                if (move == 17 || move == 18)
                    return true;
                break;
            case 20:
                if (move == 21 || move == 23)
                    return true;
                break;
            case 21:
                if (move == 20 || move == 22)
                    return true;
                break;
            case 22:
                if (move == 21 || move == 23)
                    return true;
                break;
            case 23:
                if (move == 20 || move == 22)
                    return true;
                break;
            default:
                System.err.println("Encountered error - Invalid int for move type: " + move);
                System.exit(1);
        }

        return false;
    }
}
