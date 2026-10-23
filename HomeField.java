import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Processes game-result files and reports statistics used to examine whether
 * a home field advantage exists.
 *
 * ANALYSIS: Does the Home Field Advantage exist?
 * TODO: after running all 13 data files, fill in the observed numbers here
 * (home win percentage and average home margin per sport/season) and state
 * the conclusion they support.
 */
public class HomeField {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        boolean another = true;

        while (another) {
            Scanner file = promptForFile(keyboard);

            String sport = file.nextLine().trim();
            String season = file.nextLine().trim();

            int totalGames = 0;
            int homeGames = 0;
            int homeWins = 0;
            long marginSum = 0;

            // single pass through the file
            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                totalGames++;

                String[] tokens = line.split("\\s+");
                // tokens[0] is the date; team 1 name runs up to the first integer
                int score1Index = nextIntegerIndex(tokens, 1);
                int score2Index = nextIntegerIndex(tokens, score1Index + 1);

                int score1 = Integer.parseInt(tokens[score1Index]);
                int score2 = Integer.parseInt(tokens[score2Index]);

                boolean team1Home = tokens[1].startsWith("@");
                boolean team2Home = tokens[score1Index + 1].startsWith("@");

                if (team1Home || team2Home) {
                    int margin = team1Home ? score1 - score2 : score2 - score1;
                    homeGames++;
                    marginSum += margin;
                    if (margin > 0) {
                        homeWins++;
                    }
                }
            }
            file.close();

            printResults(sport, season, totalGames, homeGames, homeWins, marginSum);

            System.out.println("Do you want to check another data set?");
            System.out.print("Enter Y or y to analyze another file, anything else to quit: ");
            String response = keyboard.nextLine().trim();
            another = Character.toUpperCase(response.charAt(0)) == 'Y';
        }
        keyboard.close();
    }

    /** Prompts until the user enters the name of an existing file; returns a Scanner on it. */
    public static Scanner promptForFile(Scanner keyboard) {
        while (true) {
            System.out.print("Enter the file name: ");
            String name = keyboard.nextLine().trim();
            File f = new File(name);
            if (f.isFile()) {
                try {
                    return new Scanner(f);
                } catch (FileNotFoundException e) {
                    // fall through and ask again
                }
            }
        }
    }

    /** Returns the index of the first token at or after start that is an integer. */
    public static int nextIntegerIndex(String[] tokens, int start) {
        int i = start;
        while (!isInteger(tokens[i])) {
            i++;
        }
        return i;
    }

    public static boolean isInteger(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return !s.isEmpty();
    }

    /** Prints the results in the required format. */
    public static void printResults(String sport, String season, int totalGames,
                                    int homeGames, int homeWins, long marginSum) {
        double homePct = totalGames == 0 ? 0 : 100.0 * homeGames / totalGames;
        double winPct = homeGames == 0 ? 0 : 100.0 * homeWins / homeGames;
        double avgMargin = homeGames == 0 ? 0 : (double) marginSum / homeGames;

        System.out.println();
        System.out.println("********** " + sport + " --- " + season + " **********");
        System.out.println("HOME FIELD ADVANTAGE RESULTS");
        System.out.printf("Total number of games: %,d%n", totalGames);
        System.out.printf("Number of games with a home team: %,d%n", homeGames);
        System.out.printf("Percentage of games with a home team: %.1f%%%n", homePct);
        System.out.printf("Number of games the home team won: %,d%n", homeWins);
        System.out.printf("Home team win percentage: %.1f%%%n", winPct);
        System.out.printf("Home team average margin: %.2f%n", avgMargin);
        System.out.println();
    }
}
