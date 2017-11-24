/**
 * Author           Taylor Ecton
 * File Name        GearBall.java
 * Date Modified    2017-09-23
 * Purpose          Implements a class that is representative of the gear ball as a whole.
 */

import java.util.HashMap;

public class GearBall {

    // the six faces of the gear ball
    private GearBallFace top;
    private GearBallFace bottom;
    private GearBallFace left;
    private GearBallFace right;
    private GearBallFace front;
    private GearBallFace back;

    // an array to hold the gear ball faces and one with their names pre-defined as strings
    private GearBallFace[] faces;
    private final String[] NAMES = {"top", "bottom", "left", "right", "front", "back"};

    // integers representing the start of rows/columns
    // used for rotation functions
    private final int TOP_START = 0;
    private final int LEFT_START = 0;
    private final int MID_START = 3;
    private final int BOT_START = 6;
    private final int RIGHT_START = 6;

    // create a HashMap of all possible movements
    // six different rows/columns can be held, each of which leaves two other columns to
    // rotate for twelve movement types, each of which has two possible directions
    // bringing the total to 24
    protected static final HashMap<Integer, String[]> MOVES = new HashMap<Integer, String[]>() {
        {
            put(0, new String[]{"top", "middle", "left"});
            put(1, new String[]{"top", "middle", "right"});
            put(2, new String[]{"top", "bottom", "left"});
            put(3, new String[]{"top", "bottom", "right"});
            put(4, new String[]{"middle", "top", "left"});
            put(5, new String[]{"middle", "top", "right"});
            put(6, new String[]{"middle", "bottom", "left"});
            put(7, new String[]{"middle", "bottom", "right"});
            put(8, new String[]{"bottom", "top", "left"});
            put(9, new String[]{"bottom", "top", "right"});
            put(10, new String[]{"bottom", "middle", "left"});
            put(11, new String[]{"bottom", "middle", "right"});
            put(12, new String[]{"left", "middle", "up"});
            put(13, new String[]{"left", "middle", "down"});
            put(14, new String[]{"left", "right", "up"});
            put(15, new String[]{"left", "right", "down"});
            put(16, new String[]{"middle", "left", "up"});
            put(17, new String[]{"middle", "left", "down"});
            put(18, new String[]{"middle", "right", "up"});
            put(19, new String[]{"middle", "right", "down"});
            put(20, new String[]{"right", "left", "up"});
            put(21, new String[]{"right", "left", "down"});
            put(22, new String[]{"right", "middle", "up"});
            put(23, new String[]{"right", "middle", "down"});
        }
    };

    // this array maps the high-level move information above to a simplified move number;
    // the moves in the above HashMap can be simplified into half as many moves that have the
    // same effect
    private static final int[] SIMPLIFIED_MOVE_NUM = {
        0,
        1,
        0,
        1,
        2,
        3,
        3,
        2,
        4,
        5,
        4,
        5,
        6,
        7,
        6,
        7,
        8,
        9,
        9,
        8,
        10,
        11,
        10,
        11
    };

    /**
     * Default constructor for the gear ball
     */
    public GearBall () {
        // initializes all GearBallFaces
        this.top = new GearBallFace(NAMES[0]);
        this.bottom = new GearBallFace(NAMES[1]);

        this.left = new GearBallFace(NAMES[2]);
        this.right = new GearBallFace(NAMES[3]);

        this.front = new GearBallFace(NAMES[4]);
        this.back = new GearBallFace(NAMES[5]);

        this.faces = new GearBallFace[]{top, bottom, left, right, front, back};
    }

    /**
     * Copy constructor for GearBall
     */
    public GearBall(GearBall toCopy) {
        GearBallFace[] facesToCopy = toCopy.getFaces();

        this.top = new GearBallFace(facesToCopy[0]);
        this.bottom = new GearBallFace(facesToCopy[1]);

        this.left = new GearBallFace(facesToCopy[2]);
        this.right = new GearBallFace(facesToCopy[3]);

        this.front = new GearBallFace(facesToCopy[4]);
        this.back = new GearBallFace(facesToCopy[5]);

        this.faces = new GearBallFace[]{top, bottom, left, right, front, back};
    }

