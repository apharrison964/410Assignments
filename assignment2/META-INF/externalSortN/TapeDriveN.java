package externalSortN;

import java.util.Random;

/**
 * Simulates a tape drive
 */
public class TapeDriveN {

    private int[] tape;
    private int currentPos = 0;

    public TapeDriveN(int capacity) {
        tape = new int[capacity];
    }

    public void write(int i) {
        tape[currentPos] = i;
        currentPos = (currentPos + 1) % tape.length;
    }

    public int read() {
        int i = tape[currentPos];
        currentPos = (currentPos + 1) % tape.length;
        return i;
    }

    public void reset() {
        currentPos = 0;
    }

    /**
     * Creates a new TapeDrive that can hold `capacity` numbers, fills it with random numbers,
     * and returns it. The numbers are random in the full integer range.
     */
    public static TapeDriveN generateRandomTape(int capacity) {
        TapeDriveN temp = new TapeDriveN(capacity);
        Random generator = new Random();
        int counter = 0;
        while (counter < capacity) {
        	temp.write(generator.nextInt());
        	counter++;
        }
        return temp;
    }
}