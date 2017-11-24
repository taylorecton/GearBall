/**
 * Author           Taylor Ecton
 * File Name        GearBallFace.java
 * Date Modified    2017-09-23
 * Purpose          Implements a class that is representative of a single face of a gear ball
 */

public class GearBallFace {
    // which face is this? (e.g. top, bottom, etc)
    private String name;

    // 2D array to hold the state of the face
    private char faceArray[][];

    // array of integers representing the state of each of
    // the four gears on this face
    // e.g. gearState[0] is the state of gear 0
    private int[] gearState;

    // the dimensions of the 2D array will be SIZE x SIZE
    protected static final int SIZE = 9;

    private static final int CENTER = 4;

    // Coordinates for each gear on the face
    // dimensions 4 x 4 x 2
    // GEARCOORDINATES[0][j][k] is the j-th pair of coordinates for
    // gear 0
    protected static final int[][][] GEARCOORDINATES =
            new int[][][]{
                    {
                            {0, 3}, {0, 4}, {0, 5}, {1, 4}
                    },
                    {
                            {3, 8}, {4, 8}, {5, 8}, {4, 7}
                    },
                    {
                            {8, 5}, {8, 4}, {8, 3}, {7, 4}
                    },
                    {
                            {5, 0}, {4, 0}, {3, 0}, {4, 1}
                    }
            };

    /**
     * Constructor for GearBallFace.
     * @param name The name of the face (top, left, front, right, bottom, back)
     */
    public GearBallFace(String name) {
        // set the size of the 2D array
        this.faceArray = new char[SIZE][SIZE];
        // set the name of this face
        this.name = name;
        // set each gear to initial state
        this.gearState = new int[]{0, 0, 0, 0};

        // initialize the 2D array to be solid color
        this.setSolid();
    }

