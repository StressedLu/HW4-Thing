import java.util.Random;

public class GameCharacter {
    public static final int MAX_POTIONS = 3;
    public static final int POTION_HEAL = 15;

    private String name;
    private int maxHealth;
    private int health;
    private int strength;
    private int defense;
    private int level;
    private int xp;
    private Weapon weapon;
    private int potions;
    private boolean defending;

    public GameCharacter(String name, int maxHealth, int strength, int defense) {
        this.name = name;
        this.maxHealth = Math.max(1, maxHealth);
        this.strength = Math.max(0, strength);
        this.defense = Math.max(0, defense);
        this.health = this.maxHealth;
        this.level = 1;
        this.xp = 0;
        this.weapon = null;
        this.potions = 0;
        this.defending = false;
    }

    public GameCharacter(String name) {
        this(name, 40, 5, 2);
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getStrength() { return strength; }
    public int getDefense() { return defense; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public int getPotionCount() { return potions; }
    public Weapon getWeapon() { return weapon; }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isDefending() {
        return defending;
    }

    public Weapon equipWeapon(Weapon w) {
        Weapon old = weapon;
        weapon = w;
        return old;
    }

    public boolean addPotion() {
        if (potions >= MAX_POTIONS) {
            return false;
        }
        potions++;
        return true;
    }

    public boolean usePotion() {
        if (potions <= 0) {
            return false;
        }
        heal(POTION_HEAL);
        potions--;
        return true;
    }

    public void heal(int amount) {
        if (amount < 0) {
            return;
        }
        health = Math.min(maxHealth, health + amount);
    }

    public void startTurn() {
        defending = false;
    }

    public void defend() {
        defending = true;
    }

    public int takeDamage(int rawDamage) {
        int dmg = rawDamage - defense;
        if (defending) {
            dmg = dmg / 2;
        }
        if (dmg < 1) {
            dmg = 1;
        }
        health = Math.max(0, health - dmg);
        return dmg;
    }

    public int attack(GameCharacter target, Random random) {
        int raw = strength;
        if (weapon != null) {
            raw += weapon.rollDamage(random);
        }
        return target.takeDamage(raw);
    }

    public int xpToNextLevel() {
        return 20 * level;
    }

    public int gainXp(int amount) {
        if (amount <= 0) {
            return 0;
        }
        xp += amount;
        int gained = 0;
        while (xp >= xpToNextLevel()) {
            xp -= xpToNextLevel();
            level++;
            maxHealth += 8;
            health = maxHealth;
            strength++;
            if (level % 2 == 0) {
                defense++;
            }
            gained++;
        }
        return gained;
    }

    public String healthBar(int width) {
        int filled = health * width / maxHealth;
        if (health > 0 && filled == 0) {
            filled = 1;
        }
        String bar = "[";
        for (int i = 0; i < width; i++) {
            bar += (i < filled) ? "#" : "-";
        }
        return bar + "]";
    }

    public String toString() {
        return name + " (Lv " + level + ") HP " + health + "/" + maxHealth
                + " STR " + strength + " DEF " + defense;
    }
}
