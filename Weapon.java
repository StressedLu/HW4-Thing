import java.util.Random;

public class Weapon {
    private String name;
    private int minDamage;
    private int maxDamage;
    private int critChance;
    private boolean lastCrit;

    public Weapon(String name, int minDamage, int maxDamage, int critChance) {
        this.name = name;
        this.minDamage = Math.max(1, minDamage);
        this.maxDamage = Math.max(this.minDamage, maxDamage);
        this.critChance = Math.max(0, Math.min(100, critChance));
        this.lastCrit = false;
    }

    public String getName() {
        return name;
    }

    public int rollDamage(Random random) {
        int damage = minDamage + random.nextInt(maxDamage - minDamage + 1);
        boolean crit = random.nextInt(100) < critChance;
        if (crit) {
            damage *= 2;
        }
        lastCrit = crit;
        return damage;
    }

    public boolean lastRollWasCrit() {
        return lastCrit;
    }

    public String toString() {
        return name + " (" + minDamage + "-" + maxDamage + " damage, " + critChance + "% crit)";
    }
}