    /**
     * Copy constructor for GearBallFace
     */
    public GearBallFace(GearBallFace toCopy) {
        // set the size of the 2D array
        this.faceArray = new char[SIZE][SIZE];
        // set the name of this face
        this.name = toCopy.name;
        this.gearState = new int[4];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                this.faceArray[row][col] = toCopy.getColor(row, col);
            }
        }

        for (int i = 0; i < 4; i++) {
            this.gearState[i] = toCopy.getGearState(i);
        }
    }

    /**
     * Gets color at specified index.
     * @param row Row in array to look at.
     * @param column Column in array to look at.
     * @return The char representing the color at that index.
     */
    public char getColor(int row, int column) {
        return faceArray[row][column];
    }

    /**
     * Sets the color at the specified index.
     * @param row Row of space to set.
     * @param column Column of space to set.
     * @param color Color to set the space to.
     */
    public void setColor(int row, int column, char color) {
        faceArray[row][column] = color;
    }

    /**
     * Copies an array representing one row of the gear ball
     * @param start Index to start at. The face has three larger rows, each represented
     *              by three rows in the array. Start will be 0, 3, or 6.
     * @return An array representing one of the larger rows of the face.
     */
    public char[][] getRowValues(int start) {
        int rows = 3;
        int columns = 9;
        int currentRowInFaceArray = start;

        char[][] values = new char[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                values[row][column] = faceArray[currentRowInFaceArray][column];
            }
            currentRowInFaceArray++;
        }

        return values;
    }

    /**
     * Copies row values from the back. Has to be handled differently from other
     * rows because the representation flips the face
     * @param start Index of start of larger row (0, 3, 6)
     * @return
     */
    public char[][] getRowValuesBizarro(int start) {
        int rows = 3;
        int columns = 9;
        int currentRowInFaceArray = -1;
        int currentColumnInFaceArray = SIZE-1;

        char[][] values = new char[rows][columns];

        if (start == 0)
            currentRowInFaceArray = 8;
        else if (start == 3)
            currentRowInFaceArray = 5;
        else if (start == 6)
            currentRowInFaceArray = 2;
        else {
            System.err.println("Encountered error in getRowValuesBizarro - Invalid start: " + start);
            System.exit(1);
        }

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                values[row][column] = faceArray[currentRowInFaceArray][currentColumnInFaceArray];
                currentColumnInFaceArray--;
            }
            currentColumnInFaceArray = SIZE-1;
            currentRowInFaceArray--;
        }

        return values;
    }

    /**
     * Copies a 9x3 column of the faceArray.
     * @param start Index indicating start of larger column (0, 3, 6).
     * @return Returns an array containing this larger column of the faceArray.
     */
    public char[][] getColumnValues(int start) {
        int rows = 9;
        int columns = 3;
        int currentColumnInFaceArray = start;

        char[][] values = new char[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                values[row][column] = faceArray[row][currentColumnInFaceArray];
                currentColumnInFaceArray++;
            }
            currentColumnInFaceArray = start;
        }

        return values;
    }

    /**
     * Copies values from a given array into the 3x9 row specified by start.
     * @param values An array representing a 3x9 row.
     * @param start Starting index of row to copy into (0, 3, 6)
     */
    public void copyRowValues(char[][] values, int start) {
        int rows = 3;
        int columns = 9;
        int currentRowInFaceArray = start;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                faceArray[currentRowInFaceArray][column] = values[row][column];
            }
            currentRowInFaceArray++;
        }
    }

    /**
     * Copies values from a given array into the 3x9 row specified by start on
     * the back face. Back face has to be handled separately due to representation
     * flipping face.
     * @param values An array representing a 3x9 row.
     * @param start Starting index of row to copy into (0, 3, 6)
     */
    public void copyRowValuesBizarro(char[][] values, int start) {
        int rows = 3;
        int columns = 9;
        int currentRowInFaceArray = -1;
        int currentColumnInFaceArray = 0;

        if (start == 0)
            currentRowInFaceArray = 6;
        else if (start == 3)
            currentRowInFaceArray = 3;
        else if (start == 6)
            currentRowInFaceArray = 0;
        else {
            System.err.println("Encountered error in copyRowValuesBizarro - invalid start: " + start);
            System.exit(1);
        }

        for (int row = rows-1; row >= 0; row--) {
            for (int column = columns-1; column >= 0; column--) {
                faceArray[currentRowInFaceArray][currentColumnInFaceArray] = values[row][column];
                currentColumnInFaceArray++;
            }
            currentColumnInFaceArray = 0;
            currentRowInFaceArray++;
        }
    }

    /**
     * Copies a 3x9 column into this face at column specified by start.
     * @param values The 3x9 column to copy.
     * @param start The index of the column on this face to copy into.
     */
    public void copyColumnValues(char[][] values, int start) {
        int rows = 9;
        int columns = 3;
        int currentColumnInFaceArray = start;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                faceArray[row][currentColumnInFaceArray] = values[row][column];
                currentColumnInFaceArray++;
            }
            currentColumnInFaceArray = start;
        }
    }

    /**
     * Rotates a face by 180 degrees.
     */
    public void rotate180() {
        int oppRow = SIZE-1;
        int oppCol = SIZE-1;
        char[][] temp = new char[SIZE][SIZE];

        // create a temporary copy of faceArray
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                temp[row][column] = faceArray[row][column];
            }
        }

        // reassign values into the new indices
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                faceArray[row][column] = temp[oppRow][oppCol];
                oppCol--;
            }
            oppCol = SIZE-1;
            oppRow--;
        }

        // update the gear states accordingly
        int tempGearState = gearState[0];
        gearState[0] = gearState[2];
        gearState[2] = tempGearState;

        tempGearState = gearState[1];
        gearState[1] = gearState[3];
        gearState[3] = tempGearState;
    }

    /**
     * Rotates the face clockwise 90 degrees.
     */
    public void rotateCW90() {
        int oppRow = SIZE-1;
        int oppCol = 0;
        char[][] temp = new char[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                temp[row][column] = faceArray[row][column];
            }
        }

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                faceArray[row][column] = temp[oppRow][oppCol];
                oppRow--;
            }
            oppRow = SIZE-1;
            oppCol++;
        }

        int tempGearState = gearState[0];
        gearState[0] = gearState[3];
        gearState[3] = gearState[2];
        gearState[2] = gearState[1];
        gearState[1] = tempGearState;
    }

    /**
     * Rotates the face counter-clockwise 90 degrees.
     */
    public void rotateCCW90() {
        int oppRow = 0;
        int oppCol = SIZE-1;
        char[][] temp = new char[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                temp[row][column] = faceArray[row][column];
            }
        }

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                faceArray[row][column] = temp[oppRow][oppCol];
                oppRow++;
            }
            oppRow = 0;
            oppCol--;
        }

        int tempGearState = gearState[0];
        gearState[0] = gearState[1];
        gearState[1] = gearState[2];
        gearState[2] = gearState[3];
        gearState[3] = tempGearState;
    }

    /**
     * Rotates a single gear on a face clockwise
     * @param adjacentFace the face adjacent to this face on the side of the given gear
     * @param gearNumber number representing which gear on the face to update; each face
     *                   has a total of 4 gears; starting with the top and going clockwise
     *                   around the edges is 0, 1, 2, 3 (with the face oriented as it is
     *                   in the printed representation in the GUI)
     * @param rotationDirection 0 for clockwise, 1 for counterclockwise
     */
    public void rotateGear(GearBallFace adjacentFace, int gearNumber, int rotationDirection) {
        // current state is the state the gear is currently in
        // the gears have a total of 5 states
        int currentState = this.gearState[gearNumber];
        int nextState;
        int adjGearNumber;
        char[] gearColors;

        if (rotationDirection == 0) {
            if (currentState != 5)
                nextState = currentState + 1;
            else
                nextState = 0;
        } else {
            if (currentState != 0)
                nextState = currentState - 1;
            else
                nextState = 5;
        }

        adjGearNumber = getAdjacentGearNum(gearNumber, adjacentFace);
        gearColors = getGearColors(gearNumber, adjacentFace);

        gearToState(this, gearNumber, adjacentFace, adjGearNumber, gearColors, nextState);
        this.gearState[gearNumber] = nextState;
        adjacentFace.gearState[adjGearNumber] = nextState;
    }

    /**
     * Prints out a string representation of a single row.
     * @param row The row to print.
     */
    public void printRow(int row) {
        // print a space for readability
        System.out.print(' ');

        // print char representing color for each index
        for (int column = 0; column < SIZE; column++) {
            System.out.print(faceArray[row][column]);

            // adding some spaces to improve readability of representation
            if ((column + 1) % 3 == 0)
                System.out.print(' ');
        }
    }

    /**
     * @return The name of this face.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this face is all a solid color
     * @return true if face is all the same color, false otherwise
     */
    public boolean isAllSameColor() {
        // set color to check against to be first element
        char color = faceArray[0][0];

        // iterate through each element; return false if a color doesn't match
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (faceArray[row][column] != color)
                    return false;
            }
        }

        // return true if all colors match
        return true;
    }

    /**
     * Sets the face to be a solid color determined by its name
     */
    public void setSolid() {
        char color;

        color = setInitialColor();

        // set the 2D array to all be same color
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                faceArray[row][column] = color;
            }
        }
    }

    /**
     * Gets the gear colors of the gear number specified.
     * @param gearNumber The gear number on this face.
     * @param adjacentFace The adjacent face on the GearBall.
     * @return An array with 2 entries, 1 for each color of the gear.
     */
    protected char[] getGearColors(int gearNumber, GearBallFace adjacentFace) {
        int colorRow = 0;
        int colorCol = 0;
        int colorRowAdj = 0;
        int colorColAdj = 0;
        char[] colors = new char[2];

        // back has to be handled specially
        if (this.getName().equals("back") || adjacentFace.getName().equals("back")) {
            if (gearNumber == 1)
                gearNumber = 4;
            else if (gearNumber == 3)
                gearNumber = 5;
        }

        // gets indices of faceArray that will always have the right color
        // for the gear
        switch (gearNumber) {
            case 0:
                colorRow = 1;
                colorCol = 3;
                colorRowAdj = 7;
                colorColAdj = 3;
                break;
            case 1:
                colorRow = 3;
                colorCol = 7;
                colorRowAdj = 3;
                colorColAdj = 1;
                break;
            case 2:
                colorRow = 7;
                colorCol = 3;
                colorRowAdj = 1;
                colorColAdj = 3;
                break;
            case 3:
                colorRow = 3;
                colorCol = 1;
                colorRowAdj = 3;
                colorColAdj = 7;
                break;
            case 4:
                colorRow = 3;
                colorCol = 7;
                colorRowAdj = 3;
                colorColAdj = 7;
                break;
            case 5:
                colorRow = 3;
                colorCol = 1;
                colorRowAdj = 3;
                colorColAdj = 1;
                break;
            default:
                // execution should not reach here
                System.err.println("updateGearCW: Error - invalid gearNumber: " + gearNumber);
                System.exit(1);
        }

        colors[0] = this.getColor(colorRow, colorCol);
        colors[1] = adjacentFace.getColor(colorRowAdj, colorColAdj);

        return colors;
    }

    /**
     * Gets the gearNumber of the same gear on the adjacent face.
     * @param gearNumber The gearNumber on this face.
     * @param adjacentFace The face adjacent to this one on the side of this gear.
     * @return The gearNumber of this gear on the adjacent face.
     */
    protected int getAdjacentGearNum(int gearNumber, GearBallFace adjacentFace) {
        int adjGearNum;

        if (gearNumber == 0) {
            adjGearNum = 2;
        } else if (gearNumber == 1) {
            if (adjacentFace.getName().equals("back")
                    || this.getName().equals("back")) {
                adjGearNum = 1;
            } else {
                adjGearNum = 3;
            }
        } else if (gearNumber == 2) {
            adjGearNum = 0;
        } else {
            if (adjacentFace.getName().equals("back")
                    || this.getName().equals("back")) {
                adjGearNum = 3;
            } else {
                adjGearNum = 1;
            }
        }

        return adjGearNum;
    }

    /**
     * Updates the state of the gear.
     * There are 6 possible states.
     * @param caller This face.
     * @param callerGearNum The gearNumber on this face.
     * @param adjFace The adjacent face.
     * @param adjGearNumber The gearNumber on the adjacent face.
     * @param gearColors The colors of the gear.
     * @param nextState The state to move the gear into.
     */
    protected void gearToState(GearBallFace caller,
                               int callerGearNum,
                               GearBallFace adjFace,
                               int adjGearNumber,
                               char[] gearColors,
                               int nextState) {
        int[][] coordsGear1 = GEARCOORDINATES[callerGearNum];
        int[][] coordsGear2 = GEARCOORDINATES[adjGearNumber];

        switch (nextState) {
            case 0:
                for (int i = 0; i < 4; i++) {
                    caller.setColor(coordsGear1[i][0], coordsGear1[i][1], gearColors[0]);
                    adjFace.setColor(coordsGear2[i][0], coordsGear2[i][1], gearColors[1]);
                }
                break;
            case 1:
                caller.setColor(coordsGear1[0][0], coordsGear1[0][1], gearColors[1]);
                caller.setColor(coordsGear1[1][0], coordsGear1[1][1], gearColors[0]);
                caller.setColor(coordsGear1[2][0], coordsGear1[2][1], gearColors[0]);
                caller.setColor(coordsGear1[3][0], coordsGear1[3][1], gearColors[0]);
                adjFace.setColor(coordsGear2[0][0], coordsGear2[0][1], gearColors[0]);
                adjFace.setColor(coordsGear2[1][0], coordsGear2[1][1], gearColors[1]);
                adjFace.setColor(coordsGear2[2][0], coordsGear2[2][1], gearColors[1]);
                adjFace.setColor(coordsGear2[3][0], coordsGear2[3][1], gearColors[1]);
                break;
            case 2:
                caller.setColor(coordsGear1[0][0], coordsGear1[0][1], gearColors[1]);
                caller.setColor(coordsGear1[1][0], coordsGear1[1][1], gearColors[1]);
                caller.setColor(coordsGear1[2][0], coordsGear1[2][1], gearColors[0]);
                caller.setColor(coordsGear1[3][0], coordsGear1[3][1], gearColors[1]);
                adjFace.setColor(coordsGear2[0][0], coordsGear2[0][1], gearColors[0]);
                adjFace.setColor(coordsGear2[1][0], coordsGear2[1][1], gearColors[0]);
                adjFace.setColor(coordsGear2[2][0], coordsGear2[2][1], gearColors[1]);
                adjFace.setColor(coordsGear2[3][0], coordsGear2[3][1], gearColors[0]);
                break;
            case 3:
                for (int i = 0; i < 4; i++) {
                    this.setColor(coordsGear1[i][0], coordsGear1[i][1], gearColors[1]);
                    adjFace.setColor(coordsGear2[i][0], coordsGear2[i][1], gearColors[0]);
                }
                break;
            case 4:
                caller.setColor(coordsGear1[0][0], coordsGear1[0][1], gearColors[0]);
                caller.setColor(coordsGear1[1][0], coordsGear1[1][1], gearColors[1]);
                caller.setColor(coordsGear1[2][0], coordsGear1[2][1], gearColors[1]);
                caller.setColor(coordsGear1[3][0], coordsGear1[3][1], gearColors[1]);
                adjFace.setColor(coordsGear2[0][0], coordsGear2[0][1], gearColors[1]);
                adjFace.setColor(coordsGear2[1][0], coordsGear2[1][1], gearColors[0]);
                adjFace.setColor(coordsGear2[2][0], coordsGear2[2][1], gearColors[0]);
                adjFace.setColor(coordsGear2[3][0], coordsGear2[3][1], gearColors[0]);
                break;
            case 5:
                caller.setColor(coordsGear1[0][0], coordsGear1[0][1], gearColors[0]);
                caller.setColor(coordsGear1[1][0], coordsGear1[1][1], gearColors[0]);
                caller.setColor(coordsGear1[2][0], coordsGear1[2][1], gearColors[1]);
                caller.setColor(coordsGear1[3][0], coordsGear1[3][1], gearColors[0]);
                adjFace.setColor(coordsGear2[0][0], coordsGear2[0][1], gearColors[1]);
                adjFace.setColor(coordsGear2[1][0], coordsGear2[1][1], gearColors[1]);
                adjFace.setColor(coordsGear2[2][0], coordsGear2[2][1], gearColors[0]);
                adjFace.setColor(coordsGear2[3][0], coordsGear2[3][1], gearColors[1]);
                break;
            default:
                System.err.println("gearToState: Error - invalid state: " + nextState);
        }
    }

    protected void setGearState(int gearNumber, int newState) {
        gearState[gearNumber] = newState;
    }

    protected int getGearState(int gearNumber) {
        return gearState[gearNumber];
    }

    public int getNumSquaresOutOfPlace() {
        int num = 0;
        char correctColor = faceArray[CENTER][CENTER];

        for (int row = 2; row <= 6; row += 2) {
            for (int col = 2; col <= 6; col +=2) {
                if (faceArray[row][col] != correctColor)
                    num++;
            }
        }

        return num;
    }

    /**
     * Sets the initial color of the face.
     * @return Char representation of the face color.
     */
    private char setInitialColor() {
        char color;

        // get initial color of face based on face name
        switch (name) {
            case "top":
                color = 'G';
                break;
            case "bottom":
                color = 'B';
                break;
            case "left":
                color = 'P';
                break;
            case "right":
                color = 'R';
                break;
            case "front":
                color = 'Y';
                break;
            case "back":
                color = 'O';
                break;
            default:
                color = 'X';
                System.err.println("Encountered error - Invalid face name: " + name);
                System.exit(1);
        }

        return color;
    }
}
