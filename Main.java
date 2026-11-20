import java.util.Random;
import java.util.Scanner;

public class Main {
	
	//seeds the random number generator for the auto-grader
	public static final int SEED = 2;

	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		Random random = new Random(SEED);

		System.out.println("=== DUNGEON DUEL ===");
		System.out.print("Enter your hero's name: ");
		String name = console.nextLine().trim();
		if (name.isEmpty()) {
			name = "Hero";
		}

		GameCharacter hero = createHero(name, random);
		GameCharacter[] enemies = createEnemies(random);

		int wins = 0;
		for (int i = 0; i < enemies.length && hero.isAlive(); i++) {
			System.out.println();
			System.out.println("A wild " + enemies[i].getName() + " appears!");
			if (fight(console, random, hero, enemies[i])) {
				wins++;
				afterVictory(hero, i + 1);
			}
		}

		System.out.println();
		if (!hero.isAlive()) {
			System.out.println("GAME OVER. Fights won: " + wins);
		} else {
			System.out.println("VICTORY! " + hero);
		}
	}

	private static GameCharacter createHero(String name, Random random) {
		int hp = randomInRange(random, 38, 46);
		int str = randomInRange(random, 4, 6);
		int def = randomInRange(random, 1, 3);
		GameCharacter hero = new GameCharacter(name, hp, str, def);
		hero.equipWeapon(new Weapon("Iron Sword", 3, 7, 10));
		hero.addPotion();
		return hero;
	}

	private static GameCharacter[] createEnemies(Random random) {
		GameCharacter[] enemies = new GameCharacter[3];

		int hp = randomInRange(random, 20, 24);
		int str = randomInRange(random, 2, 4);
		int def = randomInRange(random, 0, 1);
		enemies[0] = new GameCharacter("Goblin", hp, str, def);

		hp = randomInRange(random, 34, 40);
		str = randomInRange(random, 4, 5);
		def = randomInRange(random, 1, 2);
		enemies[1] = new GameCharacter("Orc", hp, str, def);
		enemies[1].equipWeapon(new Weapon("Spiked Club", 3, 6, 5));

		hp = randomInRange(random, 52, 60);
		str = randomInRange(random, 5, 6);
		def = randomInRange(random, 2, 3);
		enemies[2] = new GameCharacter("Dragon", hp, str, def);
		enemies[2].equipWeapon(new Weapon("Dragonfang", 4, 7, 10));

		return enemies;
	}

	private static boolean fight(Scanner console, Random random,
			GameCharacter hero, GameCharacter enemy) {
		while (hero.isAlive() && enemy.isAlive()) {
			System.out.println();
			System.out.println(hero + " " + hero.healthBar(20) + " potions: " + hero.getPotionCount());
			System.out.println(enemy + " " + enemy.healthBar(20));
			hero.startTurn();
			heroTurn(console, random, hero, enemy);
			if (enemy.isAlive()) {
				doAttack(enemy, hero, random);
			}
		}
		return hero.isAlive();
	}

	private static void heroTurn(Scanner console, Random random,
			GameCharacter hero, GameCharacter enemy) {
		while (true) {
			System.out.println("1) Attack  2) Defend  3) Use potion");
			int choice = readInt(console, "Choose: ", 1, 3);
			if (choice == 1) {
				doAttack(hero, enemy, random);
				return;
			} else if (choice == 2) {
				hero.defend();
				System.out.println(hero.getName() + " braces for the next blow.");
				return;
			} else if (hero.usePotion()) {
				System.out.println(hero.getName() + " drinks a potion and heals up to "
						+ GameCharacter.POTION_HEAL + " HP.");
				return;
			} else {
				System.out.println("You have no potions!");
			}
		}
	}

	private static void doAttack(GameCharacter attacker, GameCharacter target, Random random) {
		int damage = attacker.attack(target, random);
		String line = attacker.getName() + " hits " + target.getName() + " for " + damage + " damage!";
		Weapon w = attacker.getWeapon();
		if (w != null && w.lastRollWasCrit()) {
			line += " Critical hit!";
		}
		System.out.println(line);
	}

	private static void afterVictory(GameCharacter hero, int n) {
		int xp = 10 + 10 * n;
		System.out.println("Victory! You gain " + xp + " xp.");
		if (hero.gainXp(xp) > 0) {
			System.out.println("Level up! You are now level " + hero.getLevel() + ".");
		}
		if (hero.addPotion()) {
			System.out.println("You find a potion.");
		}
		hero.heal(15);
	}

	 // Returns a random whole number from min to max, inclusive.
    public static int randomInRange(Random rng, int min, int max) {
        return min + rng.nextInt(max - min + 1);
    }
 
    // Reads a whole number in [min, max], re-asking until valid.
    public static int readInt(Scanner console, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!console.hasNextLine()) {
                System.out.println("\nInput ended. Goodbye!");
                System.exit(0);
            }
            try {
                int value = Integer.parseInt(console.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the message
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }

}
