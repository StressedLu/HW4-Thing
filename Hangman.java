import java.util.Scanner;

/**
 * Plays multiple rounds of Hangman. Each round uses a new secret phrase from the
 * PhraseBank. The player guesses one letter at a time; the round ends when the whole
 * phrase is revealed (win) or MAX_NUM_GUESSES wrong guesses are made (loss).
 *
 * Design note: methods that compute things return values; methods that display things
 * do the printing.
 */
public class Hangman {

    // Number of wrong guesses allowed before the player loses.
    public static final int MAX_NUM_GUESSES = 5;
    // Every letter available to guess at the start of a round (no hyphens stored here).
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Program entry point. Prints the intro once, then repeatedly announces the topic,
     * plays a round with the next phrase, and asks whether to keep playing.
     * The Scanner and PhraseBank are created once here and passed to other methods.
     */
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        PhraseBank phrases = new PhraseBank();
        printIntro();
        boolean done = false;
        while (!done) {
            System.out.println("I am thinking of a " + phrases.getTopic() + " ...");
            playRound(console, phrases.getNextPhrase());
            done = isGameEnd(console);   // true when the user does not want another round
        }
    }

    /** Displays the welcome message and the rules of the game. */
    public static void printIntro() {
        System.out.println("This program plays the game of hangman.");
        System.out.println("The computer will pick a random phrase.");
        System.out.println("Input letters for your guess.");
        System.out.println("After " + MAX_NUM_GUESSES + " wrong guesses you lose.");
    }

    /**
     * Plays one round for the given phrase, until the user wins or loses.
     * Tracks three pieces of state: the masked phrase shown to the user, the letters
     * not yet guessed, and the count of wrong guesses.
     *
     * @param console the shared Scanner for keyboard input
     * @param phrase  the secret phrase (uppercase, spaces already changed to underscores)
     */
    public static void playRound(Scanner console, String phrase) {
        // Setup: hide every letter, and start with the full alphabet available.
        String masked = maskPhrase(phrase);
        String notGuessed = ALPHABET;
        int wrong = 0;
        System.out.println("The current phrase is " + masked);

        // Keep guessing while unsolved (an asterisk remains) and guesses remain.
        while (masked.indexOf('*') >= 0 && wrong < MAX_NUM_GUESSES) {
            char guess = getGuess(console, notGuessed);
            boolean present = phrase.indexOf(guess) >= 0;
            if (present) {
                masked = revealLetter(phrase, masked, guess);   // uncover all matches
            } else {
                wrong++;
            }
            // Either way, this letter can no longer be guessed.
            notGuessed = removeLetter(notGuessed, guess);
            printGuessResult(guess, present, wrong);
            // Only show the phrase again if another guess is coming; the end-of-round
            // message shows the full phrase instead.
            if (masked.indexOf('*') >= 0 && wrong < MAX_NUM_GUESSES) {
                System.out.println("The current phrase is " + masked);
            }
        }
        // Loop ended: if the user still has guesses left, they must have solved it.
        printResult(phrase, wrong < MAX_NUM_GUESSES);
    }

    /**
     * Returns a hidden version of the phrase: every letter becomes '*', while
     * underscores (the word separators) stay in place.
     * Example: "PULP_FICTION" becomes "****_*******".
     */
    public static String maskPhrase(String phrase) {
        String masked = "";
        for (int i = 0; i < phrase.length(); i++) {
            masked += (phrase.charAt(i) == '_') ? "_" : "*";
        }
        return masked;
    }

    /**
     * Returns a copy of masked in which every position where phrase contains guess is
     * replaced by that letter. phrase and masked have the same length, so a position
     * in one corresponds to the same position in the other. Strings are immutable, so
     * each replacement is built as: before + letter + after.
     */
    public static String revealLetter(String phrase, String masked, char guess) {
        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.charAt(i) == guess) {
                masked = masked.substring(0, i) + guess + masked.substring(i + 1);
            }
        }
        return masked;
    }

    /**
     * Returns letters with the given letter taken out, by joining the part before it
     * and the part after it. Assumes the letter is present (guesses are validated first).
     */
    public static String removeLetter(String letters, char letter) {
        int pos = letters.indexOf(letter);
        return letters.substring(0, pos) + letters.substring(pos + 1);
    }

    /**
     * Repeatedly shows the available letters and prompts until the user enters a valid
     * guess, then returns it as an uppercase letter. Only the first character of the
     * input is used (so "steam" guesses S), and lowercase is converted to uppercase.
     * Invalid input is echoed back unedited with a message, and the user is re-prompted.
     */
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

    /**
     * A guess is valid only if it is still in the not-guessed string. This one check
     * rejects both non-letters (digits, '*', '_') and letters already guessed, since
     * neither appears in that string.
     */
    public static boolean isValidGuess(char guess, String notGuessed) {
        return notGuessed.indexOf(guess) >= 0;
    }

    /**
     * Displays the letters not yet guessed with "--" between them, e.g. A--B--C.
     * The hyphens exist only in this display; the stored string has none.
     */
    public static void printNotGuessed(String notGuessed) {
        System.out.println("The letters you have not guessed yet are:");
        String display = "";
        for (int i = 0; i < notGuessed.length(); i++) {
            if (i > 0) {
                display += "--";   // separator goes between letters, not before the first
            }
            display += notGuessed.charAt(i);
        }
        System.out.println(display);
    }

    /**
     * Displays the outcome of one guess: which letter was guessed, whether it was in
     * the phrase, and the running count of wrong guesses.
     */
    public static void printGuessResult(char guess, boolean present, int wrong) {
        System.out.println("You guessed " + guess + ".");
        if (present) {
            System.out.println("That is present in the secret phrase.");
        } else {
            System.out.println("That is not present in the secret phrase.");
        }
        System.out.println("Number of wrong guesses so far: " + wrong);
    }

    /** Displays the end-of-round message, including the unmasked phrase. */
    public static void printResult(String phrase, boolean won) {
        if (won) {
            System.out.println("The phrase is " + phrase + ".");
            System.out.println("You win!!");
        } else {
            System.out.println("You lose. The secret phrase was " + phrase);
        }
    }

    /**
     * Asks whether the user wants another round. Returns true if the game should end
     * (anything other than Y or y), false to continue with the next phrase.
     */
    public static boolean isGameEnd(Scanner console) {
        System.out.println("Do you want to play again?");
        System.out.print("Enter 'Y' or 'y' to play again: ");
        String answer = console.nextLine().trim().toUpperCase();
        return !answer.startsWith("Y");
    }
}
