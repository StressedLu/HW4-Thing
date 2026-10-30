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
    // Thought process / build order (design a little, code a little, test a little):
    //   Step 1: countAnswers    - String of 70 letters -> A and B counters per dimension
    //   Step 2: computePercentB - counters -> percentage of B answers per dimension
    //   Step 3: determineType   - percentages -> 4 letter personality type
    //   Step 4: processPerson   - ties steps 1-3 together for one person
    //   Step 5: main            - read the file and call processPerson for each person
    //   getFileScanner / displayOutput are helpers for input and output.
    public static final int NUM_DIMS = 4;
    public static final double NO_ANSWERS = -1.0;
    public static final double THRESHOLD = 0.5;
    public static final int GROUP_SIZE = 7;
    public static final String LEFT_LETTERS = "ESTJ";
    public static final String RIGHT_LETTERS = "INFP";
    // dimension index for each question's position within a group of 7
    public static final int[] GROUP_PATTERN = {0, 1, 1, 2, 2, 3, 3};

    // Step 5: process the data from the personality tests, one person at a time
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

    // ADD METHODS HERE (listed in the order they would be built)

    // Step 1 (start here): count A and B answers per dimension.
    // Question i belongs to dimension GROUP_PATTERN[i % 7]; dashes are skipped.
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

    // Step 2: fraction of B answers per dimension (0 to 1), or NO_ANSWERS
    // when the person skipped every question (avoids dividing by zero)
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

    // Step 3: pick a letter per dimension. Above THRESHOLD -> B side letter,
    // below -> A side letter, exactly at THRESHOLD -> X, no answers -> -
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

    // Step 4: run steps 1-3 for one person and print the result
    public static void processPerson(String name, String answers) {
        int[] aCounts = new int[NUM_DIMS];
        int[] bCounts = new int[NUM_DIMS];
        countAnswers(answers, aCounts, bCounts);
        double[] percentages = computePercentB(aCounts, bCounts);
        displayOutput(name, percentages, determineType(percentages));
    }

    // helper: ask for a filename until a readable file is given
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

    // helper: print one formatted result line
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
