import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reads Myers-Briggs quiz answers from a file, computes the percentage of B
 * answers in each of four dimensions, and prints each person's personality type.
 */
public class Personality {

    static final int DIMENSIONS = 4;
    static final int GROUP_SIZE = 7;
    static final int THRESHOLD = 50;
    static final int NO_ANSWERS = -1;
    static final String LEFT_LETTERS = "ESTJ";
    static final String RIGHT_LETTERS = "INFP";
    // dimension index for each position within a group of 7 questions
    static final int[] GROUP_PATTERN = {0, 1, 1, 2, 2, 3, 3};

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Scanner fileScanner = getFileScanner(keyboard);

        int people = Integer.parseInt(fileScanner.nextLine().trim());
        for (int i = 0; i < people; i++) {
            String name = fileScanner.nextLine();
            String answers = fileScanner.nextLine();
            processPerson(name, answers);
        }

        fileScanner.close();
        keyboard.close();
    }

    /** Computes and displays the results for one person. */
    public static void processPerson(String name, String answers) {
        int[] aCounts = new int[DIMENSIONS];
        int[] bCounts = new int[DIMENSIONS];
        countAnswers(answers, aCounts, bCounts);
        int[] percents = computePercentB(aCounts, bCounts);
        String type = determineType(percents);
        displayOutput(name, percents, type);
    }

    /** Tallies A and B answers per dimension; dashes are ignored. */
    public static void countAnswers(String answers, int[] aCounts, int[] bCounts) {
        for (int i = 0; i < answers.length(); i++) {
            int dimension = GROUP_PATTERN[i % GROUP_SIZE];
            char answer = Character.toUpperCase(answers.charAt(i));
            if (answer == 'A') {
                aCounts[dimension]++;
            } else if (answer == 'B') {
                bCounts[dimension]++;
            }
        }
    }

    /** Returns the rounded percentage of B answers per dimension, or NO_ANSWERS. */
    public static int[] computePercentB(int[] aCounts, int[] bCounts) {
        int[] percents = new int[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            int total = aCounts[i] + bCounts[i];
            if (total == 0) {
                percents[i] = NO_ANSWERS;
            } else {
                percents[i] = (int) Math.round(100.0 * bCounts[i] / total);
            }
        }
        return percents;
    }

    /** Builds the four-letter type: X for a tie, - for no answers. */
    public static String determineType(int[] percents) {
        String type = "";
        for (int i = 0; i < DIMENSIONS; i++) {
            if (percents[i] == NO_ANSWERS) {
                type += "-";
            } else if (percents[i] == THRESHOLD) {
                type += "X";
            } else if (percents[i] > THRESHOLD) {
                type += RIGHT_LETTERS.charAt(i);
            } else {
                type += LEFT_LETTERS.charAt(i);
            }
        }
        return type;
    }

    /** Prompts for a filename until a readable file is given. */
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

    /** Prints one formatted result line. */
    public static void displayOutput(String name, int[] percents, String type) {
        System.out.print(name + ":");
        for (int i = 0; i < DIMENSIONS; i++) {
            if (percents[i] == NO_ANSWERS) {
                System.out.print(" NO ANSWERS");
            } else {
                System.out.print(" " + percents[i]);
            }
        }
        System.out.println(" = " + type);
    }
}
