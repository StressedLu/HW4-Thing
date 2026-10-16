import java.util.Scanner;

public class Hangman {

    public static final int MAX_NUM_GUESSES = 5;
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        PhraseBank phrases = new PhraseBank();
        printIntro();
        boolean done = false;
        while (!done) {
            System.out.println("I am thinking of a " + phrases.getTopic() + " ...");
            playRound(console, phrases.getNextPhrase());
            done = isGameEnd(console);
        }
    }

    public static void printIntro() {
        System.out.println("This program plays the game of hangman.");
        System.out.println("The computer will pick a random phrase.");
        System.out.println("Input letters for your guess.");
        System.out.println("After " + MAX_NUM_GUESSES + " wrong guesses you lose.");
    }

    /** Plays one round for the given phrase, until the user wins or loses. */
    public static void playRound(Scanner console, String phrase) {
        String masked = maskPhrase(phrase);
        String notGuessed = ALPHABET;
        int wrong = 0;
        System.out.println("The current phrase is " + masked);
        while (masked.indexOf('*') >= 0 && wrong < MAX_NUM_GUESSES) {
            char guess = getGuess(console, notGuessed);
            boolean present = phrase.indexOf(guess) >= 0;
            if (present) {
                masked = revealLetter(phrase, masked, guess);
            } else {
                wrong++;
            }
            notGuessed = removeLetter(notGuessed, guess);
            printGuessResult(guess, present, wrong);
            if (masked.indexOf('*') >= 0 && wrong < MAX_NUM_GUESSES) {
                System.out.println("The current phrase is " + masked);
            }
        }
        printResult(phrase, wrong < MAX_NUM_GUESSES);
    }

    /** Returns the phrase with every letter replaced by '*'; underscores stay. */
    public static String maskPhrase(String phrase) {
        String masked = "";
        for (int i = 0; i < phrase.length(); i++) {
            masked += (phrase.charAt(i) == '_') ? "_" : "*";
        }
        return masked;
    }

    /** Returns masked with every occurrence of guess in phrase revealed. */
    public static String revealLetter(String phrase, String masked, char guess) {
        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.charAt(i) == guess) {
                masked = masked.substring(0, i) + guess + masked.substring(i + 1);
            }
        }
        return masked;
    }

    /** Returns letters without the given letter. */
    public static String removeLetter(String letters, char letter) {
        int pos = letters.indexOf(letter);
        return letters.substring(0, pos) + letters.substring(pos + 1);
    }

    /** Prompts until the user enters a valid guess; returns it as an uppercase letter. */
    public static char getGuess(Scanner console, String notGuessed) {
        while (true) {
            printNotGuessed(notGuessed);
            System.out.print("Enter your next guess: ");
            String input = console.nextLine().trim();
            char first = Character.toUpperCase(input.charAt(0));
            if (isValidGuess(first, notGuessed)) {
                return first;
            }
            System.out.println(input + " is not a valid guess.");
        }
    }

    /** A guess is valid if it is a letter that has not been guessed yet. */
    public static boolean isValidGuess(char guess, String notGuessed) {
        return notGuessed.indexOf(guess) >= 0;
    }

    /** Displays the letters not yet guessed, separated by "--". */
    public static void printNotGuessed(String notGuessed) {
        System.out.println("The letters you have not guessed yet are:");
        String display = "";
        for (int i = 0; i < notGuessed.length(); i++) {
            if (i > 0) {
                display += "--";
            }
            display += notGuessed.charAt(i);
        }
        System.out.println(display);
    }

    public static void printGuessResult(char guess, boolean present, int wrong) {
        System.out.println("You guessed " + guess + ".");
        if (present) {
            System.out.println("That is present in the secret phrase.");
        } else {
            System.out.println("That is not present in the secret phrase.");
        }
        System.out.println("Number of wrong guesses so far: " + wrong);
    }

    public static void printResult(String phrase, boolean won) {
        if (won) {
            System.out.println("The phrase is " + phrase + ".");
            System.out.println("You win!!");
        } else {
            System.out.println("You lose. The secret phrase was " + phrase);
        }
    }

    /** Asks whether to play again; returns true if the program should end. */
    public static boolean isGameEnd(Scanner console) {
        System.out.println("Do you want to play again?");
        System.out.print("Enter 'Y' or 'y' to play again: ");
        String answer = console.nextLine().trim().toUpperCase();
        return !answer.startsWith("Y");
    }
}