    public GearBallFace[] getFaces() {
        return faces;
    }

    public String toString() {
        String gearBallString = "";

        for (GearBallFace face : faces) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    gearBallString += face.getColor(row, col);
                }
            }
        }

        return gearBallString;
    }

    /**
     * Prints a representation of GearBall like this:
     *         _
     *       _|T|_
     *      |L|F|R|
     *        |B|
     *        |b|
     *
     * where T = top, L = left, R = right, B = bottom,
     * and b = back
     */
    public void printGearBall() {
        // print top
        printFaceWithOffset(top);

        // print left --> front --> right
        printLFR();

        // print bottom
        printFaceWithOffset(bottom);

        // print back
        printFaceWithOffset(back);
    }

    /**
     * Checks to see if the gear ball is solved.
     * @return true if solved, false otherwise
     */
    public boolean isSolved() {
        // check if each face is all the same color
        for (GearBallFace face : faces)
            if (!face.isAllSameColor()) return false;

        return true;
    }

    /**
     * GearBall is reset to solved starting configuration
     */
    public void reset() {
        for (GearBallFace face : faces)
            face.setSolid();
    }

    public int getSimplifiedMoveNum(int number) {
        return SIMPLIFIED_MOVE_NUM[number];
    }

    /**
     * Maps the move number provided to the appropriate function
     * @param moveNum A move number whose high-level description can be found
     *                in the MOVES HashMap
     */
    public void rotate(int moveNum) {
        // uses the simplified move number of moveNum to map to appropriate function
        switch (moveNum) {
            case 0:
                topStaticLeft();
                break;
            case 1:
                topStaticRight();
                break;
            case 2:
                middleStaticTLeftBRight();
                break;
            case 3:
                middleStaticTRightBLeft();
                break;
            case 4:
                bottomStaticLeft();
                break;
            case 5:
                bottomStaticRight();
                break;
            case 6:
                leftStaticUp();
                break;
            case 7:
                leftStaticDown();
                break;
            case 8:
                middleStaticLUpRDown();
                break;
            case 9:
                middleStaticLDownRUp();
                break;
            case 10:
                rightStaticUp();
                break;
            case 11:
                rightStaticDown();
                break;
            default:
                // the function was somehow passed an invalid parameter
                System.err.println("GearBall.rotate(): error - Invalid simplified move number: "
                                    + SIMPLIFIED_MOVE_NUM[moveNum]);
                System.exit(1);
        }
    }

    public int getNumGearsNotInStateZero() {
        int gearsNotInStateZero = 0;

        int[] gearStates = new int[12];
        gearStates[0] = top.getGearState(0);
        gearStates[1] = top.getGearState(3);
        gearStates[2] = top.getGearState(1);
        gearStates[3] = top.getGearState(2);
        gearStates[4] = left.getGearState(3);
        gearStates[5] = left.getGearState(1);
        gearStates[6] = right.getGearState(3);
        gearStates[7] = right.getGearState(1);
        gearStates[8] = left.getGearState(2);
        gearStates[9] = front.getGearState(2);
        gearStates[10] = right.getGearState(2);
        gearStates[11] = bottom.getGearState(2);

        for (int i = 0; i < gearStates.length; i++) {
            if (gearStates[i] != 0)
                gearsNotInStateZero++;
        }

        return gearsNotInStateZero;
    }

    /**
     * Performs a rotation holding the top static and rotating
     * the bottom towards the left face (counter-clockwise).
     */
    private void topStaticLeft() {
        // temp used to copy row/column values
        char[][] temp;

        // tempGearState used to move gearStates over
        int tempGearState;

        // rotate the four gears rotated by this movement
        front.rotateGear(left, 3, 0);
        right.rotateGear(front, 3, 0);
        back.rotateGear(right, 1, 0);
        left.rotateGear(back, 3, 0);

        // copy values to new positions
        temp = front.getRowValues(MID_START);
        front.copyRowValues(right.getRowValues(MID_START), MID_START);
        right.copyRowValues(back.getRowValuesBizarro(MID_START), MID_START);
        back.copyRowValuesBizarro(left.getRowValues(MID_START), MID_START);
        left.copyRowValues(temp, MID_START);

        temp = front.getRowValues(BOT_START);
        front.copyRowValues(back.getRowValuesBizarro(BOT_START), BOT_START);
        back.copyRowValuesBizarro(temp, BOT_START);

        temp = left.getRowValues(BOT_START);
        left.copyRowValues(right.getRowValues(BOT_START), BOT_START);
        right.copyRowValues(temp, BOT_START);

        // rotate the bottom face 180 degrees
        bottom.rotate180();
        tempGearState = front.getGearState(2);
        front.setGearState(2, back.getGearState(0));
        back.setGearState(0, tempGearState);

        tempGearState = left.getGearState(2);
        left.setGearState(2, right.getGearState(2));
        right.setGearState(2, tempGearState);
    }

    /**
     * Performs a rotation holding the top static and rotating
     * the bottom towards the right face (clockwise).
     */
    private void topStaticRight() {
        // temp holds values to copy
        char[][] temp;
        int tempGearState;

        // rotate the gears rotated by this rotation
        front.rotateGear(left, 3, 1);
        right.rotateGear(front, 3, 1);
        back.rotateGear(right, 1, 1);
        left.rotateGear(back, 3, 1);

        // copy values to new positions
        temp = front.getRowValues(MID_START);
        front.copyRowValues(left.getRowValues(MID_START), MID_START);
        left.copyRowValues(back.getRowValuesBizarro(MID_START), MID_START);
        back.copyRowValuesBizarro(right.getRowValues(MID_START), MID_START);
        right.copyRowValues(temp, MID_START);

        temp = front.getRowValues(BOT_START);
        front.copyRowValues(back.getRowValuesBizarro(BOT_START), BOT_START);
        back.copyRowValuesBizarro(temp, BOT_START);

        temp = left.getRowValues(BOT_START);
        left.copyRowValues(right.getRowValues(BOT_START), BOT_START);
        right.copyRowValues(temp, BOT_START);

        // rotate the bottom 180 degrees
        bottom.rotate180();
        tempGearState = front.getGearState(2);
        front.setGearState(2, back.getGearState(0));
        back.setGearState(0, tempGearState);

        tempGearState = left.getGearState(2);
        left.setGearState(2, right.getGearState(2));
        right.setGearState(2, tempGearState);
    }

    /**
     * Performs a rotation holding the middle static, and rotating the
     * top towards the left face (CW) or the bottom towards the right (CW)
     */
    private void middleStaticTLeftBRight() {
        // temp holds copied values for swapping
        char[][] temp;
        int tempGearState;

        // rotate the affected gears
        front.rotateGear(left, 3, 1);
        right.rotateGear(front, 3, 1);
        back.rotateGear(right, 1, 1);
        left.rotateGear(back, 3, 1);

        // update the rows
        temp = front.getRowValues(TOP_START);
        front.copyRowValues(right.getRowValues(TOP_START), TOP_START);
        right.copyRowValues(back.getRowValuesBizarro(TOP_START), TOP_START);
        back.copyRowValuesBizarro(left.getRowValues(TOP_START), TOP_START);
        left.copyRowValues(temp, TOP_START);

        temp = front.getRowValues(BOT_START);
        front.copyRowValues(left.getRowValues(BOT_START), BOT_START);
        left.copyRowValues(back.getRowValuesBizarro(BOT_START), BOT_START);
        back.copyRowValuesBizarro(right.getRowValues(BOT_START), BOT_START);
        right.copyRowValues(temp, BOT_START);

        // top and bottom both rotate 90 degress CW
        top.rotateCW90();
        tempGearState = front.getGearState(0);
        front.setGearState(0, right.getGearState(0));
        right.setGearState(0, back.getGearState(2));
        back.setGearState(2, left.getGearState(0));
        left.setGearState(0, tempGearState);

        bottom.rotateCW90();
        tempGearState = front.getGearState(2);
        front.setGearState(2, left.getGearState(2));
        left.setGearState(2, back.getGearState(0));
        back.setGearState(0, right.getGearState(2));
        right.setGearState(2, tempGearState);

    }

    /**
     * Performs a rotation holding the middle static and rotating the top
     * towards the right face or bottom towards the left face (CCW).
     */
    private void middleStaticTRightBLeft() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(left, 3, 0);
        right.rotateGear(front, 3, 0);
        back.rotateGear(right, 1, 0);
        left.rotateGear(back, 3, 0);

        temp = front.getRowValues(TOP_START);
        front.copyRowValues(left.getRowValues(TOP_START), TOP_START);
        left.copyRowValues(back.getRowValuesBizarro(TOP_START), TOP_START);
        back.copyRowValuesBizarro(right.getRowValues(TOP_START), TOP_START);
        right.copyRowValues(temp, TOP_START);

        temp = front.getRowValues(BOT_START);
        front.copyRowValues(right.getRowValues(BOT_START), BOT_START);
        right.copyRowValues(back.getRowValuesBizarro(BOT_START), BOT_START);
        back.copyRowValuesBizarro(left.getRowValues(BOT_START), BOT_START);
        left.copyRowValues(temp, BOT_START);

        top.rotateCCW90();
        tempGearState = front.getGearState(0);
        front.setGearState(0, left.getGearState(0));
        left.setGearState(0, back.getGearState(2));
        back.setGearState(2, right.getGearState(0));
        right.setGearState(0, tempGearState);

        bottom.rotateCCW90();
        tempGearState = front.getGearState(2);
        front.setGearState(2, right.getGearState(2));
        right.setGearState(2, back.getGearState(0));
        back.setGearState(0, left.getGearState(2));
        left.setGearState(2, tempGearState);
    }

    /**
     * Performs a rotation holding the bottom static and rotating the top portion
     * towards the left face (CW).
     */
    private void bottomStaticLeft() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(left, 3, 1);
        right.rotateGear(front, 3, 1);
        back.rotateGear(right, 1, 1);
        left.rotateGear(back, 3, 1);

        temp = front.getRowValues(MID_START);
        front.copyRowValues(right.getRowValues(MID_START), MID_START);
        right.copyRowValues(back.getRowValuesBizarro(MID_START), MID_START);
        back.copyRowValuesBizarro(left.getRowValues(MID_START), MID_START);
        left.copyRowValues(temp, MID_START);

        temp = front.getRowValues(TOP_START);
        front.copyRowValues(back.getRowValuesBizarro(TOP_START), TOP_START);
        back.copyRowValuesBizarro(temp, TOP_START);

        temp = left.getRowValues(TOP_START);
        left.copyRowValues(right.getRowValues(TOP_START), TOP_START);
        right.copyRowValues(temp, TOP_START);

        top.rotate180();
        tempGearState = front.getGearState(0);
        front.setGearState(0, back.getGearState(2));
        back.setGearState(2, tempGearState);

        tempGearState = left.getGearState(0);
        left.setGearState(0, right.getGearState(0));
        right.setGearState(0, tempGearState);
    }

    /**
     * Performs a rotation holding the bottom static and rotating the top portion
     * towards the right face (CCW).
     */
    private void bottomStaticRight() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(left, 3, 0);
        right.rotateGear(front, 3, 0);
        back.rotateGear(right, 1, 0);
        left.rotateGear(back, 3, 0);

        temp = front.getRowValues(MID_START);
        front.copyRowValues(left.getRowValues(MID_START), MID_START);
        left.copyRowValues(back.getRowValuesBizarro(MID_START), MID_START);
        back.copyRowValuesBizarro(right.getRowValues(MID_START), MID_START);
        right.copyRowValues(temp, MID_START);

        temp = front.getRowValues(TOP_START);
        front.copyRowValues(back.getRowValuesBizarro(TOP_START), TOP_START);
        back.copyRowValuesBizarro(temp, TOP_START);

        temp = left.getRowValues(TOP_START);
        left.copyRowValues(right.getRowValues(TOP_START), TOP_START);
        right.copyRowValues(temp, TOP_START);

        top.rotate180();
        tempGearState = front.getGearState(0);
        front.setGearState(0, back.getGearState(2));
        back.setGearState(2, tempGearState);

        tempGearState = left.getGearState(0);
        left.setGearState(0, right.getGearState(0));
        right.setGearState(0, tempGearState);
    }

    /**
     * Performs a rotation holding the left side static, and rotating the
     * right portion up (CW).
     */
    private void leftStaticUp() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(top, 0, 1);
        top.rotateGear(back, 0, 1);
        back.rotateGear(bottom, 0, 1);
        bottom.rotateGear(front, 0, 1);

        temp = front.getColumnValues(MID_START);
        front.copyColumnValues(bottom.getColumnValues(MID_START), MID_START);
        bottom.copyColumnValues(back.getColumnValues(MID_START), MID_START);
        back.copyColumnValues(top.getColumnValues(MID_START), MID_START);
        top.copyColumnValues(temp, MID_START);

        temp = front.getColumnValues(RIGHT_START);
        front.copyColumnValues(back.getColumnValues(RIGHT_START), RIGHT_START);
        back.copyColumnValues(temp, RIGHT_START);

        temp = top.getColumnValues(RIGHT_START);
        top.copyColumnValues(bottom.getColumnValues(RIGHT_START), RIGHT_START);
        bottom.copyColumnValues(temp, RIGHT_START);

        right.rotate180();
        tempGearState = front.getGearState(1);
        front.setGearState(1, back.getGearState(1));
        back.setGearState(1, tempGearState);
        tempGearState = top.getGearState(1);
        top.setGearState(1, bottom.getGearState(1));
        bottom.setGearState(1, tempGearState);
    }

    /**
     * Performs a rotation holding the left side static, and rotating the
     * right portion down (CCW).
     */
    private void leftStaticDown() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(top, 0, 0);
        top.rotateGear(back, 0, 0);
        back.rotateGear(bottom, 0, 0);
        bottom.rotateGear(front, 0, 0);

        temp = front.getColumnValues(MID_START);
        front.copyColumnValues(top.getColumnValues(MID_START), MID_START);
        top.copyColumnValues(back.getColumnValues(MID_START), MID_START);
        back.copyColumnValues(bottom.getColumnValues(MID_START), MID_START);
        bottom.copyColumnValues(temp, MID_START);

        temp = front.getColumnValues(RIGHT_START);
        front.copyColumnValues(back.getColumnValues(RIGHT_START), RIGHT_START);
        back.copyColumnValues(temp, RIGHT_START);

        temp = top.getColumnValues(RIGHT_START);
        top.copyColumnValues(bottom.getColumnValues(RIGHT_START), RIGHT_START);
        bottom.copyColumnValues(temp, RIGHT_START);

        right.rotate180();
        tempGearState = front.getGearState(1);
        front.setGearState(1, back.getGearState(1));
        back.setGearState(1, tempGearState);
        tempGearState = top.getGearState(1);
        top.setGearState(1, bottom.getGearState(1));
        bottom.setGearState(1, tempGearState);
    }

    /**
     * Performs a rotation holding the middle static and rotating the left
     * up or the right down (CCW).
     */
    private void middleStaticLUpRDown() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(top, 0, 0);
        top.rotateGear(back, 0, 0);
        back.rotateGear(bottom, 0, 0);
        bottom.rotateGear(front, 0, 0);

        temp = front.getColumnValues(LEFT_START);
        front.copyColumnValues(bottom.getColumnValues(LEFT_START), LEFT_START);
        bottom.copyColumnValues(back.getColumnValues(LEFT_START), LEFT_START);
        back.copyColumnValues(top.getColumnValues(LEFT_START), LEFT_START);
        top.copyColumnValues(temp, LEFT_START);

        temp = front.getColumnValues(RIGHT_START);
        front.copyColumnValues(top.getColumnValues(RIGHT_START), RIGHT_START);
        top.copyColumnValues(back.getColumnValues(RIGHT_START), RIGHT_START);
        back.copyColumnValues(bottom.getColumnValues(RIGHT_START), RIGHT_START);
        bottom.copyColumnValues(temp, RIGHT_START);

        left.rotateCCW90();
        tempGearState = front.getGearState(3);
        front.setGearState(3, bottom.getGearState(3));
        bottom.setGearState(3, back.getGearState(3));
        back.setGearState(3, top.getGearState(3));
        top.setGearState(3, tempGearState);

        right.rotateCCW90();
        tempGearState = front.getGearState(1);
        front.setGearState(1, top.getGearState(1));
        top.setGearState(1, back.getGearState(1));
        back.setGearState(1, bottom.getGearState(1));
        bottom.setGearState(1, tempGearState);
    }

    /**
     * Performs a rotation holding the middle static and rotating the left
     * down or the right up (CW).
     */
    private void middleStaticLDownRUp() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(top, 0, 1);
        top.rotateGear(back, 0, 1);
        back.rotateGear(bottom, 0, 1);
        bottom.rotateGear(front, 0, 1);

        temp = front.getColumnValues(RIGHT_START);
        front.copyColumnValues(bottom.getColumnValues(RIGHT_START), RIGHT_START);
        bottom.copyColumnValues(back.getColumnValues(RIGHT_START), RIGHT_START);
        back.copyColumnValues(top.getColumnValues(RIGHT_START), RIGHT_START);
        top.copyColumnValues(temp, RIGHT_START);

        temp = front.getColumnValues(LEFT_START);
        front.copyColumnValues(top.getColumnValues(LEFT_START), LEFT_START);
        top.copyColumnValues(back.getColumnValues(LEFT_START), LEFT_START);
        back.copyColumnValues(bottom.getColumnValues(LEFT_START), LEFT_START);
        bottom.copyColumnValues(temp, LEFT_START);

        left.rotateCW90();
        tempGearState = front.getGearState(3);
        front.setGearState(3, top.getGearState(3));
        top.setGearState(3, back.getGearState(3));
        back.setGearState(3, bottom.getGearState(3));
        bottom.setGearState(3, tempGearState);

        right.rotateCW90();
        tempGearState = front.getGearState(1);
        front.setGearState(1, bottom.getGearState(1));
        bottom.setGearState(1, back.getGearState(1));
        back.setGearState(1, top.getGearState(1));
        top.setGearState(1, tempGearState);
    }

    /**
     * Performs a rotation holding the right side static, and rotating the
     * left portion up (CCW).
     */
    private void rightStaticUp() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(top, 0, 0);
        top.rotateGear(back, 0, 0);
        back.rotateGear(bottom, 0, 0);
        bottom.rotateGear(front, 0, 0);

        temp = front.getColumnValues(MID_START);
        front.copyColumnValues(bottom.getColumnValues(MID_START), MID_START);
        bottom.copyColumnValues(back.getColumnValues(MID_START), MID_START);
        back.copyColumnValues(top.getColumnValues(MID_START), MID_START);
        top.copyColumnValues(temp, MID_START);

        temp = front.getColumnValues(LEFT_START);
        front.copyColumnValues(back.getColumnValues(LEFT_START), LEFT_START);
        back.copyColumnValues(temp, LEFT_START);

        temp = top.getColumnValues(LEFT_START);
        top.copyColumnValues(bottom.getColumnValues(LEFT_START), LEFT_START);
        bottom.copyColumnValues(temp, LEFT_START);

        left.rotate180();
        tempGearState = front.getGearState(3);
        front.setGearState(3, back.getGearState(3));
        back.setGearState(3, tempGearState);
        tempGearState = top.getGearState(3);
        top.setGearState(3, bottom.getGearState(3));
        bottom.setGearState(3, tempGearState);
    }

    /**
     * Performs a rotation holding the right side static, and rotating the
     * left portion down (CW).
     */
    private void rightStaticDown() {
        char[][] temp;
        int tempGearState;

        front.rotateGear(top, 0, 1);
        top.rotateGear(back, 0, 1);
        back.rotateGear(bottom, 0, 1);
        bottom.rotateGear(front, 0, 1);

        temp = front.getColumnValues(MID_START);
        front.copyColumnValues(top.getColumnValues(MID_START), MID_START);
        top.copyColumnValues(back.getColumnValues(MID_START), MID_START);
        back.copyColumnValues(bottom.getColumnValues(MID_START), MID_START);
        bottom.copyColumnValues(temp, MID_START);

        temp = front.getColumnValues(LEFT_START);
        front.copyColumnValues(back.getColumnValues(LEFT_START), LEFT_START);
        back.copyColumnValues(temp, LEFT_START);

        temp = top.getColumnValues(LEFT_START);
        top.copyColumnValues(bottom.getColumnValues(LEFT_START), LEFT_START);
        bottom.copyColumnValues(temp, LEFT_START);

        left.rotate180();
        tempGearState = front.getGearState(3);
        front.setGearState(3, back.getGearState(3));
        back.setGearState(3, tempGearState);
        tempGearState = top.getGearState(3);
        top.setGearState(3, bottom.getGearState(3));
        bottom.setGearState(3, tempGearState);
    }

    /**
     * Used to print the top, bottom and back in the GUI representation. The face
     * is offset to account for the middle row being longer.
     * @param face The face being printed
     */
    private void printFaceWithOffset(GearBallFace face) {
        if (face == bottom) {
            // prints border above bottom face
            printLongBorder();
        } else {
            // prints border above top/back face
            printOffsetBorder();
        }

        // prints indicated face offset by spaces
        for (int row = 0; row < GearBallFace.SIZE; row++) {
            // print blank spaces to offset face
            // SIZE + 2 accounts for left border ('*') of offset
            // and extra readability space in row
            for (int column = 0; column < GearBallFace.SIZE + 2; column++) {
                System.out.print(' ');

                // account for extra readability spaces
                if ((column + 1) % 3 == 0) {
                    System.out.print(' ');
                }
            }

            // print left border and current row of face
            System.out.print('*');
            face.printRow(row);

            // print right border and new line
            System.out.print("*\n");
        }

        if (face == back) {
            // print border above back face
            printOffsetBorder();
        }
    }

    /**
     * Prints the left, front, and right faces side by side for the GUI.
     */
    private void printLFR() {
        // print top border for left, front, right
        printLongBorder();

        // prints the left, front, and right faces in that order
        for (int row = 0; row < GearBallFace.SIZE; row++) {
            // print current row for left face, then front, then right
            System.out.print('*');
            left.printRow(row);
            System.out.print('*');
            front.printRow(row);
            System.out.print('*');
            right.printRow(row);

            // print border and new line
            System.out.print("*\n");
        }
    }

    /**
     * Prints the upper and lower border for the left, front, and right faces
     */
    private void printLongBorder() {
        // print border for top/bottom of left, front, and right (SIZE times 3)
        // with 4 additional '*'s for side borders (plus 4)
        // with 12 additional '*'s to account for extra spaces for readability (plus 12)
        // for total of (plus 16)
        int columns = (GearBallFace.SIZE * 3) + 16;

        for (int column = 0; column < columns; column++) {
            System.out.print('*');
        }

        System.out.print('\n');
    }

    /**
     * Prints an upper/lower border for the offset faces (top, bottom, back).
     */
    private void printOffsetBorder() {
        // print top/bottom border of face, offset by spaces
        // two spaces for left side border of long border and extra space in row
        System.out.print("  ");

        // spaces for SIZE '*'s of long border to offset
        for (int column = 0; column < GearBallFace.SIZE; column++) {
            System.out.print(' ');

            // print additional spaces for readability
            if ((column + 1) % 3 == 0)
                System.out.print(' ');
        }

        // left border for this border
        System.out.print('*');

        // SIZE '*'s for middle of border
        for (int column = 0; column < GearBallFace.SIZE; column++) {
            System.out.print('*');

            // account for additional space for readability
            if ((column + 1) % 3 == 0)
                System.out.print('*');
        }

        // right border and new line
        System.out.print("**\n");
    }
}
