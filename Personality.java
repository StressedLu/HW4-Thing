/*
 * CS312 Assignment 7 - Personality Quiz
 *
 * On my honor, <NAME>, this programming assignment is my own work,
 * I have not shared it with others and I will not share it in the future.
 *
 * A program to read a file with raw data from a Keirsey personality test
 * and print out the results.
 *
 * Name:
 * UTEID:
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Personality {

    // ADD CLASS CONSTANTS HERE
    public static final int NUM_DIMS = 4;
    public static final double NO_ANSWERS = -1.0;
    public static final double THRESHOLD = 0.5;
    public static final int GROUP_SIZE = 7;
    public static final String LEFT_LETTERS = "ESTJ";
    public static final String RIGHT_LETTERS = "INFP";
    // dimension index for each question's position within a group of 7
    public static final int[] GROUP_PATTERN = {0, 1, 1, 2, 2, 3, 3};

    // The main method to process the data from the personality tests
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Scanner fileScanner = getFileScanner(keyboard);

        int numPeople = Integer.parseInt(fileScanner.nextLine().trim());
        for (int i = 0; i < numPeople; i++) {
            String name = fileScanner.nextLine();
            String answers = fileScanner.nextLine();
            processPerson(name, answers);
        }

        fileScanner.close();
        keyboard.close();
    }

    // ADD METHODS HERE

    // compute and display the results for one person
    public static void processPerson(String name, String answers) {
        int[] aCounts = new int[NUM_DIMS];
        int[] bCounts = new int[NUM_DIMS];
        countAnswers(answers, aCounts, bCounts);
        double[] percentages = computePercentB(aCounts, bCounts);
        displayOutput(name, percentages, determineType(percentages));
    }

    // tally A and B answers per dimension; dashes are ignored
    public static void countAnswers(String answers, int[] aCounts, int[] bCounts) {
        for (int i = 0; i < answers.length(); i++) {
            int dim = GROUP_PATTERN[i % GROUP_SIZE];
            char answer = Character.toUpperCase(answers.charAt(i));
            if (answer == 'A') {
                aCounts[dim]++;
            } else if (answer == 'B') {
                bCounts[dim]++;
            }
        }
    }

    // fraction of B answers per dimension, or NO_ANSWERS if none were given
    public static double[] computePercentB(int[] aCounts, int[] bCounts) {
        double[] percentages = new double[NUM_DIMS];
        for (int i = 0; i < NUM_DIMS; i++) {
            int total = aCounts[i] + bCounts[i];
            if (total == 0) {
                percentages[i] = NO_ANSWERS;
            } else {
                percentages[i] = (double) bCounts[i] / total;
            }
        }
        return percentages;
    }

    // four letter type: X for a tie, - for no answers
    public static String determineType(double[] percentages) {
        String type = "";
        for (int i = 0; i < NUM_DIMS; i++) {
            if (percentages[i] == NO_ANSWERS) {
                type += "-";
            } else if (percentages[i] == THRESHOLD) {
                type += "X";
            } else if (percentages[i] > THRESHOLD) {
                type += RIGHT_LETTERS.charAt(i);
            } else {
                type += LEFT_LETTERS.charAt(i);
            }
        }
        return type;
    }

    // prompt for a filename until a readable file is given
    public static Scanner getFileScanner(Scanner keyboard) {
        Scanner result = null;
        while (result == null) {
            System.out.print("Enter the name of the file with the personality data: ");
            String filename = keyboard.nextLine();
            try {
                result = new Scanner(new File(filename));
            } catch (FileNotFoundException e) {
                System.out.println("Unable to open file: " + filename);
            }
        }
        return result;
    }

    // method to print results
    public static void displayOutput(String name, double[] percentages, String results) {
        int intPercent = 0;
        String strPercent = "";
        System.out.printf("%31s", (name + ":"));
        for (int i = 0; i < NUM_DIMS; i++) {
            if (percentages[i] != NO_ANSWERS) {
                intPercent = (int) (Math.round(percentages[i] * 100));
                strPercent = String.valueOf(intPercent);
                System.out.printf("%11s", strPercent);
            } else {
                System.out.printf("%11s", "NO ANSWERS");
            }
        }
        System.out.println(" = " + results);
    }
}
