import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * CS312 Assignment 6 - HomeFieldAdvantage
 * 
 * On my honor, Jorge Garcia, this programming assignment is my own work, 
 * I have not shared it with others and I will not share it in the future.
 *
 * A program to play determine the extend of home field advantage in sports.
 *
 * Name: Jorge Garcia
 * UTEID: jlg7659
 *  
 */

 
 //  ADD DATA ANALYSIS HERE
 //  TODO: run all 13 data files, then state here whether the home field
 //  advantage exists, citing the home team win percentage and average
 //  home team margin for each sport and season.
 
public class HomeField {

    // Process home field analysis files until the user chooses to quit.
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("A program to analyze home field advantage in sports.");
        System.out.println();
        // DO NOT CREATE ANY OTHER SCANNERS for SYSTEM.IN.
        Scanner keyboard = new Scanner(System.in);

        boolean another = true;
        while (another) {
            Scanner fileScanner = getFileScanner(keyboard);
            processFile(fileScanner);
            fileScanner.close();
            another = askToContinue(keyboard);
        }

        keyboard.close();
    }

    // Prompt until the user enters the name of an existing file,
    // then return a Scanner connected to that file.
    private static Scanner getFileScanner(Scanner keyboard)
            throws FileNotFoundException {
        System.out.print("Enter the file name: ");
        File file = new File(keyboard.nextLine().trim());
        while (!file.exists()) {
            System.out.println("Sorry, that file does not exist");
            System.out.print("Enter the file name: ");
            file = new File(keyboard.nextLine().trim());
        }
        return new Scanner(file);
    }

    // Ask whether to analyze another file. Only the first non-whitespace
    // character of the response matters, and Y is case-insensitive.
    private static boolean askToContinue(Scanner keyboard) {
        System.out.println("Do you want to check another data set?");
        System.out.print("Enter Y or y to analyze another file, anything else to quit: ");
        String response = keyboard.nextLine().trim();
        boolean another = Character.toUpperCase(response.charAt(0)) == 'Y';
        if (another) {
            System.out.println();
        }
        return another;
    }

    // Read every game in the file in a single pass and print the results.
    private static void processFile(Scanner fileScanner) {
        String sport = fileScanner.nextLine().trim();
        String season = fileScanner.nextLine().trim();

        int totalGames = 0;
        int gamesWithHomeTeam = 0;
        int gamesHomeTeamWinner = 0;
        int totalMargin = 0;

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (!line.isEmpty()) {
                totalGames++;
                Scanner lineScanner = new Scanner(line);
                lineScanner.next(); // date
                String team1 = readTeamName(lineScanner);
                int score1 = lineScanner.nextInt();
                String team2 = readTeamName(lineScanner);
                int score2 = lineScanner.nextInt();
                lineScanner.close();

                if (team1.startsWith("@") || team2.startsWith("@")) {
                    int margin = team1.startsWith("@") ? score1 - score2 : score2 - score1;
                    gamesWithHomeTeam++;
                    totalMargin += margin;
                    if (margin > 0) {
                        gamesHomeTeamWinner++;
                    }
                }
            }
        }

        double percentHomeTeamGames = 0.0;
        if (totalGames > 0) {
            percentHomeTeamGames = 100.0 * gamesWithHomeTeam / totalGames;
        }
        double percentHomeTeamWinners = 0.0;
        double averageMargin = 0.0;
        if (gamesWithHomeTeam > 0) {
            percentHomeTeamWinners = 100.0 * gamesHomeTeamWinner / gamesWithHomeTeam;
            averageMargin = (double) totalMargin / gamesWithHomeTeam;
        }

        System.out.println();
        System.out.println("**********   " + sport + " --- " + season + "   **********");
        System.out.println();
        System.out.println("HOME FIELD ADVANTAGE RESULTS");
        System.out.println();
        printResults(totalGames, gamesWithHomeTeam, gamesHomeTeamWinner,
                percentHomeTeamGames, percentHomeTeamWinners, averageMargin);
    }

    // Read tokens up to, but not including, the next integer (the team's score).
    // Returns the team name with single spaces between words.
    private static String readTeamName(Scanner lineScanner) {
        String name = lineScanner.next();
        while (!lineScanner.hasNextInt()) {
            name += " " + lineScanner.next();
        }
        return name;
    }

    // Use this method to print the summary results
	// ** Do not change the code in this method **

    
    
    
    
    
    private static void printResults(int totalGames,
        int gamesWithHomeTeam,
        int gamesHomeTeamWinner,
        double percentHomeTeamGames,
        double percentHomeTeamWinners,
        double margin) {
    
        System.out.printf("Total number of games: %,d\n", totalGames);
        System.out.printf("Number of games with a home team: %,d\n", gamesWithHomeTeam);
        System.out.printf("Percentage of games with a home team:%5.1f%%\n", percentHomeTeamGames);
        System.out.printf("Number of games the home team won: %,d\n", gamesHomeTeamWinner);
        System.out.printf("Home team win percentage:%5.1f", percentHomeTeamWinners);
        System.out.println("%");
        System.out.printf("Home team average margin:%5.2f\n\n", margin);
    }

}
